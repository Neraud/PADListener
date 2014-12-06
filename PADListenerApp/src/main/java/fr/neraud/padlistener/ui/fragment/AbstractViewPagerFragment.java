package fr.neraud.padlistener.ui.fragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.widget.SlidingTabLayout;

/**
 * Base fragment with a ViewPager and tabs.<br/>
 * Handles paging and links with tabs.
 *
 * @author Neraud
 */
public abstract class AbstractViewPagerFragment extends Fragment {

	private ViewPager mViewPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.entry();

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

			@Override
			public CharSequence getPageTitle(int position) {
				return getString(getTabTitle(position));
			}
		};

		mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				MyLog.entry("position = " + position);
				// When swiping between pages, select the corresponding tab.
				final ActionBar actionBar = getActivity().getActionBar();
				if (actionBar != null) {
					actionBar.setSelectedNavigationItem(position);
				}

				// notifyFragmentSelected will be called in onTabSelected, ne need to call it twice
				//notifyFragmentSelected(position);
				MyLog.exit();
			}
		});

		//registerTabsInActionBar(view);
		registerTabsInCustomTabHost(view);

		MyLog.exit();
		return view;
	}

	private void registerTabsInActionBar(View view) {
		MyLog.entry();

		final ActionBar actionBar = getActivity().getActionBar();
		if (actionBar != null) {
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

			final ActionBar.TabListener tabListener = new ActionBar.TabListener() {

				@Override
				public void onTabSelected(Tab tab, FragmentTransaction ft) {
					MyLog.entry("tabPosition = " + tab.getPosition());
					// When the tab is selected, switch to the corresponding page in the ViewPager.
					mViewPager.setCurrentItem(tab.getPosition());
					notifyFragmentSelected(tab.getPosition());
					MyLog.exit();
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
		}
		MyLog.exit();
	}

	private void registerTabsInCustomTabHost(View view) {
		MyLog.entry();
		final SlidingTabLayout slidingTabLayout = (SlidingTabLayout)view.findViewById(R.id.sliding_tabs);
		slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
		slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.tab_selected_strip));
		slidingTabLayout.setDistributeEvenly(true);
		slidingTabLayout.setViewPager(mViewPager);
		MyLog.exit();
	}

	/**
	 * @return the page counts
	 */
	protected abstract int getPageCount();

	/**
	 * @param position position
	 * @return the Fragment corresponding to the page position
	 */
	protected abstract Fragment getPageFragment(int position);

	/**
	 * @param position position
	 * @return the resource id for the tab title
	 */
	protected abstract Integer getTabTitle(int position);

	protected void notifyFragmentSelected(int position) {
		// Override if necessary
	}
}
