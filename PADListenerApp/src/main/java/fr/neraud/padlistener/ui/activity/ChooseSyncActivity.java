package fr.neraud.padlistener.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.SyncMode;
import fr.neraud.padlistener.model.ChooseModelContainer;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.ComputeSyncResultModel;
import fr.neraud.padlistener.ui.constant.UiScreen;
import fr.neraud.padlistener.ui.fragment.PushSyncFragment;
import fr.neraud.padlistener.ui.helper.BaseHelpManager;

/**
 * Activity to choose elements to sync
 *
 * @author Neraud
 */
public class ChooseSyncActivity extends AbstractPADListenerActivity {

	public static final String EXTRA_ACCOUNT_ID_NAME = "account_id";
	public static final String EXTRA_CHOOSE_SYNC_RESULT_NAME = "choose_sync_result";
	private int mAccountId;
	private ChooseSyncModel mChooseResult;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.entry();

		super.onCreate(savedInstanceState);

		final Bundle extras = getIntent().getExtras();

		mAccountId = extras.getInt(EXTRA_ACCOUNT_ID_NAME);
		mChooseResult = (ChooseSyncModel) extras.getSerializable(ChooseSyncActivity.EXTRA_CHOOSE_SYNC_RESULT_NAME);

		if (mChooseResult.isHasEncounteredUnknownMonster()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.choose_sync_alert_unknown_monster_encountered_title);
			builder.setMessage(R.string.choose_sync_alert_unknown_monster_encountered_content);
			builder.create().show();
		}

		setContentView(R.layout.choose_sync_activity);

		MyLog.exit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.choose_sync, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_choose_sync_sync:
				pushSync();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void pushSync() {
		MyLog.entry();

		final boolean hasUserInfoToUpdate = mChooseResult.getSyncedUserInfoToUpdate().getModel().hasDataToSync();
		final boolean hasUserInfoToUpdateChosen = hasUserInfoToUpdate && mChooseResult.getSyncedUserInfoToUpdate().isChosen();
		final int materialToUpdateCount = mChooseResult.getSyncedMaterialsToUpdate().size();
		final int materialToUpdateChosenCount = countChosenItems(mChooseResult.getSyncedMaterialsToUpdate());
		final int monsterToUpdateCount = mChooseResult.getSyncedMonsters(SyncMode.UPDATED).size();
		final int monsterToUpdateChosenCount = countChosenItems(mChooseResult.getSyncedMonsters(SyncMode.UPDATED));
		final int monsterToCreateCount = mChooseResult.getSyncedMonsters(SyncMode.CREATED).size();
		final int monsterToCreateChosenCount = countChosenItems(mChooseResult.getSyncedMonsters(SyncMode.CREATED));
		final int monsterToDeleteCount = mChooseResult.getSyncedMonsters(SyncMode.DELETED).size();
		final int monsterToDeleteChosenCount = countChosenItems(mChooseResult.getSyncedMonsters(SyncMode.DELETED));

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.choose_sync_alert_push_title);

		if (materialToUpdateChosenCount > 0 || monsterToUpdateChosenCount > 0 || monsterToCreateChosenCount > 0
				|| monsterToDeleteChosenCount > 0 || hasUserInfoToUpdateChosen) {
			final String message = getString(R.string.choose_sync_alert_push_content_selected, materialToUpdateChosenCount, materialToUpdateCount,
					monsterToUpdateChosenCount, monsterToUpdateCount, monsterToCreateChosenCount, monsterToCreateCount,
					monsterToDeleteChosenCount, monsterToDeleteCount);

			builder.setMessage(message);
			builder.setPositiveButton(R.string.choose_sync_alert_push_button_ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					final Bundle bundle = new Bundle();
					bundle.putSerializable(PushSyncFragment.EXTRA_CHOOSE_SYNC_MODEL_NAME, mChooseResult);
					bundle.putSerializable(PushSyncFragment.EXTRA_ACCOUNT_ID_NAME, mAccountId);
					goToScreen(UiScreen.PUSH_SYNC, bundle);
				}
			});
		} else {
			builder.setMessage(R.string.choose_sync_alert_push_content_empty);
		}

		builder.setNegativeButton(R.string.choose_sync_alert_push_button_cancel, null);
		builder.create().show();

		MyLog.exit();
	}

	private static <T extends Serializable> int countChosenItems(List<ChooseModelContainer<T>> list) {
		int count = 0;
		for (final ChooseModelContainer<?> item : list) {
			if (item.isChosen()) {
				count++;
			}
		}
		return count;
	}

	protected BaseHelpManager getHelpManager() {
		return new BaseHelpManager(this, "choose_sync", 1) {
			@Override
			public void buildHelpPages(PageBuilder builder) {
				builder.addHelpPage(R.string.choose_sync_help_compute_sync_title, R.string.choose_sync_help_compute_sync_content);
				builder.addHelpPage(R.string.choose_sync_help_tabs_title, R.string.choose_sync_help_tabs_content);
				builder.addHelpPage(R.string.choose_sync_help_select_title, R.string.choose_sync_help_select_content);
				builder.addHelpPage(R.string.choose_sync_help_advanced_title, R.string.choose_sync_help_advanced_content);
				builder.addHelpPage(R.string.choose_sync_help_push_title, R.string.choose_sync_help_push_content, R.id.menu_choose_sync_sync, null);
			}

			@Override
			public void buildDeltaHelpPages(PageBuilder builder, int lastDisplayedVersion) {
				switch (lastDisplayedVersion) {
					default:
						buildHelpPages(builder);
				}
			}
		};
	}

	public ChooseSyncModel getChooseResult() {
		return mChooseResult;
	}
}
