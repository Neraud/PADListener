package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.database.Cursor;

import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.helper.MonsterInfoProviderHelper;
import fr.neraud.padlistener.ui.model.MonsterInfoCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridCursorAdapter;

/**
 * Adapter to display the ViewMonsterInfo fragment for the Info tab
 *
 * @author Neraud
 */
public class MonsterInfoCursorAdapter extends CardGridCursorAdapter {

	public MonsterInfoCursorAdapter(Context context) {
		super(context);
	}

	@Override
	protected Card getCardFromCursor(Cursor cursor) {
		final MonsterInfoModel model = MonsterInfoProviderHelper.cursorToModel(cursor);

		final MonsterInfoCard card = new MonsterInfoCard(getContext(), model);

		return card;
	}

}
