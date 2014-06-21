package fr.neraud.padlistener.gui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.util.Log;

import fr.neraud.padlistener.service.FetchPadHerderMonsterImageService;
import fr.neraud.padlistener.service.constant.RestCallState;
import fr.neraud.padlistener.service.receiver.AbstractRestResultReceiver;

/**
 * ViewMonsterInfo retained fragment to store the RefreshImages progression
 *
 * @author Neraud
 */
public class ViewMonsterInfoRefreshImagesTaskFragment extends Fragment {

	private RestCallState state = null;
	private int imagesDownloaded = 0;
	private int imagesToDownload = 0;
	private int monsterIdDownloading = 0;
	private CallBacks callbacks = null;

	public static interface CallBacks {

		public void updateState(RestCallState state, int imagesDownloaded, int imagesToDownload, int monsterIdDownloading);
	}

	private class MyResultReceiver extends ResultReceiver {

		public MyResultReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			imagesDownloaded = resultData.getInt("imagesDownloaded");
			imagesToDownload = resultData.getInt("imagesToDownload");
			switch (resultCode) {
				case 0:
					//final String imageUrl = resultData.getString("imageUrl");
					monsterIdDownloading = resultData.getInt("monsterId");
					break;
				case 1:
					state = RestCallState.SUCCESSED;
					break;
				default:
					break;
			}
			notifyCallBacks();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
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
			callbacks.updateState(state, imagesDownloaded, imagesToDownload, monsterIdDownloading);
		}
	}

	public void startFetchImagesService() {
		Log.d(getClass().getName(), "startFetchImagesService");
		state = RestCallState.RUNNING;
		imagesDownloaded = 0;
		imagesToDownload = 0;
		monsterIdDownloading = 0;
		notifyCallBacks();

		final Intent startIntent = new Intent(getActivity(), FetchPadHerderMonsterImageService.class);
		startIntent.putExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME, new MyResultReceiver(new Handler()));
		getActivity().startService(startIntent);
	}

}
