package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.IgnoreMonsterQuickActionsHelper;
import fr.neraud.padlistener.helper.MonsterInfoHelper;
import fr.neraud.padlistener.ui.adapter.ManageIgnoreListQuickActionsAdapter;

/**
 * ViewCapturedData fragment for the Information tab
 *
 * @author Neraud
 */
public class ManageIgnoreListQuickActionsFragment extends Fragment {

	private ManageIgnoreListTaskFragment mTaskFragment;
	private ManageIgnoreListQuickActionsAdapter mAdapter;

	@InjectView(R.id.manage_ignore_list_quick_actions_list)
	ListView mListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.entry();

		final View view = inflater.inflate(R.layout.manage_ignore_list_fragment_quick_actions, container, false);
		ButterKnife.inject(this, view);

		mTaskFragment = (ManageIgnoreListTaskFragment) getFragmentManager().findFragmentByTag(ManageIgnoreListTaskFragment.TAG_TASK_FRAGMENT);

		final IgnoreMonsterQuickActionsHelper helper = new IgnoreMonsterQuickActionsHelper(getActivity());
		mAdapter = new ManageIgnoreListQuickActionsAdapter(getActivity(), helper.extractQuickActions(), mTaskFragment);
		mListView.setAdapter(mAdapter);

		MyLog.exit();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		MyLog.entry();
		super.onViewCreated(view, savedInstanceState);
		mTaskFragment.registerQuickActionsFragment(this);
		MyLog.exit();
	}

	@Override
	public void onDestroyView() {
		MyLog.entry();
		super.onDestroyView();
		mTaskFragment.registerListFragment(null);
		ButterKnife.reset(this);
		MyLog.exit();
	}

	public void refreshAdapter(MonsterInfoHelper monsterInfoHelper, Set<Integer> ignoredIds) {
		MyLog.entry();
		mAdapter.notifyDataSetChanged();
		MyLog.exit();
	}

}
