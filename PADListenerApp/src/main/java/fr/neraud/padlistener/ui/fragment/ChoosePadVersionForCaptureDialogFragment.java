package fr.neraud.padlistener.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.PADVersion;
import fr.neraud.padlistener.exception.MissingRequirementException;
import fr.neraud.padlistener.service.AutoCaptureService;
import fr.neraud.padlistener.service.receiver.AbstractAutoCaptureReceiver;

/**
 * Created by Neraud on 25/11/2014.
 */
public class ChoosePadVersionForCaptureDialogFragment extends ChoosePadVersionDialogFragment {

	private Context mContext;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity.getApplicationContext();
	}

	@Override
	protected void handlePadVersionChosen(PADVersion chosenVersion) {
		MyLog.entry("chosenVersion = " + chosenVersion);

		final Intent serviceIntent = new Intent(mContext, AutoCaptureService.class);
		AutoCaptureService.addPadVersionInIntent(serviceIntent, chosenVersion);
		AutoCaptureService.addCaptureListenerInIntent(serviceIntent, new AbstractAutoCaptureReceiver(new Handler()) {

			@Override
			public void notifyProgress(State state) {
				MyLog.entry("state = " + state);
				MyLog.exit();
			}

			@Override
			public void notifyListenerError(Exception error) {
				String reason = error.getMessage();
				if(error instanceof MissingRequirementException) {
					reason = mContext.getString(((MissingRequirementException) error).getRequirement().getErrorTextResId());
				}
				final String label = mContext.getString(R.string.auto_capture_start_listener_failed, reason);
				Toast.makeText(mContext, label, Toast.LENGTH_LONG).show();
			}
		});
		mContext.startService(serviceIntent);

		MyLog.exit();
	}
}
