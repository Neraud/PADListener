
package fr.neraud.padlistener.gui.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import fr.neraud.padlistener.gui.fragment.ViewCapturedDataInfoFragment;
import fr.neraud.padlistener.gui.fragment.ViewCapturedDataMonstersFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
		case 0:
			return new ViewCapturedDataInfoFragment();
		case 1:
			return new ViewCapturedDataMonstersFragment();
		default:
			return null;
		}
	}

	@Override
	public int getCount() {
		return 2;
	}

}
