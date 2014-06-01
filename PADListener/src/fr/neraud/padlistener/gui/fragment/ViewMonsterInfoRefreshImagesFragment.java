
package fr.neraud.padlistener.gui.fragment;

import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.fragment.ViewMonsterInfoRefreshImagesTaskFragment.CallBacks;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.service.constant.RestCallState;

public class ViewMonsterInfoRefreshImagesFragment extends Fragment {

	private static final String TAG_TASK_FRAGMENT = "images_task_fragment";
	private ViewMonsterInfoRefreshImagesTaskFragment mTaskFragment;

	private TextView statusText;
	private TextView current;
	private ProgressBar progress;
	private Button startButton;

	private final CallBacks callbacks = new CallBacks() {

		@Override
		public void updateState(RestCallState state, int imagesDownloaded, int imagesToDownload, int monsterIdDownloading) {
			Log.d(getClass().getName(), "updateState");

			if (state != null) {
				startButton.setEnabled(false);
				progress.setVisibility(View.VISIBLE);
				progress.setIndeterminate(false);

				switch (state) {
				case RUNNING:
					progress.setProgress(imagesDownloaded);
					progress.setMax(imagesToDownload);
					statusText.setText(getString(R.string.monster_info_fetch_images_text, (imagesDownloaded + 1), imagesToDownload,
					        monsterIdDownloading));
					break;
				case SUCCESSED:
					statusText.setText(getString(R.string.monster_info_fetch_images_done, imagesDownloaded));
					refreshLastUpdate();
					break;
				default:
					break;
				}
			} else {
				progress.setVisibility(View.GONE);
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View view = inflater.inflate(R.layout.view_monster_info_fragment_refresh_images, container, false);

		statusText = (TextView) view.findViewById(R.id.monster_info_fetch_images_status);
		progress = (ProgressBar) view.findViewById(R.id.monster_info_fetch_images_progress);
		current = (TextView) view.findViewById(R.id.monster_info_fetch_images_current);
		startButton = (Button) view.findViewById(R.id.monster_info_fetch_images_button);

		refreshLastUpdate();

		final FragmentManager fm = getFragmentManager();
		mTaskFragment = (ViewMonsterInfoRefreshImagesTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
		if (mTaskFragment == null) {
			mTaskFragment = new ViewMonsterInfoRefreshImagesTaskFragment();
			fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
		}
		mTaskFragment.registerCallbacks(callbacks);

		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "onClick");
				mTaskFragment.startFetchImagesService();
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
