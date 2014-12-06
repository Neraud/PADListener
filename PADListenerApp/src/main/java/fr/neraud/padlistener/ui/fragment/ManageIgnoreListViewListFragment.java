package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.exception.UnknownMonsterException;
import fr.neraud.padlistener.helper.MonsterInfoHelper;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.ui.adapter.IgnoredMonsterAdapter;

/**
 * ViewCapturedData fragment for the Monsters tabs
 *
 * @author Neraud
 */
public class ManageIgnoreListViewListFragment extends Fragment {

	private ManageIgnoreListTaskFragment mTaskFragment;
	private IgnoredMonsterAdapter mAdapter;

	@InjectView(R.id.manage_ignore_list_monsters_grid)
	GridView mGridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.entry();

		final View view = inflater.inflate(R.layout.manage_ignore_list_fragment_list, container, false);
		ButterKnife.inject(this, view);

		mTaskFragment = (ManageIgnoreListTaskFragment) getFragmentManager().findFragmentByTag(ManageIgnoreListTaskFragment.TAG_TASK_FRAGMENT);
		mAdapter = new IgnoredMonsterAdapter(getActivity(), mTaskFragment);
		mGridView.setAdapter(mAdapter);

		MyLog.exit();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		MyLog.entry();
		super.onViewCreated(view, savedInstanceState);
		mTaskFragment.registerListFragment(this);
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
		final List<MonsterInfoModel> monsterModels = new ArrayList<MonsterInfoModel>();

		final List<Integer> ignoredIdList = new ArrayList<Integer>(ignoredIds);
		Collections.sort(ignoredIdList);

		for (final Integer ignoredId : ignoredIdList) {
			try {
				final MonsterInfoModel monsterInfo = monsterInfoHelper.getMonsterInfo(ignoredId);
				monsterModels.add(monsterInfo);
			} catch (UnknownMonsterException e) {
				MyLog.warn("missing monster info for id = " + e.getMonsterId());
			}
		}

		mAdapter.clear();
		mAdapter.addAll(monsterModels);
		MyLog.exit();
	}
}
