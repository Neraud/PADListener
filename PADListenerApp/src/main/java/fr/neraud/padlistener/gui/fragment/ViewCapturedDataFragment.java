package fr.neraud.padlistener.gui.fragment;

import android.support.v4.app.Fragment;

import fr.neraud.padlistener.R;

/**
 * Main fragment for ViewCapturedData
 *
 * @author Neraud
 */
public class ViewCapturedDataFragment extends AbstractViewPagerFragment {

	@Override
	protected int getPageCount() {
		return 2;
	}

	@Override
	protected Fragment getPageFragment(int position) {
		switch (position) {
			case 0:
				return new ViewCapturedDataInfoFragment();
			case 1:
				return new ViewCapturedDataMonstersFragment();
			default:
				return null;
		}
	}

	@Override
	protected Integer getTabTitle(int position) {
		switch (position) {
			case 0:
				return R.string.view_captured_data_tab_info_player;
			case 1:
				return R.string.view_captured_data_tab_monsters;
			default:
				return null;
		}
	}

}
