package fr.neraud.padlistener.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.constant.PADRegion;
import fr.neraud.padlistener.exception.UnknownMonsterException;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;
import fr.neraud.padlistener.provider.helper.MonsterInfoProviderHelper;

/**
 * Helper to convert the monster ids from the player region to JP ids
 * Created by Neraud on 16/08/2014.
 */
public class MonsterIdConverterHelper {

	private final Context context;
	private final PADRegion region;
	private final Map<Integer, Integer> monsterIdInCapturedRegionToRef;

	public MonsterIdConverterHelper(Context context, PADRegion region) {
		this.context = context;
		this.region = region;
		monsterIdInCapturedRegionToRef = initMonsterIdInCapturedRegionToRef();
	}

	@SuppressLint("UseSparseArrays")
	private Map<Integer, Integer> initMonsterIdInCapturedRegionToRef() {
		MyLog.entry();

		List<MonsterInfoModel> monsterInfos = extractMonsterInfo();

		final Map<Integer, Integer> monsterIdInCapturedRegionToRef = new HashMap<Integer, Integer>();

		for (final MonsterInfoModel monsterInfo : monsterInfos) {
			if (monsterInfo.getId(region) != null) {
				monsterIdInCapturedRegionToRef.put(monsterInfo.getId(region), monsterInfo.getIdJP());
			}
		}

		MyLog.exit();
		return monsterIdInCapturedRegionToRef;
	}

	private List<MonsterInfoModel> extractMonsterInfo() {
		MyLog.entry();

		final Uri uri = MonsterInfoDescriptor.UriHelper.uriForAll();
		final Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

		final List<MonsterInfoModel> monsterInfos = new ArrayList<MonsterInfoModel>();

		if (cursor.moveToFirst()) {
			do {
				final MonsterInfoModel model = MonsterInfoProviderHelper.cursorToModel(cursor);
				monsterInfos.add(model);
			} while (cursor.moveToNext());
		}
		cursor.close();

		MyLog.exit();
		return monsterInfos;
	}

	public int getMonsterRefIdByCapturedId(int capturedId) throws UnknownMonsterException {
		if (monsterIdInCapturedRegionToRef.containsKey(capturedId)) {
			return monsterIdInCapturedRegionToRef.get(capturedId);
		} else {
			throw new UnknownMonsterException(capturedId);
		}
	}
}
