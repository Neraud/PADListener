
package fr.neraud.padlistener.gui.fragment;

import java.text.SimpleDateFormat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.service.FetchPadHerderMonsterInfoService;
import fr.neraud.padlistener.service.constant.RestCallError;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;
import fr.neraud.padlistener.service.receiver.AbstractRestResultReceiver;

public class ViewMonsterInfoRefreshInfoFragment extends Fragment {

	private TextView statusText;
	private TextView current;
	private ProgressBar progress;

	private class MyResultReceiver extends AbstractRestResultReceiver<Integer> {

		public MyResultReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveProgress(RestCallRunningStep step) {
			Log.d(getClass().getName(), "onReceiveProgress : " + step);
			switch (step) {
			case STARTED:
				statusText.setText(R.string.monster_info_fetch_info_calling);
				progress.setIndeterminate(false);
				progress.setProgress(1);
				progress.setMax(4);
				break;
			case RESPONSE_RECEIVED:
				statusText.setText(R.string.monster_info_fetch_info_parsing);
				progress.setProgress(2);
				break;
			case RESPONSE_PARSED:
				statusText.setText(R.string.monster_info_fetch_info_saving);
				progress.setProgress(3);
				break;
			default:
				break;
			}
		}

		@Override
		protected void onReceiveSuccess(Integer result) {
			Log.d(getClass().getName(), "onReceiveSuccess");
			progress.setProgress(4);
			statusText.setText(R.string.monster_info_fetch_info_fetching_done);
			refreshLastUpdate();
		}

		@Override
		protected void onReceiveError(RestCallError error, String errorMessage) {
			Log.d(getClass().getName(), "onReceiveError : " + error);
			statusText.setText(R.string.monster_info_fetch_info_fetching_failed);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View view = inflater.inflate(R.layout.view_monster_info_fragment_refresh_info, container, false);

		statusText = (TextView) view.findViewById(R.id.monster_info_fetch_info_status);
		progress = (ProgressBar) view.findViewById(R.id.monster_info_fetch_info_progress);
		progress.setVisibility(View.GONE);

		current = (TextView) view.findViewById(R.id.monster_info_fetch_info_current);
		refreshLastUpdate();

		final Button startButton = (Button) view.findViewById(R.id.monster_info_fetch_info_button);
		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "onClick");
				startButton.setEnabled(false);
				progress.setIndeterminate(true);
				progress.setVisibility(View.VISIBLE);
				statusText.setText(R.string.monster_info_fetch_info_fetching);

				final Intent startIntent = new Intent(getActivity(), FetchPadHerderMonsterInfoService.class);
				startIntent.putExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME, new MyResultReceiver(new Handler()));
				getActivity().startService(startIntent);
			}
		});

		return view;
	}

	private void refreshLastUpdate() {
		final String lastRefreshDate = SimpleDateFormat.getDateInstance().format(
		        new TechnicalSharedPreferencesHelper(getActivity()).getMonsterInfoRefreshDate());
		current.setText(getString(R.string.monster_info_fetch_info_current, lastRefreshDate));
	}
}
