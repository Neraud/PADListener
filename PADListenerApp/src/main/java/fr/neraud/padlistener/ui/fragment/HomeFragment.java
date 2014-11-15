package fr.neraud.padlistener.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.activity.HomeActivity;
import fr.neraud.padlistener.ui.constant.UiScreen;

/**
 * Main fragment for MainMenu
 *
 * @author Neraud
 */
public class HomeFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View mainView = inflater.inflate(R.layout.home_fragment, container, false);

		fillCaptureCard(mainView);
		fillSyncCard(mainView);

		return mainView;
	}

	private void fillCaptureCard(View mainView) {
		final Button autoButton = (Button) mainView.findViewById(R.id.home_capture_auto_button);
		final Button manualButton = (Button) mainView.findViewById(R.id.home_capture_manual_button);

		final View.OnClickListener autoOnClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "capture.autoButton.onClick");
				// TODO
			}
		};

		final View.OnClickListener manualOnClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "capture.manualButton.onClick");
				((HomeActivity) getActivity()).goToScreen(UiScreen.SWITCH_LISTENER);
			}
		};

		fillButton(autoButton, false, autoOnClickListener, R.drawable.button_primary);
		fillButton(manualButton, true, manualOnClickListener, R.drawable.button_secondary);
	}

	private void fillSyncCard(View mainView) {
		final Button autoButton = (Button) mainView.findViewById(R.id.home_sync_auto_button);
		final Button manualButton = (Button) mainView.findViewById(R.id.home_sync_manual_button);

		final View.OnClickListener autoOnClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "sync.autoButton.onClick");
				// TODO
			}
		};

		final View.OnClickListener manualOnClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "sync.manualButton.onClick");
				((HomeActivity) getActivity()).goToScreen(UiScreen.COMPUTE_SYNC);
			}
		};

		fillButton(autoButton, false, autoOnClickListener, R.drawable.button_primary);
		fillButton(manualButton, true, manualOnClickListener, R.drawable.button_secondary);
	}

	private void fillButton(Button button, boolean enabled, View.OnClickListener listener, int drawableResId) {
		if (enabled) {
			button.setBackgroundResource(drawableResId);
		} else {
			// if the button is disabled, the listener won't be triggered, so we just swap the background to the disabled one
			final Drawable draw = getResources().getDrawable(drawableResId);
			draw.setState(new int[]{-android.R.attr.state_enabled});

			// deprecated since API 16 which introduced setBackgound(Drawable), but to remain compatible with API15 it still must be used
			//noinspection deprecation
			button.setBackgroundDrawable(draw.getCurrent());
		}

		if (listener != null) {
			button.setOnClickListener(listener);
		}
	}

}
