package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.IgnoreMonsterQuickActionsHelper;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.SyncedMaterialModel;
import fr.neraud.padlistener.ui.helper.ChooseSyncDataPagerHelper;
import fr.neraud.padlistener.ui.model.ChooseSyncMaterialCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * ChooseSync fragment for the MaterialsUpdated tab
 *
 * @author Neraud
 */
public class ChooseSyncMaterialsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View view = inflater.inflate(R.layout.choose_sync_fragment_materials, container, false);
		final CardListView mListView = (CardListView) view.findViewById(R.id.choose_sync_materials_list);

		final ChooseSyncModel result = (ChooseSyncModel) getArguments().getSerializable(ChooseSyncDataPagerHelper.ARGUMENT_SYNC_RESULT_NAME);

		final CardArrayAdapter adapter = new CardArrayAdapter(getActivity(), buildCardList(result.getSyncedMaterialsToUpdate()));
		mListView.setAdapter(adapter);

		return view;
	}

	private List<Card> buildCardList(List<ChooseSyncModelContainer<SyncedMaterialModel>> syncedMaterialsToUpdate) {
		final List<Card> materialsCard = new ArrayList<Card>();

		final IgnoreMonsterQuickActionsHelper helper = new IgnoreMonsterQuickActionsHelper(getActivity());
		for(final ChooseSyncModelContainer<SyncedMaterialModel> model : syncedMaterialsToUpdate) {
			materialsCard.add(new ChooseSyncMaterialCard(getActivity(), model));
		}

		return materialsCard;
	}

}
