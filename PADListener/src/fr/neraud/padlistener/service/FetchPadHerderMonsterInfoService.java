
package fr.neraud.padlistener.service;

import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.ResultReceiver;
import android.util.Log;
import fr.neraud.padlistener.http.PadHerderDescriptor;
import fr.neraud.padlistener.http.RestClient;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.http.exception.ProcessException;
import fr.neraud.padlistener.http.model.RestRequest;
import fr.neraud.padlistener.http.parser.MonsterInfoJsonParser;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;
import fr.neraud.padlistener.provider.helper.MonsterInfoHelper;
import fr.neraud.padlistener.service.constant.RestCallProgress;
import fr.neraud.padlistener.service.receiver.AbstractRestResultReceiver;

public class FetchPadHerderMonsterInfoService extends AbstractRestIntentService<List<MonsterInfoModel>> {

	private final boolean MOCK = false;

	public FetchPadHerderMonsterInfoService() {
		super("FetchPadHerderMonsterInfoService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (MOCK) {
			try {
				final ResultReceiver receiver = intent.getParcelableExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME);
				Thread.sleep(1000);
				notifyProgress(receiver, RestCallProgress.STARTED);
				Thread.sleep(1000);
				notifyProgress(receiver, RestCallProgress.RESPONSE_RECEIVED);
				Thread.sleep(1000);
				notifyProgress(receiver, RestCallProgress.RESPONSE_PARSED);
				Thread.sleep(1000);
				notifyProgress(receiver, RestCallProgress.DATA_PROCESSED);
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			super.onHandleIntent(intent);
		}
	}

	@Override
	protected RestClient createRestClient() {
		return new RestClient(PadHerderDescriptor.serverUrl);
	}

	@Override
	protected RestRequest createRestRequest() {
		return PadHerderDescriptor.RequestHelper.initRequestForMonsterInfo();
	}

	@Override
	protected List<MonsterInfoModel> parseResult(String responseContent) throws ParsingException {
		Log.d(getClass().getName(), "parseResult");
		final MonsterInfoJsonParser parser = new MonsterInfoJsonParser();
		final List<MonsterInfoModel> monsters = parser.parse(responseContent);
		return monsters;
	}

	@Override
	protected void processResult(List<MonsterInfoModel> monsters) throws ProcessException {
		Log.d(getClass().getName(), "processResult");
		final ContentResolver cr = getContentResolver();
		final Uri uri = MonsterInfoDescriptor.UriHelper.uriForAll();

		cr.delete(uri, null, null);
		final ContentValues[] values = new ContentValues[monsters.size()];
		int i = 0;
		for (final MonsterInfoModel monster : monsters) {
			values[i] = MonsterInfoHelper.modelToValues(monster);
			i++;
		}
		cr.bulkInsert(uri, values);
	}

}
