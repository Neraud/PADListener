
package fr.neraud.padlistener.gui.fragment;

import java.text.SimpleDateFormat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
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
import fr.neraud.padlistener.service.FetchPadHerderMonsterImageService;
import fr.neraud.padlistener.service.receiver.AbstractRestResultReceiver;

public class ViewMonsterInfoRefreshImagesFragment extends Fragment {

	private TextView statusText;
	private TextView current;
	private ProgressBar progress;

	private class MyResultReceiver extends ResultReceiver {

		public MyResultReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			final int imagesDownloaded = resultData.getInt("imagesDownloaded");
			switch (resultCode) {
			case 0:
				final int imagesToDownload = resultData.getInt("imagesToDownload");
				//final String imageUrl = resultData.getString("imageUrl");
				final int monsterId = resultData.getInt("monsterId");

				progress.setIndeterminate(false);
				progress.setProgress(imagesDownloaded);
				progress.setMax(imagesToDownload);
				statusText.setText(getString(R.string.monster_info_fetch_images_text, (imagesDownloaded + 1), imagesToDownload,
				        monsterId));
				break;
			case 1:
				statusText.setText(getString(R.string.monster_info_fetch_images_done, imagesDownloaded));
				refreshLastUpdate();
				break;
			default:
				break;
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View view = inflater.inflate(R.layout.view_monster_info_fragment_refresh_images, container, false);

		statusText = (TextView) view.findViewById(R.id.monster_info_fetch_images_status);
		progress = (ProgressBar) view.findViewById(R.id.monster_info_fetch_images_progress);
		progress.setVisibility(View.GONE);
		current = (TextView) view.findViewById(R.id.monster_info_fetch_images_current);

		refreshLastUpdate();

		final Button startButton = (Button) view.findViewById(R.id.monster_info_fetch_images_button);
		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "onClick");
				startButton.setEnabled(false);
				progress.setIndeterminate(true);
				progress.setVisibility(View.VISIBLE);

				final Intent startIntent = new Intent(getActivity(), FetchPadHerderMonsterImageService.class);
				startIntent.putExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME, new MyResultReceiver(new Handler()));
				getActivity().startService(startIntent);
			}
		});

		return view;
	}

	private void refreshLastUpdate() {
		final String lastRefreshDate = SimpleDateFormat.getDateInstance().format(
		        new TechnicalSharedPreferencesHelper(getActivity()).getMonsterImagesRefreshDate());
		current.setText(getString(R.string.monster_info_fetch_images_current, lastRefreshDate));
	}

}
