package fr.neraud.padlistener.gui.helper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.JsonCaptureHelper;
import fr.neraud.padlistener.model.CapturedPlayerInfoModel;
import fr.neraud.padlistener.provider.helper.CapturedPlayerInfoHelper;

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
		final CapturedPlayerInfoModel model = CapturedPlayerInfoHelper.cursorToModel(cursor);

		((TextView) view.findViewById(R.id.view_captured_data_info_item_name)).setText(model.getName());

		final String lineName = context.getString(R.string.view_captured_info_item_name, model.getName(), model.getLastUpdate());
		((TextView) view.findViewById(R.id.view_captured_data_info_item_name)).setText(lineName);

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

		final Button shareButton = (Button) view.findViewById(R.id.view_captured_data_info_item_share);
		final JsonCaptureHelper jsonCaptureHelper = new JsonCaptureHelper(context);
		if (jsonCaptureHelper.hasPadCapturedData()) {
			shareButton.setVisibility(View.VISIBLE);
			shareButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d(getClass().getName(), "onClick");
					Intent sendIntent = new Intent(Intent.ACTION_SEND);
					//sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					sendIntent.setType("text/plain");

					Uri uri = Uri.fromFile(jsonCaptureHelper.getPadCapturedDataFile());
					sendIntent.putExtra(Intent.EXTRA_STREAM, uri);

					context.startActivity(Intent.createChooser(sendIntent, context.getText(R.string.view_captured_info_item_share_dialog_label)));
				}
			});
		} else {
			shareButton.setVisibility(View.GONE);
		}
	}

}
