package fr.neraud.padlistener.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import fr.neraud.padlistener.constant.SyncMaterialInMonster;
import fr.neraud.padlistener.exception.UnknownMonsterException;
import fr.neraud.padlistener.model.BaseMonsterModel;
import fr.neraud.padlistener.model.CapturedMonsterCardModel;
import fr.neraud.padlistener.model.CapturedPlayerInfoModel;
import fr.neraud.padlistener.model.ComputeSyncResultModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.model.SyncedMaterialModel;
import fr.neraud.padlistener.model.SyncedMonsterModel;
import fr.neraud.padlistener.model.SyncedUserInfoModel;
import fr.neraud.padlistener.model.UserInfoMaterialModel;
import fr.neraud.padlistener.model.UserInfoModel;
import fr.neraud.padlistener.model.UserInfoMonsterModel;

/**
 * Helper to prepare the sync models. Compares the captured and PADherder data and merges the monsters.
 *
 * @author Neraud
 */
public class SyncHelper {

	private final DefaultSharedPreferencesHelper helper;
	private final MonsterInfoHelper monsterInfoHelper;
	private boolean hasEncounteredUnknownMonster = false;

	public SyncHelper(Context context, List<MonsterInfoModel> monsterInfos) {
		super();
		this.helper = new DefaultSharedPreferencesHelper(context);
		this.monsterInfoHelper = new MonsterInfoHelper(monsterInfos);
	}

	public ComputeSyncResultModel sync(List<CapturedMonsterCardModel> capturedMonsters, CapturedPlayerInfoModel capturedInfo, UserInfoModel padInfo) {
		Log.d(getClass().getName(), "sync");

		final Map<Integer, UserInfoMaterialModel> padherderMaterialsById = reorgPadherderMaterials(padInfo.getMaterials());
		final Map<Integer, List<UserInfoMonsterModel>> padherderMonstersById = reorgPadherderMonster(padInfo.getMonsters());

		final Map<Integer, Integer> capturedMaterialsById = reorgCapturedMaterials(capturedMonsters);
		final Map<Integer, List<CapturedMonsterCardModel>> capturedMonstersById = reorgCapturedMonster(capturedMonsters);

		filterMaterials(padherderMaterialsById, padherderMonstersById, capturedMaterialsById, capturedMonstersById);

		final Map<Integer, List<CapturedMonsterCardModel>> capturedMonstersByBaseId = reorgMonstersByBaseId(capturedMonstersById);
		final Map<Integer, List<UserInfoMonsterModel>> padherderMonstersByBaseId = reorgMonstersByBaseId(padherderMonstersById);

		final ComputeSyncResultModel result = new ComputeSyncResultModel();

		final SyncedUserInfoModel syncedUserInfo = syncRank(capturedInfo, padInfo);
		final List<SyncedMaterialModel> syncedMaterials = syncMaterials(capturedMaterialsById, padherderMaterialsById);
		final List<SyncedMonsterModel> syncedMonsters = syncMonsters(capturedMonstersByBaseId, padherderMonstersByBaseId);

		result.setSyncedUserInfo(syncedUserInfo);
		result.setSyncedMaterials(syncedMaterials);
		result.setSyncedMonsters(syncedMonsters);

		result.setHasEncounteredUnknownMonster(hasEncounteredUnknownMonster);

		return result;
	}

	@SuppressLint("UseSparseArrays")
	private Map<Integer, List<CapturedMonsterCardModel>> reorgCapturedMonster(List<CapturedMonsterCardModel> capturedMonsters) {
		Log.d(getClass().getName(), "reorgCapturedMonster");
		final Map<Integer, List<CapturedMonsterCardModel>> capturedMonstersById = new HashMap<Integer, List<CapturedMonsterCardModel>>();

		for (final CapturedMonsterCardModel capturedMonster : capturedMonsters) {
			final int capturedId = capturedMonster.getIdJp();
			Log.d(getClass().getName(), "reorgCapturedMonster : - capturedId = " + capturedId);
			try {
				final MonsterInfoModel monsterInfo = monsterInfoHelper.getMonsterInfo(capturedId);

				if (!capturedMonstersById.containsKey(capturedId)) {
					capturedMonstersById.put(capturedId, new ArrayList<CapturedMonsterCardModel>());
				}

				capMonsterData(monsterInfo, capturedMonster);
				capturedMonstersById.get(capturedId).add(capturedMonster);
			} catch (UnknownMonsterException e) {
				Log.w(getClass().getName(), "reorgCapturedMonster : missing monster info for capturedId = " + e.getMonsterId());
				hasEncounteredUnknownMonster = true;
			}
		}

		// Sort monsters DESC
		final Comparator<BaseMonsterModel> comparatorDesc = Collections.reverseOrder(new MonsterComparator(monsterInfoHelper));
		for (final List<CapturedMonsterCardModel> capturedMonstersList : capturedMonstersById.values()) {
			Collections.sort(capturedMonstersList, comparatorDesc);
		}

		return capturedMonstersById;
	}

	private void capMonsterData(MonsterInfoModel monsterInfo, CapturedMonsterCardModel capturedMonster) {
		// PAD allows to exp a monster beyond its max, but PADHerder caps to the max exp
		final MonsterExpHelper expHelper = new MonsterExpHelper(monsterInfo);
		final long expCap = expHelper.getExpCap();
		if (capturedMonster.getExp() > expCap) {
			Log.d(getClass().getName(), "capMonsterData : capping monster xp for " + capturedMonster);
			capturedMonster.setExp(expCap);
		}

		// PAD allows to awaken a monster beyond its max, but PADHerder caps to the max awakenings
		final int maxAwakenings = monsterInfo.getAwokenSkillIds() == null ? 0 : monsterInfo.getAwokenSkillIds().size();
		if (capturedMonster.getAwakenings() > maxAwakenings) {
			Log.d(getClass().getName(), "capMonsterData : capping monster awakenings for " + capturedMonster);
			capturedMonster.setAwakenings(maxAwakenings);
		}

		// TODO cap skillups and plusses ?
	}

	@SuppressLint("UseSparseArrays")
	private Map<Integer, Integer> reorgCapturedMaterials(List<CapturedMonsterCardModel> capturedMonsters) {
		Log.d(getClass().getName(), "reorgCapturedMaterials");
		final Map<Integer, Integer> capturedMaterialsById = new HashMap<Integer, Integer>();

		for (final CapturedMonsterCardModel capturedMonster : capturedMonsters) {
			final Integer capturedId = capturedMonster.getIdJp();
			try {
				// Get monster info to check if the captureId exists
				monsterInfoHelper.getMonsterInfo(capturedId);

				Integer count = capturedMaterialsById.get(capturedId);
				if (count == null) {
					count = 0;
				}
				capturedMaterialsById.put(capturedId, ++count);
			} catch (UnknownMonsterException e) {
				Log.w(getClass().getName(), "reorgCapturedMaterials : missing material info for capturedId = " + e.getMonsterId());
				hasEncounteredUnknownMonster = true;
			}
		}

		return capturedMaterialsById;
	}

	@SuppressLint("UseSparseArrays")
	private Map<Integer, List<UserInfoMonsterModel>> reorgPadherderMonster(List<UserInfoMonsterModel> monsters) {
		Log.d(getClass().getName(), "reorgPadherderMonster");

		final Map<Integer, List<UserInfoMonsterModel>> padherderMonstersById = new HashMap<Integer, List<UserInfoMonsterModel>>();

		for (final UserInfoMonsterModel monster : monsters) {
			final int padherderId = monster.getIdJp();
			try {
				// Get monster info to check if the captureId exists
				monsterInfoHelper.getMonsterInfo(padherderId);
				if (!padherderMonstersById.containsKey(padherderId)) {
					padherderMonstersById.put(padherderId, new ArrayList<UserInfoMonsterModel>());
				}
				padherderMonstersById.get(padherderId).add(monster);
			} catch (UnknownMonsterException e) {
				Log.w(getClass().getName(), "reorgPadherderMonster : missing monster info for padherderId = " + e.getMonsterId());
				hasEncounteredUnknownMonster = true;
			}
		}

		// Sort monsters DESC
		final Comparator<BaseMonsterModel> comparatorDesc = Collections.reverseOrder(new MonsterComparator(monsterInfoHelper));
		for (final List<UserInfoMonsterModel> capturedMonstersList : padherderMonstersById.values()) {
			Collections.sort(capturedMonstersList, comparatorDesc);
		}

		return padherderMonstersById;
	}

	@SuppressLint("UseSparseArrays")
	private Map<Integer, UserInfoMaterialModel> reorgPadherderMaterials(List<UserInfoMaterialModel> materials) {
		Log.d(getClass().getName(), "reorgPadherderMaterials");
		final Map<Integer, UserInfoMaterialModel> padherderMaterialsById = new HashMap<Integer, UserInfoMaterialModel>();

		for (final UserInfoMaterialModel material : materials) {
			final int padherderId = material.getId();
			try {
				// Get monster info to check if the captureId exists
				monsterInfoHelper.getMonsterInfo(padherderId);
				padherderMaterialsById.put(material.getId(), material);
			} catch (UnknownMonsterException e) {
				Log.w(getClass().getName(), "reorgPadherderMaterials : missing monster info for padherderId = " + e.getMonsterId());
				hasEncounteredUnknownMonster = true;
			}
		}

		return padherderMaterialsById;
	}

	private void filterMaterials(Map<Integer, UserInfoMaterialModel> padherderMaterialsById,
			Map<Integer, List<UserInfoMonsterModel>> padherderMonstersById, Map<Integer, Integer> capturedMaterialsById,
			Map<Integer, List<CapturedMonsterCardModel>> capturedMonstersById) {
		Log.d(getClass().getName(), "filterMaterials");

		final SyncMaterialInMonster syncMaterialInMonster = helper.getSyncMaterialInMonster();
		final boolean syncDeductMonsterInMaterial = helper.isSyncDeductMonsterInMaterial();

		if (syncMaterialInMonster != SyncMaterialInMonster.ALWAYS || syncDeductMonsterInMaterial) {
			for (final Integer materialId : padherderMaterialsById.keySet()) {
				if (capturedMonstersById.containsKey(materialId)) {
					Log.d(getClass().getName(), "filterMaterials : materialId : " + materialId);
					if (syncMaterialInMonster == SyncMaterialInMonster.NEVER) {
						Log.d(getClass().getName(), "filterMaterials : removing all from captured monsters");
						capturedMonstersById.remove(materialId);
					} else if (syncMaterialInMonster == SyncMaterialInMonster.ONLY_IF_ALREADY_IN_PADHERDER) {
						if (padherderMaterialsById.containsKey(materialId)) {
							final int numberToRemove = padherderMaterialsById.get(materialId).getQuantity();
							Log.d(getClass().getName(), "filterMaterials : in padherder's materials, removing " + numberToRemove + " from captured monsters");

							final List<CapturedMonsterCardModel> capturedMonsterList = capturedMonstersById.get(materialId);
							// Start an iterator at the end of the list, to remove the lowest monsters first
							final ListIterator<CapturedMonsterCardModel> iter = capturedMonsterList.listIterator(capturedMonsterList.size());
							int i = 0;
							while (iter.hasPrevious() && i < numberToRemove) {
								iter.previous();
								iter.remove();
								i++;
							}
						} else {
							Log.d(getClass().getName(), "filterMaterials : not in padherder's materials, nothing to do");
						}
					} else {
						Log.d(getClass().getName(), "filterMaterials : not removing from monsters");
					}

					// Same if than above, but a remove can occur, so we need to check again
					if (syncDeductMonsterInMaterial) {
						if (padherderMonstersById.containsKey(materialId)) {
							final int nbAlreadyMonsters = padherderMonstersById.get(materialId).size();
							Log.d(getClass().getName(), "filterMaterials : in capturedMonster, reducing captured quantity by " + nbAlreadyMonsters);
							final int old = capturedMaterialsById.get(materialId);
							capturedMaterialsById.put(materialId, old - nbAlreadyMonsters);
						} else {
							Log.d(getClass().getName(), "filterMaterials : not in capturedMonster, nothing to do");
						}
					}
				}
			}
		} else {
			Log.d(getClass().getName(), "filterMaterials : nothing to filter");
		}
	}

	private SyncedUserInfoModel syncRank(CapturedPlayerInfoModel capturedInfo, UserInfoModel padInfo) {
		Log.d(getClass().getName(), "syncRank");
		final SyncedUserInfoModel syncedPlayerInfo = new SyncedUserInfoModel();
		syncedPlayerInfo.setCapturedInfo(capturedInfo.getRank());
		syncedPlayerInfo.setPadherderInfo(padInfo.getRank());
		syncedPlayerInfo.setProfileApiId(padInfo.getProfileApiId());

		return syncedPlayerInfo;
	}

	private List<SyncedMaterialModel> syncMaterials(Map<Integer, Integer> capturedMaterialsById,
			Map<Integer, UserInfoMaterialModel> padherderMaterialsById) {
		Log.d(getClass().getName(), "syncMaterials");

		final List<SyncedMaterialModel> syncedMaterials = new ArrayList<SyncedMaterialModel>();
		final Set<Integer> materialIds = new HashSet<Integer>();
		materialIds.addAll(padherderMaterialsById.keySet());
		// PADHerder always list all the materials it knows. Some "materials" (ex : High Emerald Dragon, id 259) are not tracked in PADHerder, and should not be used in materials then
		//materialIds.addAll(capturedMaterialsById.keySet());

		for (final Integer materialId : materialIds) {
			final UserInfoMaterialModel padherderMaterial = padherderMaterialsById.get(materialId);
			try {
				final SyncedMaterialModel syncedMaterial = new SyncedMaterialModel();
				final MonsterInfoModel monsterInfo = monsterInfoHelper.getMonsterInfo(materialId);
				syncedMaterial.setPadherderMonsterInfo(monsterInfo);
				syncedMaterial.setCapturedMonsterInfo(monsterInfo);
				syncedMaterial.setPadherderId(padherderMaterial.getPadherderId());
				syncedMaterial.setCapturedInfo(capturedMaterialsById.containsKey(materialId) ? capturedMaterialsById.get(materialId) : 0);
				syncedMaterial.setPadherderInfo(padherderMaterial.getQuantity());

				Log.d(getClass().getName(), "syncedMaterial : - " + syncedMaterial);
				syncedMaterials.add(syncedMaterial);
			} catch (UnknownMonsterException e) {
				// Should not happen as materials are checked before
				Log.w(getClass().getName(), "syncMaterials : missing material info for materialId = " + e.getMonsterId());
				hasEncounteredUnknownMonster = true;
			}
		}

		return syncedMaterials;
	}

	private <T extends BaseMonsterModel> Map<Integer, List<T>> reorgMonstersByBaseId(Map<Integer, List<T>> capturedMonstersById) {
		final Map<Integer, List<T>> capturedMonstersByBaseId = new HashMap<Integer, List<T>>();
		for (final Integer id : capturedMonstersById.keySet()) {
			try {
				final Integer baseId = monsterInfoHelper.getMonsterInfo(id).getBaseMonsterId();
				if (!capturedMonstersByBaseId.containsKey(baseId)) {
					capturedMonstersByBaseId.put(baseId, new ArrayList<T>());
				}
				capturedMonstersByBaseId.get(baseId).addAll(capturedMonstersById.get(id));
			} catch (UnknownMonsterException e) {
				// Should not happen as monsters are checked before during the reorg phase
				Log.w(getClass().getName(), "reorgMonstersByBaseId : missing monster info for id = " + e.getMonsterId());
				hasEncounteredUnknownMonster = true;
			}
		}
		final Comparator<BaseMonsterModel> comparatorDesc = Collections.reverseOrder(new MonsterComparator(monsterInfoHelper));
		for (final Integer id : capturedMonstersByBaseId.keySet()) {
			Collections.sort(capturedMonstersByBaseId.get(id), comparatorDesc);
		}

		return capturedMonstersByBaseId;
	}

	private List<SyncedMonsterModel> syncMonsters(Map<Integer, List<CapturedMonsterCardModel>> capturedMonstersById,
			Map<Integer, List<UserInfoMonsterModel>> padherderMonstersById) {
		Log.d(getClass().getName(), "syncMonsters");

		final List<SyncedMonsterModel> syncedMonsters = new ArrayList<SyncedMonsterModel>();
		final Set<Integer> monsterIds = new HashSet<Integer>();
		monsterIds.addAll(capturedMonstersById.keySet());
		monsterIds.addAll(padherderMonstersById.keySet());

		for (final Integer monsterId : monsterIds) {
			final List<CapturedMonsterCardModel> capturedMonsters = capturedMonstersById.get(monsterId);
			final List<UserInfoMonsterModel> padherderMonsters = padherderMonstersById.get(monsterId);

			final List<SyncedMonsterModel> syncedMonstersForId = syncMonstersForOneId(monsterId, capturedMonsters, padherderMonsters);

			syncedMonsters.addAll(syncedMonstersForId);
		}

		return syncedMonsters;
	}

	private List<SyncedMonsterModel> syncMonstersForOneId(Integer monsterId, List<CapturedMonsterCardModel> capturedMonsters,
			List<UserInfoMonsterModel> padherderMonsters) {
		Log.d(getClass().getName(), "syncMonstersForOneId : " + monsterId);
		final List<SyncedMonsterModel> syncedMonsters = new ArrayList<SyncedMonsterModel>();

		Log.d(getClass().getName(), "syncMonstersForOneId : capturedMonsters = " + capturedMonsters);
		Log.d(getClass().getName(), "syncMonstersForOneId : padherderMonsters = " + padherderMonsters);

		final List<CapturedMonsterCardModel> capturedMonstersWork = new ArrayList<CapturedMonsterCardModel>();
		if (capturedMonsters != null) {
			capturedMonstersWork.addAll(capturedMonsters);
		}
		final List<UserInfoMonsterModel> padherderMonstersWork = new ArrayList<UserInfoMonsterModel>();
		if (padherderMonsters != null) {
			padherderMonstersWork.addAll(padherderMonsters);
		}

		final Iterator<CapturedMonsterCardModel> capturedIter = capturedMonstersWork.iterator();

		// Filter "equals" monsters
		capturedLoop:
		while (capturedIter.hasNext()) {
			final CapturedMonsterCardModel captured = capturedIter.next();
			final Iterator<UserInfoMonsterModel> parherderIter = padherderMonstersWork.iterator();
			while (parherderIter.hasNext()) {
				final UserInfoMonsterModel padherder = parherderIter.next();
				if (MonsterComparatorHelper.areMonstersEqual(captured, padherder)) {
					Log.d(getClass().getName(), "syncMonstersForOneId : equals, removing : " + captured);
					capturedIter.remove();
					parherderIter.remove();
					continue capturedLoop;
				}
			}
		}

		final int nbCaptured = capturedMonstersWork.size();
		final int nbPadherder = padherderMonstersWork.size();

		for (int i = 0; i < Math.max(nbCaptured, nbPadherder); i++) {
			final SyncedMonsterModel model = new SyncedMonsterModel();
			try {
				if (i < nbCaptured && i < nbPadherder) {
					// Update while we have enough monsters in each list
					model.setPadherderId(padherderMonstersWork.get(i).getPadherderId());

					final UserInfoMonsterModel padherder = padherderMonstersWork.get(i);
					final CapturedMonsterCardModel captured = capturedMonstersWork.get(i);

					model.setPadherderMonsterInfo(monsterInfoHelper.getMonsterInfo(padherder.getIdJp()));
					model.setCapturedMonsterInfo(monsterInfoHelper.getMonsterInfo(captured.getIdJp()));

					// Keep priority and note
					captured.setPriority(padherder.getPriority());
					captured.setNote(padherder.getNote());

					model.setCapturedInfo(captured);
					model.setPadherderInfo(padherder);
					Log.d(getClass().getName(), "syncMonstersForOneId : updated : " + model);
				} else if (i < nbCaptured) {
					// more captured -> creating
					final CapturedMonsterCardModel captured = capturedMonstersWork.get(i);

					model.setCapturedMonsterInfo(monsterInfoHelper.getMonsterInfo(captured.getIdJp()));

					// default create priority
					captured.setPriority(helper.getDefaultMonsterCreatePriority());
					model.setCapturedInfo(capturedMonstersWork.get(i));

					model.setPadherderInfo(null);
					Log.d(getClass().getName(), "syncMonstersForOneId : added : " + model);
				} else {
					// not enough captured -> deleting
					final UserInfoMonsterModel padherder = padherderMonstersWork.get(i);

					model.setPadherderMonsterInfo(monsterInfoHelper.getMonsterInfo(padherder.getIdJp()));

					model.setPadherderId(padherder.getPadherderId());
					model.setCapturedInfo(null);
					model.setPadherderInfo(padherderMonstersWork.get(i));
					Log.d(getClass().getName(), "syncMonstersForOneId : deleted : " + model);
				}

				syncedMonsters.add(model);
			} catch (UnknownMonsterException e) {
				// Should not happen as monsters are checked before during the reorg phase
				Log.w(getClass().getName(), "syncMonstersForOneId : missing monster info for id = " + e.getMonsterId());
				hasEncounteredUnknownMonster = true;
			}
		}

		return syncedMonsters;
	}
}
