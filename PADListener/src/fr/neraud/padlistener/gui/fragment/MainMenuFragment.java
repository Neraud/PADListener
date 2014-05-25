
package fr.neraud.padlistener.gui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.AbstractPADListenerActivity;
import fr.neraud.padlistener.gui.constant.GuiScreen;

public class MainMenuFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View mainView = inflater.inflate(R.layout.main_menu_fragment, container, false);

		final TextView switchListener = (TextView) mainView.findViewById(R.id.switchListener);

		switchListener.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "switchListener.onClick");
				((AbstractPADListenerActivity) getActivity()).goToScreen(GuiScreen.SWITCH_LISTENER);
			}
		});

		final TextView viewMonsterInfo = (TextView) mainView.findViewById(R.id.viewMonsterInfo);
		viewMonsterInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "viewMonsterInfo.onClick");
				((AbstractPADListenerActivity) getActivity()).goToScreen(GuiScreen.VIEW_MONSTER_INFO);
			}
		});

		final TextView viewCapturedData = (TextView) mainView.findViewById(R.id.viewCapturedData);
		viewCapturedData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "viewCapturedData.onClick");
				((AbstractPADListenerActivity) getActivity()).goToScreen(GuiScreen.VIEW_CAPTURED_DATA);
			}
		});

		/*
		final TextView viewPadHerder = (TextView) mainView.findViewById(R.id.viewPadHerderData);
		viewPadHerder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "viewPadHerder.onClick");
				((AbstractPADListenerActivity) getActivity()).goToScreen(GuiScreen.VIEW_PADHERDER);
			}
		});
		*/
		final TextView computeSync = (TextView) mainView.findViewById(R.id.computeSync);
		computeSync.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "computeSync.onClick");
				((AbstractPADListenerActivity) getActivity()).goToScreen(GuiScreen.COMPUTE_SYNC);
			}
		});

		return mainView;
	}
}
