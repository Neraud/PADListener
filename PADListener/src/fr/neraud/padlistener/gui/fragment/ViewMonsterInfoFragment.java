
package fr.neraud.padlistener.gui.fragment;

import android.support.v4.app.Fragment;
import fr.neraud.padlistener.R;

/**
 * Main fragment for ViewMonsterInfo
 * 
 * @author Neraud
 */
public class ViewMonsterInfoFragment extends AbstractViewPagerFragment {

	@Override
	protected int getPageCount() {
		return 3;
	}

	@Override
	protected Fragment getPageFragment(int position) {
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

	@Override
	protected Integer getTabTitle(int position) {
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
