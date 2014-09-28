package fr.neraud.padlistener.ui.adapter;

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;

import fr.neraud.padlistener.model.CapturedMonsterFullInfoModel;
import fr.neraud.padlistener.provider.helper.CapturedPlayerMonsterProviderHelper;
import fr.neraud.padlistener.ui.model.MonsterWithStatsCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridCursorAdapter;

/**
 * Adapter to display the ViewcapturedData monsters
 *
 * @author Neraud
 */
public class CapturedMonsterCursorAdapter extends CardGridCursorAdapter {

	private FragmentActivity mActivity;

	public CapturedMonsterCursorAdapter(FragmentActivity activity) {
		super(activity);
		mActivity = activity;
	}

	@Override
	protected Card getCardFromCursor(Cursor cursor) {
		final CapturedMonsterFullInfoModel model = CapturedPlayerMonsterProviderHelper.cursorWithInfoToModel(cursor);
		final MonsterWithStatsCard card = new MonsterWithStatsCard(mActivity, model.getMonsterInfo(), model.getCapturedMonster());

		return card;
	}

}
