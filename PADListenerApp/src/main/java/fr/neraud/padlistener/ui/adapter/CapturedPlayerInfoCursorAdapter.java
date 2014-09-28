package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.CapturedPlayerInfoModel;
import fr.neraud.padlistener.provider.helper.CapturedPlayerInfoProviderHelper;

/**
 * Adaptor to display the captured player information
 *
 * @author Neraud
 */
public class CapturedPlayerInfoCursorAdapter extends SimpleCursorAdapter {

	public CapturedPlayerInfoCursorAdapter(Context context, int layout) {
		super(context, layout, null, new String[0], new int[0], 0);
	}

	@Override
	public void bindView(View view, final Context context, Cursor cursor) {
		Log.d(getClass().getName(), "bindView");
		final CapturedPlayerInfoModel model = CapturedPlayerInfoProviderHelper.cursorToModel(cursor);

		final String lineName = context.getString(R.string.view_captured_info_item_name, model.getName(), model.getLastUpdate());
		((TextView) view.findViewById(R.id.view_captured_data_info_item_name)).setText(lineName);

		final String lineRegion = context.getString(R.string.view_captured_info_item_region, model.getRegion().name());
		((TextView) view.findViewById(R.id.view_captured_data_info_item_region)).setText(lineRegion);

		final String lineRank = context.getString(R.string.view_captured_info_item_rank, model.getRank(), model.getExp());
		((TextView) view.findViewById(R.id.view_captured_data_info_item_rank)).setText(lineRank);

		final String lineMaxRank = context.getString(R.string.view_captured_info_item_maxrank, model.getStaminaMax(),
				model.getCostMax());
		((TextView) view.findViewById(R.id.view_captured_data_info_item_maxrank)).setText(lineMaxRank);

		final String lineMaxStats = context.getString(R.string.view_captured_info_item_maxstats, model.getCardMax(),
				model.getFriendMax());
		((TextView) view.findViewById(R.id.view_captured_data_info_item_maxstats)).setText(lineMaxStats);

		final String lineCurrency = context.getString(R.string.view_captured_info_item_currency, model.getCoins(),
				model.getStones());
		((TextView) view.findViewById(R.id.view_captured_data_info_item_currency)).setText(lineCurrency);
	}

}
