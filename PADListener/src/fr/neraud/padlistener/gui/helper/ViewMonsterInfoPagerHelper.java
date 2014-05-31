
package fr.neraud.padlistener.gui.helper;

import android.support.v4.app.Fragment;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.fragment.ViewMonsterInfoListFragment;
import fr.neraud.padlistener.gui.fragment.ViewMonsterInfoRefreshImagesFragment;
import fr.neraud.padlistener.gui.fragment.ViewMonsterInfoRefreshInfoFragment;

public class ViewMonsterInfoPagerHelper {

	public int getCount() {
		return 3;
	}

	public Fragment createFragment(int position) {
		switch (position) {
		case 0:
			return new ViewMonsterInfoListFragment();
		case 1:
			return new ViewMonsterInfoRefreshInfoFragment();
		case 2:
			return new ViewMonsterInfoRefreshImagesFragment();
		default:
			return null;
		}
	}

	public Integer getTitle(int position) {
		switch (position) {
		case 0:
			return R.string.view_monster_info_tab_monsters;
		case 1:
			return R.string.view_monster_info_tab_refresh_info;
		case 2:
			return R.string.view_monster_info_tab_refresh_images;
		default:
			return null;
		}
	}

}
