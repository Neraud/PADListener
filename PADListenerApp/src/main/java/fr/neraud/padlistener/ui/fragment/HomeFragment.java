package fr.neraud.padlistener.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.service.AutoCaptureService;
import fr.neraud.padlistener.service.AutoSyncService;
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

	@Override
	public void onResume() {
		Log.d(getClass().getName(), "onResume");
		super.onResume();

		fillCaptureCard(getView());
		fillSyncCard(getView());
	}

	private void fillCaptureCard(View mainView) {
		final Button autoButton = (Button) mainView.findViewById(R.id.home_capture_auto_button);

		final boolean autoButtonEnabled;
		final View.OnClickListener autoOnClickListener;
		if(canUseAutoCapture()) {
			autoButtonEnabled = true;
			autoOnClickListener = new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d(getClass().getName(), "capture.autoButton.onClick");
					final Intent serviceIntent = new Intent(getActivity(), AutoCaptureService.class);
					getActivity().startService(serviceIntent);
				}
			};
		} else {
			autoButtonEnabled = false;
			autoOnClickListener = new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d(getClass().getName(), "capture.autoButton.onClick");
					final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle(R.string.home_auto_capture_disabled_title);
					builder.setCancelable(true);
					builder.setMessage(R.string.home_auto_capture_disabled_message);
					builder.create().show();
				}
			};
		}

		fillButton(autoButton, autoButtonEnabled, autoOnClickListener, R.drawable.button_primary);

		final Button manualButton = (Button) mainView.findViewById(R.id.home_capture_manual_button);

		final View.OnClickListener manualOnClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "capture.manualButton.onClick");
				((HomeActivity) getActivity()).goToScreen(UiScreen.SWITCH_LISTENER);
			}
		};

		fillButton(manualButton, true, manualOnClickListener, R.drawable.button_secondary);
	}

	private boolean canUseAutoCapture() {
		final DefaultSharedPreferencesHelper helper = new DefaultSharedPreferencesHelper(getActivity());

		return helper.getProxyMode().isAutomatic();
	}

	private void fillSyncCard(View mainView) {
		final Button autoButton = (Button) mainView.findViewById(R.id.home_sync_auto_button);

		final boolean autoButtonEnabled;
		final View.OnClickListener autoOnClickListener;
		if(canUseAutoSync()) {
			autoButtonEnabled = true;
			autoOnClickListener = new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d(getClass().getName(), "sync.autoButton.onClick");
					final Intent serviceIntent = new Intent(getActivity(), AutoSyncService.class);
					getActivity().startService(serviceIntent);
				}
			};
		} else {
			autoButtonEnabled = false;
			autoOnClickListener = new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d(getClass().getName(), "sync.autoButton.onClick");
					final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle(R.string.home_auto_sync_disabled_title);
					builder.setCancelable(true);
					builder.setMessage(R.string.home_auto_sync_disabled_message);
					builder.create().show();
				}
			};
		}

		fillButton(autoButton, autoButtonEnabled, autoOnClickListener, R.drawable.button_primary);

		final Button manualButton = (Button) mainView.findViewById(R.id.home_sync_manual_button);

		final View.OnClickListener manualOnClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "sync.manualButton.onClick");
				((HomeActivity) getActivity()).goToScreen(UiScreen.COMPUTE_SYNC);
			}
		};

		fillButton(manualButton, true, manualOnClickListener, R.drawable.button_secondary);
	}


	private boolean canUseAutoSync() {
		final DefaultSharedPreferencesHelper defaultHelper = new DefaultSharedPreferencesHelper(getActivity());
		boolean hasAccounts = defaultHelper.getPadHerderAccounts().size() > 0;

		final TechnicalSharedPreferencesHelper techHelper = new TechnicalSharedPreferencesHelper(getActivity());
		boolean hasCaptured = techHelper.getLastCaptureDate().getTime() > 0;

		return hasAccounts && hasCaptured;
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
