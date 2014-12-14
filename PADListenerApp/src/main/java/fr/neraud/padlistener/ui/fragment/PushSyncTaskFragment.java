package fr.neraud.padlistener.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.constant.SyncMode;
import fr.neraud.padlistener.model.ChooseModelContainer;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.PushSyncStatModel;
import fr.neraud.padlistener.model.PushSyncStatModel.ElementToPush;
import fr.neraud.padlistener.service.PushSyncService;

/**
 * PushSync retained fragment to store the sync push progression
 *
 * @author Neraud
 */
public class PushSyncTaskFragment extends Fragment {

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
			MyLog.entry();

			final ElementToPush element = ElementToPush.valueOf(resultData.getString(PushSyncService.RECEIVER_ELEMENT_NAME));
			final boolean isSuccess = resultData.getBoolean(PushSyncService.RECEIVER_SUCCESS_NAME);
			final String errorMessage = resultData.getString(PushSyncService.RECEIVER_MESSAGE_NAME);

			if (isSuccess) {
				pushModel.incrementElementsPushed(element);
			} else {
				pushModel.incrementElementsError(element, errorMessage);
			}
			notifyCallBacks();

			MyLog.exit();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.entry();

		super.onCreate(savedInstanceState);

		setRetainInstance(true);

		pushModel = initModel();

		notifyCallBacks();

		final Intent intent = new Intent(getActivity(), PushSyncService.class);
		intent.putExtra(PushSyncService.CHOOSE_SYNC_MODEL_EXTRA_NAME, result);
		intent.putExtra(PushSyncService.ACCOUNT_ID_EXTRA_NAME, accountId);
		intent.putExtra(PushSyncService.RECEIVER_EXTRA_NAME, new MyResultReceiver(new Handler()));
		getActivity().startService(intent);

		MyLog.exit();
	}

	@Override
	public void onDetach() {
		MyLog.entry();
		super.onDetach();
		callbacks = null;
		MyLog.exit();
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
		MyLog.entry();

		final PushSyncStatModel pushModel = new PushSyncStatModel();

		int count = 0;
		if (result.getSyncedUserInfoToUpdate().getModel().hasDataToSync() && result.getSyncedUserInfoToUpdate().isChosen()) {
			count++;
		}
		pushModel.initElementsToPush(ElementToPush.USER_INFO, count);

		count = 0;
		for (final ChooseModelContainer<?> element : result.getSyncedMaterialsToUpdate()) {
			if (element.isChosen()) {
				count++;
			}
		}
		pushModel.initElementsToPush(ElementToPush.MATERIAL_TO_UPDATE, count);

		count = 0;
		for (final ChooseModelContainer<?> element : result.getSyncedMonsters(SyncMode.UPDATED)) {
			if (element.isChosen()) {
				count++;
			}
		}
		pushModel.initElementsToPush(ElementToPush.MONSTER_TO_UPDATE, count);

		count = 0;
		for (final ChooseModelContainer<?> element : result.getSyncedMonsters(SyncMode.CREATED)) {
			if (element.isChosen()) {
				count++;
			}
		}
		pushModel.initElementsToPush(ElementToPush.MONSTER_TO_CREATE, count);

		count = 0;
		for (final ChooseModelContainer<?> element : result.getSyncedMonsters(SyncMode.DELETED)) {
			if (element.isChosen()) {
				count++;
			}
		}
		pushModel.initElementsToPush(ElementToPush.MONSTER_TO_DELETE, count);

		MyLog.exit();
		return pushModel;
	}

	public void setChooseSyncModel(ChooseSyncModel result) {
		this.result = result;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

}
