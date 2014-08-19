package fr.neraud.padlistener.service.helper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;
import fr.neraud.padlistener.provider.helper.MonsterInfoProviderHelper;

/**
 * Created by Neraud on 15/08/2014.
 */
public class UpdateMonsterInfoHelper {

	private Context context;

	public UpdateMonsterInfoHelper(Context context) {
		this.context = context;
	}

	public int mergeAndSaveMonsterInfo(List<MonsterInfoModel> monsters, Map<Integer, Integer> evolutions) {
		fillEvolutionData(monsters, evolutions);
		return saveData(monsters);
	}

	private void fillEvolutionData(List<MonsterInfoModel> monsters, Map<Integer, Integer> evolutions) {
		Log.d(getClass().getName(), "fillEvolutionData");
		final Map<Integer, MonsterInfoModel> monsterInfoById = new HashMap<Integer, MonsterInfoModel>();
		for (final MonsterInfoModel monster : monsters) {
			monsterInfoById.put(monster.getIdJP(), monster);
		}
		for (final MonsterInfoModel monster : monsters) {
			int evolutionStage = 1;
			int baseMonsterId = monster.getIdJP();
			while (evolutions.containsKey(baseMonsterId)) {
				baseMonsterId = evolutions.get(baseMonsterId);
				evolutionStage++;
			}
			Log.d(getClass().getName(), "fillEvolutionData : " + monster + " -> " + monsterInfoById.get(baseMonsterId) + " (stage = " + evolutionStage + ")");
			monster.setBaseMonsterId(baseMonsterId);
			monster.setEvolutionStage(evolutionStage);
		}
	}

	private int saveData(List<MonsterInfoModel> monsters) {
		Log.d(getClass().getName(), "saveData");
		final ContentResolver cr = context.getContentResolver();
		final Uri uri = MonsterInfoDescriptor.UriHelper.uriForAll();

		cr.delete(uri, null, null);
		final ContentValues[] values = new ContentValues[monsters.size()];
		int i = 0;
		for (final MonsterInfoModel monster : monsters) {
			values[i] = MonsterInfoProviderHelper.modelToValues(monster);
			i++;
		}
		return cr.bulkInsert(uri, values);
	}
}
