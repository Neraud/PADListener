package fr.neraud.padlistener.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.JsonCaptureHelper;
import fr.neraud.padlistener.ui.constant.NavigationDrawerItem;

/**
 * Activity to view captured data
 *
 * @author Neraud
 */
public class ViewCapturedDataActivity extends AbstractPADListenerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_captured_data_activity);
	}

	@Override
	protected NavigationDrawerItem getSelfNavDrawerItem() {
		return NavigationDrawerItem.VIEW_CAPTURED_DATA;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_captured_data, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.view_captured_data_action_share:
				final JsonCaptureHelper jsonCaptureHelper = new JsonCaptureHelper(this);
				if (jsonCaptureHelper.hasPadCapturedData()) {
					Intent sendIntent = new Intent(Intent.ACTION_SEND);
					//sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					sendIntent.setType("text/plain");

					Uri uri = Uri.fromFile(jsonCaptureHelper.getPadCapturedDataFile());
					sendIntent.putExtra(Intent.EXTRA_STREAM, uri);

					startActivity(Intent.createChooser(sendIntent, getText(R.string.view_captured_info_item_share_dialog_label)));
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
