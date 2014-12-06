package fr.neraud.padlistener.ui.fragment;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fr.neraud.log.MyLog;
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
		MyLog.entry();

		final View mainView = inflater.inflate(R.layout.home_fragment, container, false);

		fillCaptureCard(mainView);
		fillSyncCard(mainView);

		MyLog.exit();
		return mainView;
	}

	@Override
	public void onResume() {
		MyLog.entry();

		super.onResume();

		fillCaptureCard(getView());
		fillSyncCard(getView());
		fillCaptureAndSyncCard(getView());

		MyLog.exit();
	}

	private void fillCaptureCard(View mainView) {
		MyLog.entry();

		final Button autoButton = (Button) mainView.findViewById(R.id.home_capture_auto_button);

		final boolean autoButtonEnabled;
		final View.OnClickListener autoOnClickListener;
		if (canUseAutoCapture()) {
			autoButtonEnabled = true;
			autoOnClickListener = new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					MyLog.entry();
					showChoosePadVersionDialog(new ChoosePadVersionForCaptureDialogFragment());
					MyLog.exit();
				}
			};
		} else {
			autoButtonEnabled = false;
			autoOnClickListener = new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					MyLog.entry();

					final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle(R.string.home_auto_capture_disabled_title);
					builder.setCancelable(true);
					builder.setMessage(R.string.home_auto_capture_disabled_message);
					builder.create().show();

					MyLog.exit();
				}
			};
		}

		fillButton(autoButton, autoButtonEnabled, autoOnClickListener, R.drawable.button_primary);

		final Button manualButton = (Button) mainView.findViewById(R.id.home_capture_manual_button);

		final View.OnClickListener manualOnClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MyLog.entry();
				((HomeActivity) getActivity()).goToScreen(UiScreen.SWITCH_LISTENER);
				MyLog.exit();
			}
		};

		fillButton(manualButton, true, manualOnClickListener, R.drawable.button_secondary);

		MyLog.exit();
	}

	private boolean canUseAutoCapture() {
		final DefaultSharedPreferencesHelper helper = new DefaultSharedPreferencesHelper(getActivity());
		return helper.getProxyMode().isAutomatic();
	}

	private void fillSyncCard(View mainView) {
		MyLog.entry();

		final Button autoButton = (Button) mainView.findViewById(R.id.home_sync_auto_button);

		final boolean autoButtonEnabled;
		final View.OnClickListener autoOnClickListener;
		if (canUseAutoSync(true)) {
			autoButtonEnabled = true;
			autoOnClickListener = new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					MyLog.entry();

					final Bundle extras = new Bundle();
					extras.putBoolean(ComputeSyncActivity.AUTO_SYNC_EXTRA_NAME, true);
					((HomeActivity) getActivity()).goToScreen(UiScreen.COMPUTE_SYNC, extras);

					MyLog.exit();
				}
			};
		} else {
			autoButtonEnabled = false;
			autoOnClickListener = new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					MyLog.entry();

					final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle(R.string.home_auto_sync_disabled_title);
					builder.setCancelable(true);
					builder.setMessage(R.string.home_auto_sync_disabled_message);
					builder.create().show();

					MyLog.exit();
				}
			};
		}

		fillButton(autoButton, autoButtonEnabled, autoOnClickListener, R.drawable.button_primary);

		final Button manualButton = (Button) mainView.findViewById(R.id.home_sync_manual_button);

		final View.OnClickListener manualOnClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MyLog.entry();

				((HomeActivity) getActivity()).goToScreen(UiScreen.COMPUTE_SYNC);

				MyLog.exit();
			}
		};

		fillButton(manualButton, true, manualOnClickListener, R.drawable.button_secondary);

		MyLog.exit();
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

	private void fillCaptureAndSyncCard(View mainView) {
		MyLog.entry();

		final Button autoButton = (Button) mainView.findViewById(R.id.home_capture_and_sync_auto_button);

		final boolean autoButtonEnabled;
		final View.OnClickListener autoOnClickListener;
		if (canUseAutoCapture() && canUseAutoSync(false)) {
			autoButtonEnabled = true;
			autoOnClickListener = new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					MyLog.entry();
					showChoosePadVersionDialog(new ChoosePadVersionForCaptureAndSyncDialogFragment());
					MyLog.exit();
				}
			};
		} else {
			autoButtonEnabled = false;
			autoOnClickListener = new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					MyLog.entry();

					final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle(R.string.home_auto_capture_and_sync_disabled_title);
					builder.setCancelable(true);
					builder.setMessage(R.string.home_auto_capture_and_sync_disabled_message);
					builder.create().show();

					MyLog.exit();
				}
			};
		}
		fillButton(autoButton, autoButtonEnabled, autoOnClickListener, R.drawable.button_primary);

		MyLog.exit();
	}

	private void showChoosePadVersionDialog(ChoosePadVersionDialogFragment fragment) {
		MyLog.entry();

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("choosePadDialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		fragment.show(ft, "choosePadDialog");

		MyLog.exit();
	}
}
