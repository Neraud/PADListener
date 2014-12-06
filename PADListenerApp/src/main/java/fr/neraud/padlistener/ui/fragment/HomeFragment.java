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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
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

	@InjectView(R.id.home_capture_auto_button)
	Button mCaptureAutoButton;
	@InjectView(R.id.home_capture_manual_button)
	Button mCaptureManualButton;

	@InjectView(R.id.home_sync_auto_button)
	Button mSyncAutoButton;
	@InjectView(R.id.home_sync_manual_button)
	Button mSyncManualButton;

	@InjectView(R.id.home_capture_and_sync_auto_button)
	Button mCaptureAndSyncAutoButton;

	private boolean mCanAutoCapture;
	private boolean mCanAutoSync;
	private boolean mCanAutoCaptureAndSync;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.entry();

		final View view = inflater.inflate(R.layout.home_fragment, container, false);
		ButterKnife.inject(this, view);

		MyLog.exit();
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	@Override
	public void onResume() {
		MyLog.entry();

		super.onResume();
		mCanAutoCapture = canUseAutoCapture();
		mCanAutoSync = canUseAutoSync(true);
		mCanAutoCaptureAndSync = mCanAutoCapture && canUseAutoSync(false);

		fillButton(mCaptureAutoButton, mCanAutoCapture, R.drawable.button_primary);
		fillButton(mCaptureManualButton, true, R.drawable.button_secondary);

		fillButton(mSyncAutoButton, mCanAutoSync, R.drawable.button_primary);
		fillButton(mSyncManualButton, true, R.drawable.button_secondary);

		fillButton(mCaptureAndSyncAutoButton, mCanAutoCaptureAndSync, R.drawable.button_primary);

		MyLog.exit();
	}

	private boolean canUseAutoCapture() {
		final DefaultSharedPreferencesHelper helper = new DefaultSharedPreferencesHelper(getActivity());
		return helper.getProxyMode().isAutomatic();
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

	private void fillButton(Button button, boolean enabled, int drawableResId) {
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


	@OnClick(R.id.home_capture_manual_button)
	@SuppressWarnings("unused")
	void onManualCaptureClicked() {
		MyLog.entry();
		((HomeActivity) getActivity()).goToScreen(UiScreen.SWITCH_LISTENER);
		MyLog.exit();
	}

	@OnClick(R.id.home_capture_auto_button)
	@SuppressWarnings("unused")
	void onAutoCaptureClicked() {
		MyLog.entry();

		if (mCanAutoCapture) {
			showChoosePadVersionDialog(new ChoosePadVersionForCaptureDialogFragment());
		} else {
			final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.home_auto_capture_disabled_title);
			builder.setCancelable(true);
			builder.setMessage(R.string.home_auto_capture_disabled_message);
			builder.create().show();
		}

		MyLog.exit();
	}

	@OnClick(R.id.home_sync_manual_button)
	@SuppressWarnings("unused")
	void onManualSyncClicked() {
		MyLog.entry();
		((HomeActivity) getActivity()).goToScreen(UiScreen.COMPUTE_SYNC);
		MyLog.exit();
	}

	@OnClick(R.id.home_sync_auto_button)
	@SuppressWarnings("unused")
	void onAutoSyncClicked() {
		MyLog.entry();

		if (mCanAutoSync) {
			final Bundle extras = new Bundle();
			extras.putBoolean(ComputeSyncActivity.AUTO_SYNC_EXTRA_NAME, true);
			((HomeActivity) getActivity()).goToScreen(UiScreen.COMPUTE_SYNC, extras);
		} else {
			final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.home_auto_sync_disabled_title);
			builder.setCancelable(true);
			builder.setMessage(R.string.home_auto_sync_disabled_message);
			builder.create().show();
		}

		MyLog.exit();
	}

	@OnClick(R.id.home_capture_and_sync_auto_button)
	@SuppressWarnings("unused")
	void onAutoCaptureAndSyncClicked() {
		MyLog.entry();

		if (mCanAutoCaptureAndSync) {
			showChoosePadVersionDialog(new ChoosePadVersionForCaptureAndSyncDialogFragment());
		} else {
			final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.home_auto_capture_and_sync_disabled_title);
			builder.setCancelable(true);
			builder.setMessage(R.string.home_auto_capture_and_sync_disabled_message);
			builder.create().show();
		}

		MyLog.exit();
	}
}
