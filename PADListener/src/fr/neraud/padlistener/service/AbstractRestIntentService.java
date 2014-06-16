
package fr.neraud.padlistener.service;

import java.io.Serializable;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import fr.neraud.padlistener.http.client.RestClient;
import fr.neraud.padlistener.http.exception.HttpCallException;
import fr.neraud.padlistener.http.exception.HttpResponseException;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.http.exception.ProcessException;
import fr.neraud.padlistener.http.model.MyHttpRequest;
import fr.neraud.padlistener.http.model.RestResponse;
import fr.neraud.padlistener.service.constant.RestCallError;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;
import fr.neraud.padlistener.service.constant.RestCallState;
import fr.neraud.padlistener.service.receiver.AbstractRestResultReceiver;

/**
 * Base IntentService used for Rest calls
 * 
 * @author Neraud
 * @param <R>
 * @param <M>
 */
public abstract class AbstractRestIntentService<R, M extends Serializable> extends IntentService {

	private ResultReceiver receiver;

	public AbstractRestIntentService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(getClass().getName(), "onHandleIntent");
		initParams(intent);
		receiver = intent.getParcelableExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME);
		notifyProgress(RestCallRunningStep.STARTED);
		try {
			final RestResponse restResponse = callRestApi();
			notifyProgress(RestCallRunningStep.RESPONSE_RECEIVED);
			if (restResponse.isResponseOk()) {
				final R result = parseResult(restResponse.getContentResult());

				notifyProgress(RestCallRunningStep.RESPONSE_PARSED);
				final M resultModel = processResult(result);
				notifyResult(resultModel);
			} else {
				throw new HttpResponseException(restResponse.getStatus(), "Code " + restResponse.getStatus()
				        + " received with content : " + restResponse.getContentResult());
			}
		} catch (final HttpCallException e) {
			Log.e(getClass().getName(), "onHandleIntent : HttpCallException " + e.getMessage(), e);
			notifyError(RestCallError.REST_CALL_ERROR, e);
		} catch (final HttpResponseException e) {
			Log.e(getClass().getName(), "onHandleIntent : HttpResponseException " + e.getMessage(), e);
			notifyError(RestCallError.RESPONSE_ERROR, e);
		} catch (final ParsingException e) {
			Log.e(getClass().getName(), "onHandleIntent : ParsingException " + e.getMessage(), e);
			notifyError(RestCallError.PARSING_ERROR, e);
		} catch (final ProcessException e) {
			Log.e(getClass().getName(), "onHandleIntent : ProcessException " + e.getMessage(), e);
			notifyError(RestCallError.PROCESS_ERROR, e);
		}
	}

	private RestResponse callRestApi() throws HttpCallException {
		final RestClient restClient = createRestClient();
		final MyHttpRequest restRequest = createMyHttpRequest();
		final RestResponse restResponse = restClient.call(restRequest);
		return restResponse;
	}

	protected void initParams(Intent intent) {
		// Override if necessary
	}

	protected abstract RestClient createRestClient();

	protected abstract MyHttpRequest createMyHttpRequest();

	protected abstract R parseResult(final String responseContent) throws ParsingException;

	protected abstract M processResult(R result) throws ProcessException;

	protected void notifyProgress(RestCallRunningStep step) {
		Log.d(getClass().getName(), "notifyProgress : " + step);
		if (receiver != null) {
			final Bundle bundle = new Bundle();
			bundle.putString(AbstractRestResultReceiver.RECEIVER_BUNDLE_STEP_NAME, step.name());
			receiver.send(RestCallState.RUNNING.getCode(), bundle);
		} else {
			Log.w(getClass().getName(), "processResult : no ResultReceiver available !");
		}
	}

	protected void notifyResult(M result) {
		Log.d(getClass().getName(), "notifyResult");
		if (receiver != null) {
			final Bundle bundle = new Bundle();
			bundle.putSerializable(AbstractRestResultReceiver.RECEIVER_BUNDLE_RESULT_NAME, result);
			receiver.send(RestCallState.SUCCESSED.getCode(), bundle);
		} else {
			Log.w(getClass().getName(), "processResult : no ResultReceiver available !");
		}
	}

	protected void notifyError(RestCallError error, Throwable t) {
		Log.d(getClass().getName(), "notifyError : " + error);
		if (receiver != null) {
			final Bundle bundle = new Bundle();
			bundle.putString(AbstractRestResultReceiver.RECEIVER_BUNDLE_ERROR_NAME, error.name());
			bundle.putSerializable(AbstractRestResultReceiver.RECEIVER_BUNDLE_ERROR_CAUSE, t);
			receiver.send(RestCallState.FAILED.getCode(), bundle);
		} else {
			Log.w(getClass().getName(), "processResult : no ResultReceiver available !");
		}
	}

	protected ResultReceiver getReceiver() {
		return receiver;
	}

}
