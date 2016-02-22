package fr.neraud.padlistener.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

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

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.constant.SyncMaterialInMonster;
import fr.neraud.padlistener.exception.UnknownMonsterException;
import fr.neraud.padlistener.model.CapturedPlayerInfoModel;
import fr.neraud.padlistener.model.ComputeSyncResultModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.model.MonsterModel;
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

	public ComputeSyncResultModel sync(List<MonsterModel> capturedMonsters, CapturedPlayerInfoModel capturedInfo, UserInfoModel padInfo) {
		MyLog.entry();

		final Map<Integer, UserInfoMaterialModel> padherderMaterialsById = reorgPadherderMaterials(padInfo.getMaterials());
		final Map<Integer, List<UserInfoMonsterModel>> padherderMonstersById = reorgPadherderMonster(padInfo.getMonsters());

		final Map<Integer, Integer> capturedMaterialsById = reorgCapturedMaterials(capturedMonsters);
		final Map<Integer, List<MonsterModel>> capturedMonstersById = reorgCapturedMonster(capturedMonsters);

		filterMaterials(padherderMaterialsById, padherderMonstersById, capturedMaterialsById, capturedMonstersById);

		final Map<Integer, List<MonsterModel>> capturedMonstersByBaseId = reorgMonstersByBaseId(capturedMonstersById);
		final Map<Integer, List<UserInfoMonsterModel>> padherderMonstersByBaseId = reorgMonstersByBaseId(padherderMonstersById);

		final ComputeSyncResultModel result = new ComputeSyncResultModel();

		final SyncedUserInfoModel syncedUserInfo = syncRank(capturedInfo, padInfo);
		final List<SyncedMaterialModel> syncedMaterials = syncMaterials(capturedMaterialsById, padherderMaterialsById);
		final List<SyncedMonsterModel> syncedMonsters = syncMonsters(capturedMonstersByBaseId, padherderMonstersByBaseId);

		result.setSyncedUserInfo(syncedUserInfo);
		result.setSyncedMaterials(syncedMaterials);
		result.setSyncedMonsters(syncedMonsters);

		result.setHasEncounteredUnknownMonster(hasEncounteredUnknownMonster);

		MyLog.exit();
		return result;
	}

	@SuppressLint("UseSparseArrays")
	private Map<Integer, List<MonsterModel>> reorgCapturedMonster(List<MonsterModel> capturedMonsters) {
		MyLog.entry();

		final Map<Integer, List<MonsterModel>> capturedMonstersById = new HashMap<Integer, List<MonsterModel>>();

		for (final MonsterModel capturedMonster : capturedMonsters) {
			final int capturedId = capturedMonster.getIdJp();
			MyLog.debug(" - capturedId = " + capturedId);
			try {
				final MonsterInfoModel monsterInfo = monsterInfoHelper.getMonsterInfo(capturedId);

				if (!capturedMonstersById.containsKey(capturedId)) {
					capturedMonstersById.put(capturedId, new ArrayList<MonsterModel>());
				}

				capMonsterData(monsterInfo, capturedMonster);
				capturedMonstersById.get(capturedId).add(capturedMonster);
			} catch (UnknownMonsterException e) {
				MyLog.warn("missing monster info for capturedId = " + e.getMonsterId());
				hasEncounteredUnknownMonster = true;
			}
		}

		// Sort monsters DESC
		final Comparator<MonsterModel> comparatorDesc = Collections.reverseOrder(new MonsterComparator(monsterInfoHelper));
		for (final List<MonsterModel> capturedMonstersList : capturedMonstersById.values()) {
			Collections.sort(capturedMonstersList, comparatorDesc);
		}

		MyLog.exit();
		return capturedMonstersById;
	}

	private void capMonsterData(MonsterInfoModel monsterInfo, MonsterModel capturedMonster) {
		// PAD allows to exp a monster beyond its max, but PADHerder caps to the max exp
		final MonsterExpHelper expHelper = new MonsterExpHelper(monsterInfo);
		final long expCap = expHelper.getExpCap();
		if (capturedMonster.getExp() > expCap) {
			MyLog.debug("capping monster xp for " + capturedMonster);
			capturedMonster.setExp(expCap);
		}

		// PAD allows to awaken a monster beyond its max, but PADHerder caps to the max awakenings
		final int maxAwakenings = monsterInfo.getAwokenSkillIds() == null ? 0 : monsterInfo.getAwokenSkillIds().size();
		if (capturedMonster.getAwakenings() > maxAwakenings) {
			MyLog.debug("capping monster awakenings for " + capturedMonster);
			capturedMonster.setAwakenings(maxAwakenings);
		}

		// TODO cap skillups and plusses ?
	}

	@SuppressLint("UseSparseArrays")
	private Map<Integer, Integer> reorgCapturedMaterials(List<MonsterModel> capturedMonsters) {
		MyLog.entry();

		final Map<Integer, Integer> capturedMaterialsById = new HashMap<Integer, Integer>();

		for (final MonsterModel capturedMonster : capturedMonsters) {
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
				MyLog.warn("missing material info for capturedId = " + e.getMonsterId());
				hasEncounteredUnknownMonster = true;
			}
		}

		MyLog.exit();
		return capturedMaterialsById;
	}

	@SuppressLint("UseSparseArrays")
	private Map<Integer, List<UserInfoMonsterModel>> reorgPadherderMonster(List<UserInfoMonsterModel> monsters) {
		MyLog.entry();

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
				MyLog.warn("missing monster info for padherderId = " + e.getMonsterId());
				hasEncounteredUnknownMonster = true;
			}
		}

		// Sort monsters DESC
		final Comparator<MonsterModel> comparatorDesc = Collections.reverseOrder(new MonsterComparator(monsterInfoHelper));
		for (final List<UserInfoMonsterModel> capturedMonstersList : padherderMonstersById.values()) {
			Collections.sort(capturedMonstersList, comparatorDesc);
		}

		MyLog.exit();
		return padherderMonstersById;
	}

	@SuppressLint("UseSparseArrays")
	private Map<Integer, UserInfoMaterialModel> reorgPadherderMaterials(List<UserInfoMaterialModel> materials) {
		MyLog.entry();

		final Map<Integer, UserInfoMaterialModel> padherderMaterialsById = new HashMap<Integer, UserInfoMaterialModel>();

		for (final UserInfoMaterialModel material : materials) {
			final int padherderId = material.getId();
			try {
				// Get monster info to check if the captureId exists
				monsterInfoHelper.getMonsterInfo(padherderId);
				padherderMaterialsById.put(material.getId(), material);
			} catch (UnknownMonsterException e) {
				MyLog.warn("missing monster info for padherderId = " + e.getMonsterId());
				hasEncounteredUnknownMonster = true;
			}
		}

		MyLog.exit();
		return padherderMaterialsById;
	}

	private void filterMaterials(Map<Integer, UserInfoMaterialModel> padherderMaterialsById,
			Map<Integer, List<UserInfoMonsterModel>> padherderMonstersById, Map<Integer, Integer> capturedMaterialsById,
			Map<Integer, List<MonsterModel>> capturedMonstersById) {
		MyLog.entry();

		final SyncMaterialInMonster syncMaterialInMonster = helper.getSyncMaterialInMonster();
		final boolean syncDeductMonsterInMaterial = helper.isSyncDeductMonsterInMaterial();

		if (syncMaterialInMonster != SyncMaterialInMonster.ALWAYS || syncDeductMonsterInMaterial) {
			for (final Integer materialId : padherderMaterialsById.keySet()) {
				if (capturedMonstersById.containsKey(materialId)) {
					MyLog.debug("- " + materialId);
					if (syncMaterialInMonster == SyncMaterialInMonster.NEVER) {
						MyLog.debug("removing all from captured monsters");
						capturedMonstersById.remove(materialId);
					} else if (syncMaterialInMonster == SyncMaterialInMonster.ONLY_IF_ALREADY_IN_PADHERDER) {
						if (padherderMonstersById.containsKey(materialId)) {
							final int numberToKeep = padherderMonstersById.get(materialId).size();
							final int numberToRemove = capturedMonstersById.get(materialId).size() - numberToKeep;
							MyLog.debug("in padherder's monsters, removing " + numberToRemove + " from captured monsters");

							final List<MonsterModel> capturedMonsterList = capturedMonstersById.get(materialId);
							// Start an iterator at the end of the list, to remove the lowest monsters first
							final ListIterator<MonsterModel> iter = capturedMonsterList.listIterator(capturedMonsterList.size());
							int i = 0;
							while (iter.hasPrevious() && i < numberToRemove) {
								iter.previous();
								iter.remove();
								i++;
							}
						} else {
							MyLog.debug("not in padherder's monsters, removing all from captured monsters");
							capturedMonstersById.remove(materialId);
						}
					} else {
						MyLog.debug("not removing from monsters");
					}

					// Same if than above, but a remove can occur, so we need to check again
					if (syncDeductMonsterInMaterial) {
						if (padherderMonstersById.containsKey(materialId)) {
							final int nbAlreadyMonsters = padherderMonstersById.get(materialId).size();
							MyLog.debug("in capturedMonster, reducing captured quantity by " + nbAlreadyMonsters);
							final int old = capturedMaterialsById.get(materialId);
							capturedMaterialsById.put(materialId, old - nbAlreadyMonsters);
						} else {
							MyLog.debug("not in capturedMonster, nothing to do");
						}
					}
				}
			}
		} else {
			MyLog.debug("nothing to filter");
		}
		MyLog.exit();
	}

	private SyncedUserInfoModel syncRank(CapturedPlayerInfoModel capturedInfo, UserInfoModel padInfo) {
		MyLog.entry();

		final SyncedUserInfoModel syncedPlayerInfo = new SyncedUserInfoModel();
		syncedPlayerInfo.setCapturedInfo(capturedInfo.getRank());
		syncedPlayerInfo.setPadherderInfo(padInfo.getRank());
		syncedPlayerInfo.setProfileApiId(padInfo.getProfileApiId());

		MyLog.exit();
		return syncedPlayerInfo;
	}

	private List<SyncedMaterialModel> syncMaterials(Map<Integer, Integer> capturedMaterialsById,
			Map<Integer, UserInfoMaterialModel> padherderMaterialsById) {
		MyLog.entry();

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

				MyLog.debug(" - " + syncedMaterial);
				syncedMaterials.add(syncedMaterial);
			} catch (UnknownMonsterException e) {
				// Should not happen as materials are checked before
				MyLog.warn("missing material info for materialId = " + e.getMonsterId());
				hasEncounteredUnknownMonster = true;
			}
		}

		MyLog.exit();
		return syncedMaterials;
	}

	private <T extends MonsterModel> Map<Integer, List<T>> reorgMonstersByBaseId(Map<Integer, List<T>> capturedMonstersById) {
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
				MyLog.warn("missing monster info for id = " + e.getMonsterId());
				hasEncounteredUnknownMonster = true;
			}
		}
		final Comparator<MonsterModel> comparatorDesc = Collections.reverseOrder(new MonsterComparator(monsterInfoHelper));
		for (final Integer id : capturedMonstersByBaseId.keySet()) {
			Collections.sort(capturedMonstersByBaseId.get(id), comparatorDesc);
		}

		return capturedMonstersByBaseId;
	}

	private List<SyncedMonsterModel> syncMonsters(Map<Integer, List<MonsterModel>> capturedMonstersById,
			Map<Integer, List<UserInfoMonsterModel>> padherderMonstersById) {
		MyLog.entry();

		final List<SyncedMonsterModel> syncedMonsters = new ArrayList<SyncedMonsterModel>();
		final Set<Integer> monsterIds = new HashSet<Integer>();
		monsterIds.addAll(capturedMonstersById.keySet());
		monsterIds.addAll(padherderMonstersById.keySet());

		for (final Integer monsterId : monsterIds) {
			final List<MonsterModel> capturedMonsters = capturedMonstersById.get(monsterId);
			final List<UserInfoMonsterModel> padherderMonsters = padherderMonstersById.get(monsterId);

			final List<SyncedMonsterModel> syncedMonstersForId = syncMonstersForOneId(monsterId, capturedMonsters, padherderMonsters);

			syncedMonsters.addAll(syncedMonstersForId);
		}

		MyLog.exit();
		return syncedMonsters;
	}

	private List<SyncedMonsterModel> syncMonstersForOneId(Integer monsterId, List<MonsterModel> capturedMonsters,
			List<UserInfoMonsterModel> padherderMonsters) {
		MyLog.entry("monsterId = " + monsterId);
		final List<SyncedMonsterModel> syncedMonsters = new ArrayList<SyncedMonsterModel>();

		MyLog.debug("capturedMonsters = " + capturedMonsters);
		MyLog.debug("padherderMonsters = " + padherderMonsters);

		final List<MonsterModel> capturedMonstersWork = new ArrayList<MonsterModel>();
		if (capturedMonsters != null) {
			capturedMonstersWork.addAll(capturedMonsters);
		}
		final List<UserInfoMonsterModel> padherderMonstersWork = new ArrayList<UserInfoMonsterModel>();
		if (padherderMonsters != null) {
			padherderMonstersWork.addAll(padherderMonsters);
		}

		final Iterator<MonsterModel> capturedIter = capturedMonstersWork.iterator();

		// Matching monsters on CARD_ID
		capturedLoop:
		while (capturedIter.hasNext()) {
			final MonsterModel captured = capturedIter.next();
			if (captured.getCardId() != null) {
				final Iterator<UserInfoMonsterModel> parherderIter = padherderMonstersWork.iterator();
				while (parherderIter.hasNext()) {
					final UserInfoMonsterModel padherder = parherderIter.next();
					if (captured.getCardId().equals(padherder.getCardId())) {
						if (MonsterComparatorHelper.areMonstersEqual(captured, padherder)) {
							MyLog.debug("matching ids and equal, removing : " + captured);
							capturedIter.remove();
							parherderIter.remove();
							continue capturedLoop;
						} else {
							try {
								final SyncedMonsterModel model = fillUpdatedSyncedMonsterModel(padherder, captured);
								syncedMonsters.add(model);
								MyLog.debug("matching ids and different, updating : " + model);
							} catch (UnknownMonsterException e) {
								// Should not happen as monsters are checked before during the reorg phase
								MyLog.warn("missing monster info for id = " + e.getMonsterId());
								hasEncounteredUnknownMonster = true;
							}
							capturedIter.remove();
							parherderIter.remove();
							continue capturedLoop;
						}
					}
				}
			}
		}

		// Filter "equals" monsters
		capturedLoop:
		while (capturedIter.hasNext()) {
			final MonsterModel captured = capturedIter.next();
			final Iterator<UserInfoMonsterModel> parherderIter = padherderMonstersWork.iterator();
			while (parherderIter.hasNext()) {
				final UserInfoMonsterModel padherder = parherderIter.next();
				if (MonsterComparatorHelper.areMonstersEqual(captured, padherder)) {
					MyLog.debug("equal, removing : " + captured);
					capturedIter.remove();
					parherderIter.remove();
					continue capturedLoop;
				}
			}
		}

		final int nbCaptured = capturedMonstersWork.size();
		final int nbPadherder = padherderMonstersWork.size();

		for (int i = 0; i < Math.max(nbCaptured, nbPadherder); i++) {
			final SyncedMonsterModel model;
			try {
				if (i < nbCaptured && i < nbPadherder) {
					// Update while we have enough monsters in each list
					model = fillUpdatedSyncedMonsterModel(padherderMonstersWork.get(i), capturedMonstersWork.get(i));
					MyLog.debug("updated : " + model);
				} else if (i < nbCaptured) {
					// more captured -> creating
					model = fillCreatedSyncedMonsterModel(capturedMonstersWork.get(i));
					MyLog.debug("added : " + model);
				} else {
					// not enough captured -> deleting
					model = fillDeletedSyncedMonsterModel(padherderMonstersWork.get(i));
					MyLog.debug("deleted : " + model);
				}

				syncedMonsters.add(model);
			} catch (UnknownMonsterException e) {
				// Should not happen as monsters are checked before during the reorg phase
				MyLog.warn("missing monster info for id = " + e.getMonsterId());
				hasEncounteredUnknownMonster = true;
			}
		}

		MyLog.exit();
		return syncedMonsters;
	}

	@NonNull
	private SyncedMonsterModel fillUpdatedSyncedMonsterModel(UserInfoMonsterModel padherder, MonsterModel captured) throws UnknownMonsterException {
		final SyncedMonsterModel model = new SyncedMonsterModel();
		model.setPadherderId(padherder.getPadherderId());
		model.setPadherderMonsterInfo(monsterInfoHelper.getMonsterInfo(padherder.getIdJp()));
		model.setCapturedMonsterInfo(monsterInfoHelper.getMonsterInfo(captured.getIdJp()));

		// Keep priority and note
		captured.setPriority(padherder.getPriority());
		captured.setNote(padherder.getNote());

		model.setCapturedInfo(captured);
		model.setPadherderInfo(padherder);
		return model;
	}

	@NonNull
	private SyncedMonsterModel fillCreatedSyncedMonsterModel(MonsterModel captured) throws UnknownMonsterException {
		final SyncedMonsterModel model = new SyncedMonsterModel();
		model.setCapturedMonsterInfo(monsterInfoHelper.getMonsterInfo(captured.getIdJp()));

		// default create priority
		captured.setPriority(helper.getDefaultMonsterCreatePriority());
		model.setCapturedInfo(captured);

		model.setPadherderInfo(null);
		return model;
	}


	@NonNull
	private SyncedMonsterModel fillDeletedSyncedMonsterModel(UserInfoMonsterModel padherder) throws UnknownMonsterException {
		final SyncedMonsterModel model = new SyncedMonsterModel();
		model.setPadherderMonsterInfo(monsterInfoHelper.getMonsterInfo(padherder.getIdJp()));

		model.setPadherderId(padherder.getPadherderId());
		model.setCapturedInfo(null);
		model.setPadherderInfo(padherder);
		return model;
	}
}
