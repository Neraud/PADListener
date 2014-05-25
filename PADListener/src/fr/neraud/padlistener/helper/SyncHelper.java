
package fr.neraud.padlistener.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.util.Log;
import fr.neraud.padlistener.helper.MonsterComparator.MonsterComparisonResult;
import fr.neraud.padlistener.model.CapturedMonsterCardModel;
import fr.neraud.padlistener.model.CapturedPlayerInfoModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.model.SyncComputeResultModel;
import fr.neraud.padlistener.model.SyncedMaterialModel;
import fr.neraud.padlistener.model.SyncedMonsterModel;
import fr.neraud.padlistener.model.UserInfoMaterialModel;
import fr.neraud.padlistener.model.UserInfoModel;
import fr.neraud.padlistener.model.UserInfoMonsterModel;

public class SyncHelper {

	private final Map<Integer, MonsterInfoModel> monsterInfoById;

	public SyncHelper(List<MonsterInfoModel> monsterInfos) {
		super();
		this.monsterInfoById = reorgMonsterInfo(monsterInfos);
	}

	public SyncComputeResultModel sync(List<CapturedMonsterCardModel> capturedMonsters, CapturedPlayerInfoModel capturedInfo,
	        UserInfoModel padInfo) {
		Log.d(getClass().getName(), "sync");

		final Map<Integer, List<CapturedMonsterCardModel>> capturedMonstersById = reorgCapturedMonster(capturedMonsters);
		final Map<Integer, Integer> capturedMaterialsById = reorgCapturedMaterials(capturedMonsters);

		final Map<Integer, List<UserInfoMonsterModel>> padherderMonstersById = reorgPadherderMonster(padInfo.getMonsters());
		final Map<Integer, UserInfoMaterialModel> padherderMaterialsById = reorgPadherderMaterials(padInfo.getMaterials());

		final SyncComputeResultModel result = new SyncComputeResultModel();

		final List<SyncedMaterialModel> syncedMaterials = syncMaterials(capturedMaterialsById, padherderMaterialsById);
		final List<SyncedMonsterModel> syncedMonsters = syncMonsters(capturedMonstersById, padherderMonstersById);

		result.setSyncedMaterials(syncedMaterials);
		result.setSyncedMonsters(syncedMonsters);

		return result;
	}

	@SuppressLint("UseSparseArrays")
	private Map<Integer, MonsterInfoModel> reorgMonsterInfo(List<MonsterInfoModel> monsterInfos) {
		Log.d(getClass().getName(), "reorgMonsterInfo");
		final Map<Integer, MonsterInfoModel> monsterInfoById = new HashMap<Integer, MonsterInfoModel>();

		for (final MonsterInfoModel monsterInfo : monsterInfos) {
			monsterInfoById.put(monsterInfo.getId(), monsterInfo);
		}

		return monsterInfoById;
	}

	@SuppressLint("UseSparseArrays")
	private Map<Integer, List<CapturedMonsterCardModel>> reorgCapturedMonster(List<CapturedMonsterCardModel> capturedMonsters) {
		Log.d(getClass().getName(), "reorgCapturedMonster");
		final Map<Integer, List<CapturedMonsterCardModel>> capturedMonstersById = new HashMap<Integer, List<CapturedMonsterCardModel>>();

		for (final CapturedMonsterCardModel capturedMonster : capturedMonsters) {
			final MonsterInfoModel monterInfo = monsterInfoById.get(capturedMonster.getId());
			if (monterInfo.getType1().isMaterial()) {
				// TODO
				continue;
			}

			if (!capturedMonstersById.containsKey(capturedMonster.getId())) {
				capturedMonstersById.put(capturedMonster.getId(), new ArrayList<CapturedMonsterCardModel>());
			}
			capturedMonstersById.get(capturedMonster.getId()).add(capturedMonster);
		}

		return capturedMonstersById;
	}

	@SuppressLint("UseSparseArrays")
	private Map<Integer, Integer> reorgCapturedMaterials(List<CapturedMonsterCardModel> capturedMonsters) {
		Log.d(getClass().getName(), "reorgCapturedMaterials");
		final Map<Integer, Integer> capturedMaterialsById = new HashMap<Integer, Integer>();

		for (final CapturedMonsterCardModel capturedMonster : capturedMonsters) {
			final MonsterInfoModel monterInfo = monsterInfoById.get(capturedMonster.getId());
			if (!monterInfo.getType1().isMaterial()) {
				// TODO
				continue;
			}

			Integer count = capturedMaterialsById.get(capturedMonster.getId());
			if (count == null) {
				count = 0;
			}
			capturedMaterialsById.put(capturedMonster.getId(), ++count);
		}

		return capturedMaterialsById;
	}

	@SuppressLint("UseSparseArrays")
	private Map<Integer, List<UserInfoMonsterModel>> reorgPadherderMonster(List<UserInfoMonsterModel> monsters) {
		Log.d(getClass().getName(), "reorgPadherderMonster");

		final Map<Integer, List<UserInfoMonsterModel>> padherderMonstersById = new HashMap<Integer, List<UserInfoMonsterModel>>();

		for (final UserInfoMonsterModel monster : monsters) {
			if (!padherderMonstersById.containsKey(monster.getId())) {
				padherderMonstersById.put(monster.getId(), new ArrayList<UserInfoMonsterModel>());
			}
			padherderMonstersById.get(monster.getId()).add(monster);
		}

		return padherderMonstersById;
	}

	@SuppressLint("UseSparseArrays")
	private Map<Integer, UserInfoMaterialModel> reorgPadherderMaterials(List<UserInfoMaterialModel> materials) {
		Log.d(getClass().getName(), "reorgPadherderMonster");
		final Map<Integer, UserInfoMaterialModel> padherderMaterialsById = new HashMap<Integer, UserInfoMaterialModel>();

		for (final UserInfoMaterialModel material : materials) {
			padherderMaterialsById.put(material.getId(), material);
		}

		return padherderMaterialsById;
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

			final SyncedMaterialModel syncedMaterial = new SyncedMaterialModel();
			final MonsterInfoModel monsterInfo = monsterInfoById.get(materialId);
			syncedMaterial.setMonsterInfo(monsterInfo);
			syncedMaterial.setPadherderId(padherderMaterial.getPadherderId());
			syncedMaterial.setCapturedInfo(capturedMaterialsById.containsKey(materialId) ? capturedMaterialsById.get(materialId)
			        : 0);
			syncedMaterial.setPadherderInfo(padherderMaterial.getQuantity());

			Log.d(getClass().getName(), " - syncedMaterial : " + syncedMaterial);
			syncedMaterials.add(syncedMaterial);
		}

		return syncedMaterials;
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

			final List<SyncedMonsterModel> syncedMonstersForId = syncMonstersForOneId(monsterId, capturedMonsters,
			        padherderMonsters);

			syncedMonsters.addAll(syncedMonstersForId);
		}

		return syncedMonsters;
	}

	private List<SyncedMonsterModel> syncMonstersForOneId(Integer monsterId, List<CapturedMonsterCardModel> capturedMonsters,
	        List<UserInfoMonsterModel> padherderMonsters) {
		Log.d(getClass().getName(), "syncMonstersForOneId : " + monsterId);
		final List<SyncedMonsterModel> syncedMonsters = new ArrayList<SyncedMonsterModel>();

		final List<CapturedMonsterCardModel> capturedMonstersWork = new ArrayList<CapturedMonsterCardModel>();
		if (capturedMonsters != null) {
			capturedMonstersWork.addAll(capturedMonsters);
		}
		final List<UserInfoMonsterModel> padherderMonstersWork = new ArrayList<UserInfoMonsterModel>();
		if (padherderMonsters != null) {
			padherderMonstersWork.addAll(padherderMonsters);
		}

		final Iterator<CapturedMonsterCardModel> capturedIter = capturedMonstersWork.iterator();
		final Iterator<UserInfoMonsterModel> parherderIter = padherderMonstersWork.iterator();

		// Filter "equals" monsters
		capturedLoop: while (capturedIter.hasNext()) {
			final CapturedMonsterCardModel captured = capturedIter.next();
			while (parherderIter.hasNext()) {
				final UserInfoMonsterModel padherder = parherderIter.next();
				if (MonsterComparator.compareMonsters(captured, padherder) == MonsterComparisonResult.EQUALS) {
					capturedIter.remove();
					parherderIter.remove();
					continue capturedLoop;
				}
			}
		}

		// TODO update

		// New monsters
		for (final CapturedMonsterCardModel captured : capturedMonstersWork) {
			final SyncedMonsterModel model = new SyncedMonsterModel();
			model.setMonsterInfo(monsterInfoById.get(monsterId));
			model.setCapturedInfo(captured);
			model.setPadherderInfo(null);
			Log.d(getClass().getName(), "syncMonstersForOneId : added : " + model);
			syncedMonsters.add(model);
		}

		// Deleted monsters
		for (final UserInfoMonsterModel padherder : padherderMonstersWork) {
			final SyncedMonsterModel model = new SyncedMonsterModel();
			model.setMonsterInfo(monsterInfoById.get(monsterId));
			model.setPadherderId(padherder.getPadherderId());
			model.setCapturedInfo(null);
			model.setPadherderInfo(padherder);
			Log.d(getClass().getName(), "syncMonstersForOneId : deleted : " + model);
			syncedMonsters.add(model);
		}

		return syncedMonsters;
	}

}
