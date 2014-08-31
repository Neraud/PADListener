package fr.neraud.padlistener.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
 */
public abstract class AbstractRestIntentService<M extends Serializable> extends IntentService {

	private ResultReceiver receiver;

	protected abstract class RestTask<R> {

		protected abstract RestClient createRestClient();

		protected abstract MyHttpRequest createMyHttpRequest();

		protected abstract R parseResult(final String responseContent) throws ParsingException;
	}

	public AbstractRestIntentService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(getClass().getName(), "onHandleIntent");
		initParams(intent);
		receiver = intent.getParcelableExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME);
		notifyProgress(RestCallRunningStep.STARTED);

		List<RestTask<?>> tasks = createRestTasks();

		try {
			final List<RestResponse> restResponse = callRestApi(tasks);
			notifyProgress(RestCallRunningStep.RESPONSE_RECEIVED);

			List<?> results = extractResults(tasks, restResponse);
			notifyProgress(RestCallRunningStep.RESPONSE_PARSED);

			final M resultModel = processResult(results);
			notifyResult(resultModel);
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

	protected abstract List<RestTask<?>> createRestTasks();

	private List<RestResponse> callRestApi(List<RestTask<?>> tasks) throws HttpCallException {
		Log.d(getClass().getName(), "callRestApi");
		final List<RestResponse> responses = new ArrayList<RestResponse>();

		for (final RestTask<?> task : tasks) {
			final RestClient restClient = task.createRestClient();
			final MyHttpRequest restRequest = task.createMyHttpRequest();
			final RestResponse response = restClient.call(restRequest);
			responses.add(response);
		}
		return responses;
	}

	@SuppressWarnings("unchecked")
	private List<?> extractResults(List<RestTask<?>> tasks, List<RestResponse> responses) throws ParsingException, HttpResponseException {
		Log.d(getClass().getName(), "extractResults");
		final List<Object> results = new ArrayList();

		for (int i = 0; i < tasks.size(); i++) {
			final RestTask task = tasks.get(i);
			final RestResponse restResponse = responses.get(i);

			if (restResponse.isResponseOk()) {
				final Object result = task.parseResult(restResponse.getContentResult());
				results.add(result);

			} else {
				throw new HttpResponseException(restResponse.getStatus(), "Code " + restResponse.getStatus()
						+ " received with content : " + restResponse.getContentResult());
			}
		}
		return results;
	}

	protected abstract M processResult(List<?> results) throws ProcessException;

	protected void initParams(Intent intent) {
		// Override if necessary
	}

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
			receiver.send(RestCallState.SUCCEEDED.getCode(), bundle);
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
