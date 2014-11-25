package fr.neraud.padlistener.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.PADVersion;
import fr.neraud.padlistener.exception.NoMatchingAccountException;
import fr.neraud.padlistener.service.AutoCaptureService;
import fr.neraud.padlistener.service.AutoSyncService;
import fr.neraud.padlistener.service.receiver.AbstractAutoCaptureReceiver;
import fr.neraud.padlistener.service.receiver.AbstractAutoSyncReceiver;

/**
 * Created by Neraud on 25/11/2014.
 */
public class ChoosePadVersionForCaptureAndSyncDialogFragment extends ChoosePadVersionDialogFragment {

	private Context mContext;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity.getApplicationContext();
	}

	@Override
	protected void handlePadVersionChosen(PADVersion chosenVersion) {
		Log.d(getClass().getName(), "handlePadVersionChosen : " + chosenVersion);

		final Intent serviceIntent = new Intent(mContext, AutoCaptureService.class);
		AutoCaptureService.addPadVersionInIntent(serviceIntent, PADVersion.US);
		AutoCaptureService.addCaptureListenerInIntent(serviceIntent, new AbstractAutoCaptureReceiver(new Handler()) {

			@Override
			public void notifyProgress(State state) {
				Log.d(getClass().getName(), "notifyProgress : " + state);
				if (state == State.CAPTURE_FINISHED) {
					triggerAutoSync();
				}
			}

			@Override
			public void notifyListenerError(Exception error) {
				Toast.makeText(mContext, R.string.auto_capture_start_listener_failed, Toast.LENGTH_LONG).show();
			}
		});
		mContext.startService(serviceIntent);
	}

	private void triggerAutoSync() {
		Log.d(getClass().getName(), "triggerAutoSync");
		final Intent serviceIntent = new Intent(mContext, AutoSyncService.class);
		AutoSyncService.addSyncListenerInIntent(serviceIntent, new AbstractAutoSyncReceiver(new Handler()) {
			@Override
			public void notifyProgress(State state) {
				Log.d(getClass().getName(), "notifyProgress : " + state);

				// TODO notification for the sync process
			}

			@Override
			public void notifyError(Error error, Throwable t) {
				switch (error) {
					case NO_MATCHING_ACCOUNT:

						String accountName = "";
						if (t instanceof NoMatchingAccountException) {
							accountName = ((NoMatchingAccountException) t).getAccountName();
						}
						final String content = getString(R.string.auto_sync_no_matching_account_content, accountName);
						Toast.makeText(mContext, content, Toast.LENGTH_LONG).show();
						break;
					case COMPUTE:
						Toast.makeText(mContext, R.string.auto_sync_compute_failed, Toast.LENGTH_LONG).show();
						break;
					case PUSH:
						Toast.makeText(mContext, R.string.auto_sync_push_failed, Toast.LENGTH_LONG).show();
						break;
				}
			}
		});
		mContext.startService(serviceIntent);
	}
}
