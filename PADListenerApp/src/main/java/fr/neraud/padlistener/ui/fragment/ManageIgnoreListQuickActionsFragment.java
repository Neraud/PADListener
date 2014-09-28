package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.IgnoreMonsterQuickActionsHelper;
import fr.neraud.padlistener.helper.MonsterInfoHelper;
import fr.neraud.padlistener.model.IgnoreMonsterQuickActionModel;
import fr.neraud.padlistener.ui.model.IgnoreListQuickActionCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * ViewCapturedData fragment for the Information tab
 *
 * @author Neraud
 */
public class ManageIgnoreListQuickActionsFragment extends Fragment {

	private ManageIgnoreListTaskFragment mTaskFragment;
	private CardArrayAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View view = inflater.inflate(R.layout.manage_ignore_list_fragment_quick_actions, container, false);

		mTaskFragment = (ManageIgnoreListTaskFragment) getFragmentManager().findFragmentByTag(ManageIgnoreListTaskFragment.TAG_TASK_FRAGMENT);

		final CardListView mListView = (CardListView) view.findViewById(R.id.manage_ignore_list_quick_actions_list);
		mAdapter = new CardArrayAdapter(getActivity(), buildCardList());
		mListView.setAdapter(mAdapter);

		return view;
	}

	private List<Card> buildCardList() {
		final List<Card> quickActionCards = new ArrayList<Card>();

		final IgnoreMonsterQuickActionsHelper helper = new IgnoreMonsterQuickActionsHelper(getActivity());
		for(final IgnoreMonsterQuickActionModel model : helper.extractQuickActions()) {
			quickActionCards.add(new IgnoreListQuickActionCard(getActivity(),mTaskFragment, model));
		}

		return quickActionCards;
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
		mAdapter.notifyDataSetChanged();
	}

}
