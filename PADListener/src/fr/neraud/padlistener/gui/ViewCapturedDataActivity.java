
package fr.neraud.padlistener.gui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.helper.ViewPagerAdapter;

public class ViewCapturedDataActivity extends FragmentActivity {

	private ViewPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_captured_data);

		mSectionsPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	@Override
	protected void onDestroy() {
		Log.d(getClass().getName(), "onDestroy");
		super.onDestroy();
	}

}
