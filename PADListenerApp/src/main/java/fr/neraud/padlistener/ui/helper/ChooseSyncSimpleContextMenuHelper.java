package fr.neraud.padlistener.ui.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.SyncMode;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.model.ChooseModelContainer;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.SyncedMonsterModel;
import fr.neraud.padlistener.padherder.constant.MonsterPriority;
import fr.neraud.padlistener.ui.adapter.ChooseSyncMonstersSimpleAdapter;

/**
 * Helper to build and manage a context menu when displaying monster in a grouped list
 * Created by Neraud on 22/06/2014.
 */
public class ChooseSyncSimpleContextMenuHelper extends ChooseSyncBaseContextMenuHelper {

	private static final int MENU_ID_SELECT = 1;
	private static final int MENU_ID_DESELECT = 2;
	private static final int MENU_ID_CHANGE_PRIORITY = 3;
	private static final int MENU_ID_CHANGE_NOTE = 4;
	private static final int MENU_ID_ADD_TO_IGNORE_LIST = 5;

	private final ChooseSyncMonstersSimpleAdapter adapter;

	public ChooseSyncSimpleContextMenuHelper(Context context, SyncMode mode, ChooseSyncMonstersSimpleAdapter adapter, ChooseSyncModel result) {
		super(context, mode, result);
		this.adapter = adapter;
	}

	public void createContextMenu(ContextMenu menu, ContextMenu.ContextMenuInfo menuInfo) {
		MyLog.entry("menuInfo = " + menuInfo);

		final ChooseModelContainer<SyncedMonsterModel> monsterItem = getMonsterItem(menuInfo);
		menu.setHeaderTitle(getContext().getString(R.string.choose_sync_context_menu_one_title, monsterItem.getModel().getDisplayedMonsterInfo().getName()));
		if (monsterItem.isChosen()) {
			menu.add(getGroupId(), MENU_ID_DESELECT, 0, R.string.choose_sync_context_menu_one_deselect);
		} else {
			menu.add(getGroupId(), MENU_ID_SELECT, 0, R.string.choose_sync_context_menu_one_select);
		}

		if (getMode() != SyncMode.DELETED) {
			menu.add(getGroupId(), MENU_ID_CHANGE_PRIORITY, 0, R.string.choose_sync_context_menu_one_change_priority);
			menu.add(getGroupId(), MENU_ID_CHANGE_NOTE, 0, R.string.choose_sync_context_menu_one_change_note);
		}

		if (new DefaultSharedPreferencesHelper(getContext()).isChooseSyncUseIgnoreListForMonsters(getMode())) {
			menu.add(getGroupId(), MENU_ID_ADD_TO_IGNORE_LIST, 0, R.string.choose_sync_context_menu_one_add_to_ignore_list);
		}

		MyLog.exit();
	}

	public boolean doContextItemSelected(MenuItem menuItem) {
		MyLog.entry("menuItem = " + menuItem);

		final ChooseModelContainer<SyncedMonsterModel> monsterItem = getMonsterItem(menuItem.getMenuInfo());

		switch (menuItem.getItemId()) {
			case MENU_ID_SELECT:
				monsterItem.setChosen(true);
				notifyDataSetChanged();
				break;
			case MENU_ID_DESELECT:
				monsterItem.setChosen(false);
				notifyDataSetChanged();
				break;
			case MENU_ID_CHANGE_PRIORITY:
				final AlertDialog.Builder priorityDialogBuilder = new AlertDialog.Builder(getContext());
				final String priorityDialogTitle = getContext().getString(R.string.choose_sync_context_menu_one_change_priority_dialog_title, monsterItem.getModel().getDisplayedMonsterInfo().getName());
				priorityDialogBuilder.setTitle(priorityDialogTitle);
				int selected = monsterItem.getModel().getCapturedInfo().getPriority().ordinal();
				final CharSequence[] priorities = buildPriorityList();

				priorityDialogBuilder.setSingleChoiceItems(priorities, selected, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						MyLog.entry("i = " + i);
						final MonsterPriority priority = MonsterPriority.findByOrdinal(i);
						monsterItem.getModel().getCapturedInfo().setPriority(priority);
						dialogInterface.dismiss();
						notifyDataSetChanged();
						MyLog.exit();
					}
				});

				priorityDialogBuilder.create().show();
				break;
			case MENU_ID_CHANGE_NOTE:
				final AlertDialog.Builder noteDialogBuilder = new AlertDialog.Builder(getContext());
				final String noteDialogTitle = getContext().getString(R.string.choose_sync_context_menu_one_change_note_dialog_title, monsterItem.getModel().getDisplayedMonsterInfo().getName());
				noteDialogBuilder.setTitle(noteDialogTitle);

				final EditText input = new EditText(getContext());
				input.setText(monsterItem.getModel().getCapturedInfo().getNote());
				noteDialogBuilder.setView(input);
				noteDialogBuilder.setPositiveButton(R.string.choose_sync_context_menu_change_note_dialog_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						MyLog.entry();
						String newNote = input.getText().toString().trim();
						monsterItem.getModel().getCapturedInfo().setNote(newNote);
						notifyDataSetChanged();
						MyLog.exit();
					}
				});

				noteDialogBuilder.setNegativeButton(R.string.choose_sync_context_menu_change_note_dialog_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});
				noteDialogBuilder.create().show();

				break;
			case MENU_ID_ADD_TO_IGNORE_LIST:
				addMonsterToIgnoreList(monsterItem.getModel().getDisplayedMonsterInfo().getIdJP());
				notifyDataSetChanged();
				break;
			default:
		}

		MyLog.exit();
		return true;
	}

	private ChooseModelContainer<SyncedMonsterModel> getMonsterItem(ContextMenu.ContextMenuInfo menuInfo) {
		MyLog.entry("menuItem = " + menuInfo);

		final AdapterView.AdapterContextMenuInfo listItem = (AdapterView.AdapterContextMenuInfo) menuInfo;
		final ChooseModelContainer<SyncedMonsterModel> result = adapter.getItem(listItem.position);

		MyLog.exit();
		return result;
	}

	protected void notifyDataSetChanged() {
		adapter.notifyDataSetChanged();
	}
}
