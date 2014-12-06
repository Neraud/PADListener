package fr.neraud.padlistener.ui.fragment;

import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import fr.neraud.log.MyLog;
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
		MyLog.entry("chosenVersion = " + chosenVersion);

		final Intent serviceIntent = new Intent(getActivity(), AutoCaptureService.class);
		AutoCaptureService.addPadVersionInIntent(serviceIntent, PADVersion.US);
		AutoCaptureService.addCaptureListenerInIntent(serviceIntent, new AbstractAutoCaptureReceiver(new Handler()) {

			@Override
			public void notifyProgress(State state) {
				MyLog.entry("state = " + state);
				MyLog.exit();
			}

			@Override
			public void notifyListenerError(Exception error) {
				Toast.makeText(getActivity(), R.string.auto_capture_start_listener_failed, Toast.LENGTH_LONG).show();
			}
		});
		getActivity().startService(serviceIntent);

		MyLog.exit();
	}
}
