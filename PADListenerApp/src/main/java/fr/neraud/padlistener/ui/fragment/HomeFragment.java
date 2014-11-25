package fr.neraud.padlistener.ui.fragment;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.ui.activity.ComputeSyncActivity;
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
		Log.d(getClass().getName(), "fillCaptureCard");
		final Button autoButton = (Button) mainView.findViewById(R.id.home_capture_auto_button);

		final boolean autoButtonEnabled;
		final View.OnClickListener autoOnClickListener;
		if (canUseAutoCapture()) {
			autoButtonEnabled = true;
			autoOnClickListener = new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d(getClass().getName(), "capture.autoButton.onClick");
					showChoosePadVersionDialog(new ChoosePadVersionForCaptureDialogFragment());
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
		Log.d(getClass().getName(), "fillSyncCard");
		final Button autoButton = (Button) mainView.findViewById(R.id.home_sync_auto_button);

		final boolean autoButtonEnabled;
		final View.OnClickListener autoOnClickListener;
		if (canUseAutoSync(true)) {
			autoButtonEnabled = true;
			autoOnClickListener = new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d(getClass().getName(), "sync.autoButton.onClick");
					final Bundle extras = new Bundle();
					extras.putBoolean(ComputeSyncActivity.AUTO_SYNC_EXTRA_NAME, true);
					((HomeActivity) getActivity()).goToScreen(UiScreen.COMPUTE_SYNC, extras);
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

	private boolean canUseAutoSync(boolean needsCapturedData) {
		final DefaultSharedPreferencesHelper defaultHelper = new DefaultSharedPreferencesHelper(getActivity());
		final boolean hasAccounts = defaultHelper.getPadHerderAccounts().size() > 0;

		boolean hasCaptured = true;
		if (needsCapturedData) {
			final TechnicalSharedPreferencesHelper techHelper = new TechnicalSharedPreferencesHelper(getActivity());
			hasCaptured = techHelper.getLastCaptureDate().getTime() > 0;
		}

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

	private void showChoosePadVersionDialog(ChoosePadVersionDialogFragment fragment) {
		Log.d(getClass().getName(), "showChoosePadVersionDialog");
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("choosePadDialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		fragment.show(ft, "choosePadDialog");
	}
}
