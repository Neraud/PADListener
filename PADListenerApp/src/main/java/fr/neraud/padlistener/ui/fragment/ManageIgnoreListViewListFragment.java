package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.exception.UnknownMonsterException;
import fr.neraud.padlistener.helper.MonsterInfoHelper;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.ui.model.AbstractMonsterCard;
import fr.neraud.padlistener.ui.model.MonsterIgnoredCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

/**
 * ViewCapturedData fragment for the Monsters tabs
 *
 * @author Neraud
 */
public class ManageIgnoreListViewListFragment extends Fragment {

	private ManageIgnoreListTaskFragment mTaskFragment;
	private CardArrayAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View view = inflater.inflate(R.layout.manage_ignore_list_fragment_list, container, false);

		mTaskFragment = (ManageIgnoreListTaskFragment) getFragmentManager().findFragmentByTag(ManageIgnoreListTaskFragment.TAG_TASK_FRAGMENT);

		final CardGridView mListView = (CardGridView) view.findViewById(R.id.manage_ignore_list_monsters_grid);
		mAdapter = new CardArrayAdapter(getActivity(), new ArrayList<Card>());
		mListView.setAdapter(mAdapter);

		return view;
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
		final List<AbstractMonsterCard> monsterCards = new ArrayList<AbstractMonsterCard>();

		final List<Integer> ignoredIdList = new ArrayList<Integer>(ignoredIds);
		Collections.sort(ignoredIdList);

		for (final Integer ignoredId : ignoredIdList) {
			try {
				final MonsterInfoModel monsterInfo = monsterInfoHelper.getMonsterInfo(ignoredId);
				monsterCards.add(new MonsterIgnoredCard(getActivity(), monsterInfo, mTaskFragment));
			} catch(UnknownMonsterException e) {
				Log.w(getClass().getName(), "refreshAdapter : missing monster info for id = " + e.getMonsterId());
			}
		}

		mAdapter.clear();
		mAdapter.addAll(monsterCards);
	}
}
