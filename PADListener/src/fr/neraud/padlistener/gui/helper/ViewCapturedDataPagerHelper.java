
package fr.neraud.padlistener.gui.helper;

import android.support.v4.app.Fragment;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.fragment.ViewCapturedDataInfoFragment;
import fr.neraud.padlistener.gui.fragment.ViewCapturedDataMonstersFragment;

public class ViewCapturedDataPagerHelper {

	public int getCount() {
		return 2;
	}

	public Fragment createFragment(int position) {
		switch (position) {
		case 0:
			return new ViewCapturedDataInfoFragment();
		case 1:
			return new ViewCapturedDataMonstersFragment();
		default:
			return null;
		}
	}

	public Integer getTitle(int position) {
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
