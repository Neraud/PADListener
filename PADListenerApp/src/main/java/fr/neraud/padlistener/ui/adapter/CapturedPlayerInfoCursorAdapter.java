package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.CapturedPlayerInfoModel;
import fr.neraud.padlistener.provider.helper.CapturedPlayerInfoProviderHelper;

/**
 * Adaptor to display the captured player information
 *
 * @author Neraud
 */
public class CapturedPlayerInfoCursorAdapter extends SimpleCursorAdapter {

	static class ViewHolder {

		@InjectView(R.id.view_captured_data_info_item_name)
		TextView nameTextView;
		@InjectView(R.id.view_captured_data_info_item_region)
		TextView regionTextView;
		@InjectView(R.id.view_captured_data_info_item_rank)
		TextView rankTextView;
		@InjectView(R.id.view_captured_data_info_item_maxrank)
		TextView maxRankTextView;
		@InjectView(R.id.view_captured_data_info_item_maxstats)
		TextView maxStatsTextView;
		@InjectView(R.id.view_captured_data_info_item_currency)
		TextView currencyTextView;

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

	public CapturedPlayerInfoCursorAdapter(Context context, int layout) {
		super(context, layout, null, new String[0], new int[0], 0);
	}

	@Override
	public void bindView(View view, final Context context, Cursor cursor) {
		MyLog.entry();

		final ViewHolder viewHolder = new ViewHolder(view);
		final CapturedPlayerInfoModel model = CapturedPlayerInfoProviderHelper.cursorToModel(cursor);

		final String lineName = context.getString(R.string.view_captured_info_item_name, model.getName(), model.getLastUpdate());
		viewHolder.nameTextView.setText(lineName);

		final String lineRegion = context.getString(R.string.view_captured_info_item_region, model.getRegion().name());
		viewHolder.regionTextView.setText(lineRegion);

		final String lineRank = context.getString(R.string.view_captured_info_item_rank, model.getRank(), model.getExp());
		viewHolder.rankTextView.setText(lineRank);

		final String lineMaxRank = context.getString(R.string.view_captured_info_item_maxrank, model.getStaminaMax(), model.getCostMax());
		viewHolder.maxRankTextView.setText(lineMaxRank);

		final String lineMaxStats = context.getString(R.string.view_captured_info_item_maxstats, model.getCardMax(), model.getFriendMax());
		viewHolder.maxStatsTextView.setText(lineMaxStats);

		final String lineCurrency = context.getString(R.string.view_captured_info_item_currency, model.getCoins(), model.getStones());
		viewHolder.currencyTextView.setText(lineCurrency);

		MyLog.exit();
	}

}
