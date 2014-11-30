package fr.neraud.padlistener.ui.activity;

import android.os.Bundle;
import android.util.Log;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.helper.BaseHelpManager;

/**
 * Activity to compute sync
 *
 * @author Neraud
 */
public class ComputeSyncActivity extends AbstractPADListenerActivity {

	public static String AUTO_SYNC_EXTRA_NAME = "auto_sync";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compute_sync_activity);
	}

	protected BaseHelpManager getHelpManager() {
		return new BaseHelpManager(this, "compute_sync", 1) {
			@Override
			public void buildHelpPages(PageBuilder builder) {
				builder.addHelpPage(R.string.compute_sync_help_compute_sync_title, R.string.compute_sync_help_compute_sync_content);
				builder.addHelpPage(R.string.compute_sync_help_choose_account_title, R.string.compute_sync_help_choose_account_content);
			}

			@Override
			public void buildDeltaHelpPages(PageBuilder builder, int lastDisplayedVersion) {
				switch (lastDisplayedVersion) {
					default: buildHelpPages(builder);
				}
			}
		};
	}

}
