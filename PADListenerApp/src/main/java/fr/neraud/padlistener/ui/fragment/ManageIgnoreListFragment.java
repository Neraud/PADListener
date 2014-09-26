package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.neraud.padlistener.R;

/**
 * Main fragment for ManageIgnoreList
 *
 * @author Neraud
 */
public class ManageIgnoreListFragment extends AbstractViewPagerFragment {

	private ManageIgnoreListTaskFragment mTaskFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = super.onCreateView(inflater, container, savedInstanceState);

		final FragmentManager fm = getFragmentManager();
		mTaskFragment = (ManageIgnoreListTaskFragment) fm.findFragmentByTag(ManageIgnoreListTaskFragment.TAG_TASK_FRAGMENT);
		if (mTaskFragment == null) {
			mTaskFragment = new ManageIgnoreListTaskFragment();
			fm.beginTransaction().add(mTaskFragment, ManageIgnoreListTaskFragment.TAG_TASK_FRAGMENT).commit();
		}

		return view;
	}

	@Override
	protected int getPageCount() {
		return 2;
	}

	@Override
	protected Fragment getPageFragment(int position) {
		Log.d(getClass().getName(), "getPageFragment : " + position);
		switch (position) {
			case 0:
				return new ManageIgnoreListViewListFragment();
			case 1:
				return new ManageIgnoreListQuickActionsFragment();
			default:
				return null;
		}
	}

	@Override
	protected Integer getTabTitle(int position) {
		switch (position) {
			case 0:
				return R.string.manage_ignore_list_tab_list;
			case 1:
				return R.string.manage_ignore_list_tab_quick_actions;
			default:
				return null;
		}
	}
}
