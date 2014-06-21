package fr.neraud.padlistener.gui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.util.Log;

import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.PushSyncStatModel;
import fr.neraud.padlistener.model.PushSyncStatModel.ElementToPush;
import fr.neraud.padlistener.service.PushSyncService;

/**
 * PushSync retained fragment to store the sync push progression
 *
 * @author Neraud
 */
public class PushSyncTaskFragment extends Fragment {

	protected static final String EXTRA_CHOOSE_SYNC_MODEL_NAME = "sync_model";

	private ChooseSyncModel result;
	private int accountId;
	private CallBacks callbacks = null;

	private PushSyncStatModel pushModel;

	/**
	 * Interface to implement to be notified when the sync computation progresses
	 *
	 * @author Neraud
	 */
	public static interface CallBacks {

		public void updateState(PushSyncStatModel pushModel);
	}

	private class MyResultReceiver extends ResultReceiver {

		public MyResultReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			Log.d(getClass().getName(), "onReceiveResult");

			final ElementToPush element = ElementToPush.valueOf(resultData.getString(PushSyncService.RECEIVER_ELEMENT_NAME));
			final boolean isSuccess = resultData.getBoolean(PushSyncService.RECEIVER_SUCCESS_NAME);
			final String errorMessage = resultData.getString(PushSyncService.RECEIVER_MESSAGE_NAME);

			if (isSuccess) {
				pushModel.incrementElementsPushed(element);
			} else {
				pushModel.incrementElementsError(element, errorMessage);
			}
			notifyCallBacks();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);

		setRetainInstance(true);

		pushModel = initModel();

		notifyCallBacks();

		final Intent intent = new Intent(getActivity(), PushSyncService.class);
		intent.putExtra(PushSyncService.CHOOSE_SYNC_MODEL_EXTRA_NAME, result);
		intent.putExtra(PushSyncService.ACCOUNT_ID_EXTRA_NAME, accountId);
		intent.putExtra(PushSyncService.RECEIVER_EXTRA_NAME, new MyResultReceiver(new Handler()));
		getActivity().startService(intent);
	}

	@Override
	public void onDetach() {
		Log.d(getClass().getName(), "onDetach");
		super.onDetach();
		callbacks = null;
	}

	public void registerCallbacks(CallBacks callbacks) {
		this.callbacks = callbacks;
		notifyCallBacks();
	}

	private void notifyCallBacks() {
		if (callbacks != null) {
			callbacks.updateState(pushModel);
		}
	}

	private PushSyncStatModel initModel() {
		Log.d(getClass().getName(), "initModel");
		final PushSyncStatModel pushModel = new PushSyncStatModel();

		int count = 0;
		if (result.getSyncedUserInfoToUpdate().isChoosen()) {
			count++;
		}
		pushModel.initElementsToPush(ElementToPush.USER_INFO, count);

		count = 0;
		for (final ChooseSyncModelContainer<?> element : result.getSyncedMaterialsToUpdate()) {
			if (element.isChoosen()) {
				count++;
			}
		}
		pushModel.initElementsToPush(ElementToPush.MATERIAL_TO_UPDATE, count);

		count = 0;
		for (final ChooseSyncModelContainer<?> element : result.getSyncedMonstersToUpdate()) {
			if (element.isChoosen()) {
				count++;
			}
		}
		pushModel.initElementsToPush(ElementToPush.MONSTER_TO_UPDATE, count);

		count = 0;
		for (final ChooseSyncModelContainer<?> element : result.getSyncedMonstersToCreate()) {
			if (element.isChoosen()) {
				count++;
			}
		}
		pushModel.initElementsToPush(ElementToPush.MONSTER_TO_CREATE, count);

		count = 0;
		for (final ChooseSyncModelContainer<?> element : result.getSyncedMonstersToDelete()) {
			if (element.isChoosen()) {
				count++;
			}
		}
		pushModel.initElementsToPush(ElementToPush.MONSTER_TO_DELETE, count);

		return pushModel;
	}

	public void setChooseSyncModel(ChooseSyncModel result) {
		this.result = result;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

}
