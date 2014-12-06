package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;

/**
 * Main fragment for ManageIgnoreList
 *
 * @author Neraud
 */
public class ManageIgnoreListFragment extends AbstractViewPagerFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = super.onCreateView(inflater, container, savedInstanceState);

		final FragmentManager fm = getFragmentManager();
		ManageIgnoreListTaskFragment taskFragment = (ManageIgnoreListTaskFragment) fm.findFragmentByTag(ManageIgnoreListTaskFragment.TAG_TASK_FRAGMENT);
		if (taskFragment == null) {
			taskFragment = new ManageIgnoreListTaskFragment();
			fm.beginTransaction().add(taskFragment, ManageIgnoreListTaskFragment.TAG_TASK_FRAGMENT).commit();
		}

		return view;
	}

	@Override
	protected int getPageCount() {
		return 2;
	}

	@Override
	protected Fragment getPageFragment(int position) {
		MyLog.entry("position = " + position);

		Fragment fragment = null;
		switch (position) {
			case 0:
				fragment = new ManageIgnoreListViewListFragment();
				break;
			case 1:
				fragment = new ManageIgnoreListQuickActionsFragment();
				break;
		}

		MyLog.exit();
		return fragment;
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
