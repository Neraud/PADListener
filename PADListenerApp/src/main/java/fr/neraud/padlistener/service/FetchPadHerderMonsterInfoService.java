package fr.neraud.padlistener.service;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.http.client.RestClient;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.http.exception.ProcessException;
import fr.neraud.padlistener.http.helper.PadHerderDescriptor;
import fr.neraud.padlistener.http.model.MyHttpRequest;
import fr.neraud.padlistener.http.parser.padherder.MonsterInfoJsonParser;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;
import fr.neraud.padlistener.provider.helper.MonsterInfoHelper;

/**
 * Service used to refresh MonsterInfo from PADherder
 *
 * @author Neraud
 */
public class FetchPadHerderMonsterInfoService extends AbstractRestIntentService<Integer> {

	private class FetchInfoTask extends RestTask<List<MonsterInfoModel>> {

		@Override
		protected RestClient createRestClient() {
			return new RestClient(getApplicationContext(), PadHerderDescriptor.serverUrl);
		}

		@Override
		protected MyHttpRequest createMyHttpRequest() {
			return PadHerderDescriptor.RequestHelper.initRequestForGetMonsterInfo();
		}

		@Override
		protected List<MonsterInfoModel> parseResult(String responseContent) throws ParsingException {
			Log.d(getClass().getName(), "parseResult");
			final MonsterInfoJsonParser parser = new MonsterInfoJsonParser();
			return parser.parse(responseContent);
		}
	}

	public FetchPadHerderMonsterInfoService() {
		super("FetchPadHerderMonsterInfoService");
	}

	protected List<RestTask<?>> createRestTasks() {
		final List<RestTask<?>> tasks = new ArrayList<RestTask<?>>();
		tasks.add(new FetchInfoTask());
		return tasks;
	}

	@Override
	protected Integer processResult(List results) throws ProcessException {
		Log.d(getClass().getName(), "processResult");
		final List<MonsterInfoModel> monsters = (List<MonsterInfoModel>) results.get(0);
		final ContentResolver cr = getContentResolver();
		final Uri uri = MonsterInfoDescriptor.UriHelper.uriForAll();

		cr.delete(uri, null, null);
		final ContentValues[] values = new ContentValues[monsters.size()];
		int i = 0;
		for (final MonsterInfoModel monster : monsters) {
			values[i] = MonsterInfoHelper.modelToValues(monster);
			i++;
		}
		final int count = cr.bulkInsert(uri, values);

		new TechnicalSharedPreferencesHelper(getApplicationContext()).setMonsterInfoRefreshDate(new Date());
		return count;
	}

}
