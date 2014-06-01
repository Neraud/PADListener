
package fr.neraud.padlistener.gui.fragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fr.neraud.padlistener.R;

public abstract class AbstractViewPagerFragment extends Fragment {

	private ViewPager mViewPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View view = inflater.inflate(R.layout.default_pager_fragment, container, false);

		final FragmentPagerAdapter mSectionsPagerAdapter = new FragmentPagerAdapter(getFragmentManager()) {

			@Override
			public int getCount() {
				return getPageCount();
			}

			@Override
			public Fragment getItem(int position) {
				return getPageFragment(position);
			}
		};

		mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// When swiping between pages, select the corresponding tab.
				getActivity().getActionBar().setSelectedNavigationItem(position);
			}
		});

		final ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		final ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				// When the tab is selected, switch to the corresponding page in the ViewPager.
				mViewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
			}
		};

		for (int i = 0; i < getPageCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(getTabTitle(i)).setTabListener(tabListener));
		}

		return view;
	}

	protected abstract int getPageCount();

	protected abstract Fragment getPageFragment(int position);

	protected abstract Integer getTabTitle(int position);
}
