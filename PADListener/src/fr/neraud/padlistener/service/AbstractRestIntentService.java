
package fr.neraud.padlistener.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import fr.neraud.padlistener.http.RestClient;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.http.exception.ProcessException;
import fr.neraud.padlistener.http.exception.HttpCallException;
import fr.neraud.padlistener.http.model.RestRequest;
import fr.neraud.padlistener.http.model.RestResponse;
import fr.neraud.padlistener.service.constant.RestCallProgress;
import fr.neraud.padlistener.service.receiver.AbstractRestResultReceiver;

public abstract class AbstractRestIntentService<R> extends IntentService {

	public AbstractRestIntentService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(getClass().getName(), "onHandleIntent");

		final ResultReceiver receiver = intent.getParcelableExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME);
		notifyProgress(receiver, RestCallProgress.STARTED);
		try {
			final RestResponse restResponse = callRestApi();
			notifyProgress(receiver, RestCallProgress.RESPONSE_RECEIVED);
			if (restResponse.isResponseOk()) {
				final R result = parseResult(restResponse.getContentResult());

				notifyProgress(receiver, RestCallProgress.RESPONSE_PARSED);
				processResult(result);
				notifyProgress(receiver, RestCallProgress.DATA_PROCESSED);
			}
		} catch (final HttpCallException e) {
			Log.e(getClass().getName(), "onHandleIntent", e);
			notifyProgress(receiver, RestCallProgress.REST_CALL_ERROR);
		} catch (final ParsingException e) {
			Log.e(getClass().getName(), "onHandleIntent", e);
			notifyProgress(receiver, RestCallProgress.PARSING_ERROR);
		} catch (final ProcessException e) {
			Log.e(getClass().getName(), "onHandleIntent", e);
			notifyProgress(receiver, RestCallProgress.PROCESS_ERROR);
		}
	}

	private RestResponse callRestApi() throws HttpCallException {
		final RestClient restClient = createRestClient();
		final RestRequest restRequest = createRestRequest();
		final RestResponse restResponse = restClient.call(restRequest);
		return restResponse;
	}

	protected abstract RestClient createRestClient();

	protected abstract RestRequest createRestRequest();

	protected abstract R parseResult(final String responseContent) throws ParsingException;

	protected abstract void processResult(R result) throws ProcessException;

	protected void notifyProgress(ResultReceiver receiver, RestCallProgress progress) {
		Log.d(getClass().getName(), "notifyProgress : " + progress);
		if (receiver != null) {
			final Bundle bundle = new Bundle();
			bundle.putString("progress", progress.name());
			receiver.send(progress.getState().getCode(), bundle);
		}
	}

}
