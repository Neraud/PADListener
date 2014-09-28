package fr.neraud.padlistener.ui.adapter;

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;

import fr.neraud.padlistener.model.CapturedFriendFullInfoModel;
import fr.neraud.padlistener.provider.helper.CapturedPlayerFriendProviderHelper;
import fr.neraud.padlistener.ui.model.CapturedFriendCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridCursorAdapter;

/**
 * Created by Neraud on 28/09/2014.
 */
public class CapturedFriendCursorAdapter extends CardGridCursorAdapter {

	private FragmentActivity mActivity;

	public CapturedFriendCursorAdapter(FragmentActivity activity) {
		super(activity);
		mActivity = activity;
	}

	@Override
	protected Card getCardFromCursor(Cursor cursor) {
		final CapturedFriendFullInfoModel model = CapturedPlayerFriendProviderHelper.cursorWithInfoToModel(cursor);
		final CapturedFriendCard card = new CapturedFriendCard(mActivity, model);

		return card;
	}

}
