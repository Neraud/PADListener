
package fr.neraud.padlistener.gui;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.helper.ViewCapturedDataPagerHelper;

public class ViewCapturedDataActivity extends FragmentActivity {

	private ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_captured_data);

		final ViewCapturedDataPagerHelper helper = new ViewCapturedDataPagerHelper();

		final FragmentPagerAdapter mSectionsPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return helper.getCount();
			}

			@Override
			public Fragment getItem(int position) {
				return helper.createFragment(position);
			}
		};

		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// When swiping between pages, select the corresponding tab.
				getActionBar().setSelectedNavigationItem(position);
			}
		});

		final ActionBar actionBar = getActionBar();
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

		for (int i = 0; i < helper.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(helper.getTitle(i)).setTabListener(tabListener));
		}
	}

	@Override
	protected void onDestroy() {
		Log.d(getClass().getName(), "onDestroy");
		super.onDestroy();
	}

}
