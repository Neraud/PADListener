
package fr.neraud.padlistener.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.service.FetchPadHerderMonsterImageService;
import fr.neraud.padlistener.service.FetchPadHerderMonsterInfoService;
import fr.neraud.padlistener.service.constant.RestCallError;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;
import fr.neraud.padlistener.service.receiver.AbstractRestResultReceiver;

// FIXME : rotation restart fetching ...
public class FetchPadHerderMonsterInfoActivity extends Activity {

	private TextView mainText;
	private ProgressBar mainProgress;
	private TextView imagesText;
	private ProgressBar imagesProgress;
	private TextView imagesDetailText;

	private class FetchInfoReceiver extends AbstractRestResultReceiver<Integer> {

		public FetchInfoReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveProgress(RestCallRunningStep progress) {
			Log.d(getClass().getName(), "onReceiveProgress : " + progress);
			switch (progress) {
			case STARTED:
				mainText.setText(R.string.fetch_padherder_monster_info_calling);
				mainProgress.setIndeterminate(false);
				mainProgress.setProgress(1);
				mainProgress.setMax(5);
				break;
			case RESPONSE_RECEIVED:
				mainText.setText(R.string.fetch_padherder_monster_info_parsing);
				mainProgress.setProgress(2);
				break;
			case RESPONSE_PARSED:
				mainText.setText(R.string.fetch_padherder_monster_info_saving);
				mainProgress.setProgress(3);
				break;
			default:
				break;
			}

		}

		@Override
		protected void onReceiveSuccess(Integer result) {
			Log.d(getClass().getName(), "onReceiveSuccess");

			// TODO print count
			mainText.setText(R.string.fetch_padherder_monster_info_fetching_images);
			mainProgress.setProgress(4);
			startFetchingImages();
		}

		@Override
		protected void onReceiveError(RestCallError error, String errorMessage) {
			Log.d(getClass().getName(), "onReceiveError : " + error);
			// TODO error printing
			mainText.setText(R.string.fetch_padherder_monster_info_fetching_failed);
		}

	}

	private class ImageDownloadResultReceiver extends ResultReceiver {

		public ImageDownloadResultReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			final int imagesDownloaded = resultData.getInt("imagesDownloaded");
			switch (resultCode) {
			case 0:
				final int imagesToDownload = resultData.getInt("imagesToDownload");
				final String imageUrl = resultData.getString("imageUrl");
				final int monsterId = resultData.getInt("monsterId");

				imagesProgress.setIndeterminate(false);
				imagesProgress.setProgress(imagesDownloaded);
				imagesProgress.setMax(imagesToDownload);
				imagesText.setText(getString(R.string.fetch_padherder_monster_info_fetching_image_text, (imagesDownloaded + 1),
				        imagesToDownload, monsterId));
				imagesDetailText.setText(imageUrl);
				break;
			case 1:
				mainProgress.setProgress(5);
				mainText.setText(R.string.fetch_padherder_monster_info_fetching_done);
				imagesText.setText(getString(R.string.fetch_padherder_monster_info_fetching_image_done, imagesDownloaded));
				imagesProgress.setVisibility(View.GONE);
				imagesDetailText.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fetch_padherder_monster_info);

		mainText = (TextView) findViewById(R.id.fetch_padherder_monster_info_main_text);
		mainProgress = (ProgressBar) findViewById(R.id.fetch_padherder_monster_info_main_progress);
		imagesText = (TextView) findViewById(R.id.fetch_padherder_monster_info_images_text);
		imagesProgress = (ProgressBar) findViewById(R.id.fetch_padherder_monster_info_images_progress);
		imagesDetailText = (TextView) findViewById(R.id.fetch_padherder_monster_info_images_detail_text);

		startFetchingInfo();
	}

	private void startFetchingInfo() {
		Log.d(getClass().getName(), "startFetchingInfo");

		mainProgress.setIndeterminate(true);
		imagesText.setVisibility(View.GONE);
		imagesProgress.setVisibility(View.GONE);
		imagesDetailText.setVisibility(View.GONE);

		final Intent startIntent = new Intent(getApplicationContext(), FetchPadHerderMonsterInfoService.class);
		startIntent.putExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME, new FetchInfoReceiver(new Handler()));
		startService(startIntent);
	}

	private void startFetchingImages() {
		Log.d(getClass().getName(), "startFetchingImages");

		imagesText.setVisibility(View.VISIBLE);
		imagesProgress.setIndeterminate(true);
		imagesProgress.setVisibility(View.VISIBLE);
		imagesDetailText.setVisibility(View.VISIBLE);

		final Intent startIntent = new Intent(getApplicationContext(), FetchPadHerderMonsterImageService.class);
		startIntent.putExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME, new ImageDownloadResultReceiver(new Handler()));
		startService(startIntent);
	}
}
