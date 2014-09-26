package fr.neraud.padlistener.ui.fragment;

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
		return 3;
	}

	@Override
	protected Fragment getPageFragment(int position) {
		switch (position) {
			case 0:
				return new ViewCapturedDataInfoFragment();
			case 1:
				return new ViewCapturedDataMonstersFragment();
			case 2:
				return new ViewCapturedDataFriendsFragment();
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
			case 2:
				return R.string.view_captured_data_tab_friends;
			default:
				return null;
		}
	}

}
