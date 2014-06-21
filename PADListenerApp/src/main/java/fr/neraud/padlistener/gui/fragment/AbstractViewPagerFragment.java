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
				Log.d(getClass().getName(), "onPageSelected : " + position);
				// When swiping between pages, select the corresponding tab.
				final ActionBar actionBar = getActivity().getActionBar();
				if (actionBar != null) {
					actionBar.setSelectedNavigationItem(position);
				}

				// notifyFragmentSelected will be called in onTabSelected, ne need to call it twice
				//notifyFragmentSelected(position);
			}
		});

		final ActionBar actionBar = getActivity().getActionBar();
		if (actionBar != null) {
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

			final ActionBar.TabListener tabListener = new ActionBar.TabListener() {

				@Override
				public void onTabSelected(Tab tab, FragmentTransaction ft) {
					Log.d(getClass().getName(), "onTabSelected : " + tab.getPosition());
					// When the tab is selected, switch to the corresponding page in the ViewPager.
					mViewPager.setCurrentItem(tab.getPosition());
					notifyFragmentSelected(tab.getPosition());
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
		return view;
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
