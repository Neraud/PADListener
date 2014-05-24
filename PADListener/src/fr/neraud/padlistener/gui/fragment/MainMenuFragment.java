
package fr.neraud.padlistener.gui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.MainActivity;

public class MainMenuFragment extends Fragment {

	public static enum MainMenuEntry {
		SWITCH_LISTENER,
		VIEW_MONSTER_INFO,
		VIEW_CAPTURED_DATA,
		//VIEW_PADHERDER,
		COMPUTE_SYNC;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View mainView = inflater.inflate(R.layout.fragment_main_menu, container, false);

		final TextView switchListener = (TextView) mainView.findViewById(R.id.switchListener);

		switchListener.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "switchListener.onClick");
				((MainActivity) getActivity()).clickOnMainMenu(MainMenuEntry.SWITCH_LISTENER);
			}
		});

		final TextView viewMonsterInfo = (TextView) mainView.findViewById(R.id.viewMonsterInfo);
		viewMonsterInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "viewMonsterInfo.onClick");
				((MainActivity) getActivity()).clickOnMainMenu(MainMenuEntry.VIEW_MONSTER_INFO);
			}
		});

		final TextView viewCapturedData = (TextView) mainView.findViewById(R.id.viewCapturedData);
		viewCapturedData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "viewCapturedData.onClick");
				((MainActivity) getActivity()).clickOnMainMenu(MainMenuEntry.VIEW_CAPTURED_DATA);
			}
		});

		/*
		final TextView viewPadHerder = (TextView) mainView.findViewById(R.id.viewPadHerderData);
		viewPadHerder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "viewPadHerder.onClick");
				((MainActivity) getActivity()).clickOnMyMenu(MainMenuEntry.VIEW_PADHERDER);
			}
		});
		*/
		final TextView computeSync = (TextView) mainView.findViewById(R.id.computeSync);
		computeSync.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "computeSync.onClick");
				((MainActivity) getActivity()).clickOnMainMenu(MainMenuEntry.COMPUTE_SYNC);
			}
		});

		return mainView;
	}
}
