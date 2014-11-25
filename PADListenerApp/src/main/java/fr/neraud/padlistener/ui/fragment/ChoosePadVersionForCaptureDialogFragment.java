package fr.neraud.padlistener.ui.fragment;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.PADVersion;
import fr.neraud.padlistener.service.AutoCaptureService;
import fr.neraud.padlistener.service.receiver.AbstractAutoCaptureReceiver;

/**
 * Created by Neraud on 25/11/2014.
 */
public class ChoosePadVersionForCaptureDialogFragment extends ChoosePadVersionDialogFragment {

	@Override
	protected void handlePadVersionChosen(PADVersion chosenVersion) {
		Log.d(getClass().getName(), "handlePadVersionChosen : " + chosenVersion);

		final Intent serviceIntent = new Intent(getActivity(), AutoCaptureService.class);
		AutoCaptureService.addPadVersionInIntent(serviceIntent, PADVersion.US);
		AutoCaptureService.addCaptureListenerInIntent(serviceIntent, new AbstractAutoCaptureReceiver(new Handler()) {

			@Override
			public void notifyProgress(State state) {
				Log.d(getClass().getName(), "notifyProgress : " + state);
			}

			@Override
			public void notifyListenerError(Exception error) {
				Toast.makeText(getActivity(), R.string.auto_capture_start_listener_failed, Toast.LENGTH_LONG).show();
			}
		});
		getActivity().startService(serviceIntent);

	}
}
