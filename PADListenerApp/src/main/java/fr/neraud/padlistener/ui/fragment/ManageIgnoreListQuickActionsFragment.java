package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;

import java.util.Set;

import fr.neraud.padlistener.ui.adapter.ManageIgnoreListQuickActionsAdapter;
import fr.neraud.padlistener.helper.IgnoreMonsterQuickActionsHelper;
import fr.neraud.padlistener.helper.MonsterInfoHelper;

/**
 * ViewCapturedData fragment for the Information tab
 *
 * @author Neraud
 */
public class ManageIgnoreListQuickActionsFragment extends ListFragment {

	private ManageIgnoreListTaskFragment mTaskFragment;
	private ManageIgnoreListQuickActionsAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);

		final FragmentManager fm = getFragmentManager();
		mTaskFragment = (ManageIgnoreListTaskFragment) fm.findFragmentByTag(ManageIgnoreListTaskFragment.TAG_TASK_FRAGMENT);

		final IgnoreMonsterQuickActionsHelper helper = new IgnoreMonsterQuickActionsHelper(getActivity());
		adapter = new ManageIgnoreListQuickActionsAdapter(getActivity(), helper.extractQuickActions(), mTaskFragment);
		setListAdapter(adapter);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
		mTaskFragment.registerQuickActionsFragment(this);
	}

	@Override
	public void onDestroyView() {
		Log.d(getClass().getName(), "onDestroyView");
		super.onDestroyView();
		mTaskFragment.registerListFragment(null);
	}

	public void refreshAdapter(MonsterInfoHelper monsterInfoHelper, Set<Integer> ignoredIds) {
		Log.d(getClass().getName(), "refreshAdapter");
		adapter.notifyDataSetChanged();
	}

}
