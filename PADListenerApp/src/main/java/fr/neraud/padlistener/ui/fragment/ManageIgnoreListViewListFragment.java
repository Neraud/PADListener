package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import fr.neraud.padlistener.exception.UnknownMonsterException;
import fr.neraud.padlistener.ui.adapter.ManageIgnoreListViewListAdapter;
import fr.neraud.padlistener.helper.MonsterInfoHelper;
import fr.neraud.padlistener.model.MonsterInfoModel;

/**
 * ViewCapturedData fragment for the Monsters tabs
 *
 * @author Neraud
 */
public class ManageIgnoreListViewListFragment extends ListFragment {

	private ManageIgnoreListTaskFragment mTaskFragment;
	private ManageIgnoreListViewListAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final FragmentManager fm = getFragmentManager();
		mTaskFragment = (ManageIgnoreListTaskFragment) fm.findFragmentByTag(ManageIgnoreListTaskFragment.TAG_TASK_FRAGMENT);

		adapter = new ManageIgnoreListViewListAdapter(getActivity().getApplicationContext(), mTaskFragment);
		setListAdapter(adapter);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
		mTaskFragment.registerListFragment(this);
	}

	@Override
	public void onDestroyView() {
		Log.d(getClass().getName(), "onDestroyView");
		super.onDestroyView();
		mTaskFragment.registerListFragment(null);
	}

	public void refreshAdapter(MonsterInfoHelper monsterInfoHelper, Set<Integer> ignoredIds) {
		Log.d(getClass().getName(), "refreshAdapter");
		final List<MonsterInfoModel> monsters = new ArrayList<MonsterInfoModel>();
		for (final Integer ignoredId : ignoredIds) {
			try {
				monsters.add(monsterInfoHelper.getMonsterInfo(ignoredId));
			} catch(UnknownMonsterException e) {
				Log.w(getClass().getName(), "refreshAdapter : missing monster info for id = " + e.getMonsterId());
			}
		}

		Collections.sort(monsters, new Comparator<MonsterInfoModel>() {
			@Override
			public int compare(MonsterInfoModel a, MonsterInfoModel b) {
				return a.getIdJP() - b.getIdJP();
			}
		});

		adapter.clear();
		adapter.addAll(monsters);
	}
}
