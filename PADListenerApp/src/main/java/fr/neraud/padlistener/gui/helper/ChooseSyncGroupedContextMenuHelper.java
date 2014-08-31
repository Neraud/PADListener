package fr.neraud.padlistener.gui.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.SyncMode;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.model.SyncedMonsterModel;
import fr.neraud.padlistener.padherder.constant.MonsterPriority;

/**
 * Helper to build and manage a context menu when displaying monster in a grouped list
 * Created by Neraud on 22/06/2014.
 */
public class ChooseSyncGroupedContextMenuHelper extends ChooseSyncBaseContextMenuHelper {

	private static final int MENU_ID_SELECT = 1;
	private static final int MENU_ID_DESELECT = 2;
	private static final int MENU_ID_CHANGE_PRIORITY = 3;
	private static final int MENU_ID_CHANGE_NOTE = 4;

	private static final int MENU_ID_SELECT_ALL = 11;
	private static final int MENU_ID_DESELECT_ALL = 12;
	private static final int MENU_ID_CHANGE_PRIORITY_ALL = 13;
	private static final int MENU_ID_CHANGE_NOTE_ALL = 14;
	private static final int MENU_ID_ADD_TO_IGNORE_LIST = 15;

	private final ChooseSyncMonstersGroupedAdapter adapter;

	public ChooseSyncGroupedContextMenuHelper(Context context, SyncMode mode, ChooseSyncMonstersGroupedAdapter adapter, ChooseSyncModel result) {
		super(context, mode, result);
		this.adapter = adapter;
	}

	public void createContextMenu(ContextMenu menu, ContextMenu.ContextMenuInfo menuInfo) {
		Log.d(getClass().getName(), "createContextMenu : " + menuInfo);
		if (isGroup(menuInfo)) {
			createContextMenuForGroup(menu, menuInfo);
		} else {
			createContextMenuForChild(menu, menuInfo);
		}
	}

	public boolean doContextItemSelected(MenuItem menuItem) {
		Log.d(getClass().getName(), "doContextItemSelected : menuItem = " + menuItem);
		if (isGroup(menuItem.getMenuInfo())) {
			return doContextItemSelectedForGroup(menuItem);
		} else {
			return doContextItemSelectedForChild(menuItem);
		}
	}

	private void createContextMenuForGroup(ContextMenu menu, ContextMenu.ContextMenuInfo menuInfo) {
		Log.d(getClass().getName(), "createContextMenuForGroup : " + menuInfo);

		final MonsterInfoModel monsterInfo = getGroupMonsterItem(menuInfo);

		menu.setHeaderTitle(getContext().getString(R.string.choose_sync_context_menu_all_title, monsterInfo.getName()));
		menu.add(getGroupId(), MENU_ID_DESELECT_ALL, 0, R.string.choose_sync_context_menu_all_deselect);
		menu.add(getGroupId(), MENU_ID_SELECT_ALL, 0, R.string.choose_sync_context_menu_all_select);

		if (getMode() != SyncMode.DELETED) {
			menu.add(getGroupId(), MENU_ID_CHANGE_PRIORITY_ALL, 0, R.string.choose_sync_context_menu_all_change_priority);
			menu.add(getGroupId(), MENU_ID_CHANGE_NOTE_ALL, 0, R.string.choose_sync_context_menu_all_change_note);
		}

		if (new DefaultSharedPreferencesHelper(getContext()).isChooseSyncUseIgnoreListForMonsters(getMode())) {
			menu.add(getGroupId(), MENU_ID_ADD_TO_IGNORE_LIST, 0, R.string.choose_sync_context_menu_all_add_to_ignore_list);
		}
	}

	private void createContextMenuForChild(ContextMenu menu, ContextMenu.ContextMenuInfo menuInfo) {
		Log.d(getClass().getName(), "createContextMenuForChild : " + menuInfo);

		final ChooseSyncModelContainer<SyncedMonsterModel> monsterItem = getChildMonsterItem(menuInfo);
		menu.setHeaderTitle(getContext().getString(R.string.choose_sync_context_menu_one_title, monsterItem.getSyncedModel().getDisplayedMonsterInfo().getName()));
		if (monsterItem.isChosen()) {
			menu.add(getGroupId(), MENU_ID_DESELECT, 0, R.string.choose_sync_context_menu_one_deselect);
		} else {
			menu.add(getGroupId(), MENU_ID_SELECT, 0, R.string.choose_sync_context_menu_one_select);
		}

		if (getMode() != SyncMode.DELETED) {
			menu.add(getGroupId(), MENU_ID_CHANGE_PRIORITY, 0, R.string.choose_sync_context_menu_one_change_priority);
			menu.add(getGroupId(), MENU_ID_CHANGE_NOTE, 0, R.string.choose_sync_context_menu_one_change_note);
		}
	}

	public boolean doContextItemSelectedForGroup(final MenuItem menuItem) {
		Log.d(getClass().getName(), "doContextItemSelectedForGroup : menuItem = " + menuItem);

		final MonsterInfoModel monsterInfo = getGroupMonsterItem(menuItem.getMenuInfo());

		switch (menuItem.getItemId()) {
			case MENU_ID_SELECT_ALL:
				for (final ChooseSyncModelContainer<SyncedMonsterModel> monsterItem : getGroupChildMonsterItems(menuItem.getMenuInfo())) {
					monsterItem.setChosen(true);
				}
				notifyDataSetChanged();
				break;
			case MENU_ID_DESELECT_ALL:
				for (final ChooseSyncModelContainer<SyncedMonsterModel> monsterItem : getGroupChildMonsterItems(menuItem.getMenuInfo())) {
					monsterItem.setChosen(false);
				}
				notifyDataSetChanged();
				break;
			case MENU_ID_CHANGE_PRIORITY_ALL:
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				final String dialogTitle = getContext().getString(R.string.choose_sync_context_menu_all_change_priority_dialog_title, monsterInfo.getName());
				builder.setTitle(dialogTitle);
				final CharSequence[] priorities = buildPriorityList();

				builder.setSingleChoiceItems(priorities, -1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						Log.d(getClass().getName(), "onClick : i = " + i);
						final MonsterPriority priority = MonsterPriority.findByOrdinal(i);
						for (final ChooseSyncModelContainer<SyncedMonsterModel> monsterItem : getGroupChildMonsterItems(menuItem.getMenuInfo())) {
							monsterItem.getSyncedModel().getCapturedInfo().setPriority(priority);
						}

						dialogInterface.dismiss();
						notifyDataSetChanged();
					}
				});

				builder.create().show();
				break;
			case MENU_ID_CHANGE_NOTE_ALL:
				AlertDialog.Builder noteDialogBuilder = new AlertDialog.Builder(getContext());
				final String noteDialogTitle = getContext().getString(R.string.choose_sync_context_menu_all_change_note_dialog_title, monsterInfo.getName());
				noteDialogBuilder.setTitle(noteDialogTitle);

				final EditText input = new EditText(getContext());
				noteDialogBuilder.setView(input);
				noteDialogBuilder.setPositiveButton(R.string.choose_sync_context_menu_change_note_dialog_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Log.d(getClass().getName(), "onClick");
						String newNote = input.getText().toString().trim();
						for (final ChooseSyncModelContainer<SyncedMonsterModel> monsterItem : getGroupChildMonsterItems(menuItem.getMenuInfo())) {
							monsterItem.getSyncedModel().getCapturedInfo().setNote(newNote);
						}
						notifyDataSetChanged();
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
				addMonsterToIgnoreList(getGroupMonsterItem(menuItem.getMenuInfo()).getIdJP());
				notifyDataSetChanged();
				break;
			default:
		}

		return true;
	}

	public boolean doContextItemSelectedForChild(MenuItem menuItem) {
		Log.d(getClass().getName(), "doContextItemSelectedForChild : menuItem = " + menuItem);

		final ChooseSyncModelContainer<SyncedMonsterModel> monsterItem = getChildMonsterItem(menuItem.getMenuInfo());

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
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				final String dialogTitle = getContext().getString(R.string.choose_sync_context_menu_one_change_priority_dialog_title, monsterItem.getSyncedModel().getDisplayedMonsterInfo().getName());
				builder.setTitle(dialogTitle);
				final CharSequence[] priorities = new CharSequence[MonsterPriority.values().length];
				int selected = monsterItem.getSyncedModel().getCapturedInfo().getPriority().ordinal();

				for (MonsterPriority priority : MonsterPriority.values()) {
					priorities[priority.ordinal()] = getContext().getString(priority.getLabelResId());
				}

				builder.setSingleChoiceItems(priorities, selected, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						Log.d(getClass().getName(), "onClick : i = " + i);
						final MonsterPriority priority = MonsterPriority.findByOrdinal(i);
						monsterItem.getSyncedModel().getCapturedInfo().setPriority(priority);
						dialogInterface.dismiss();
						notifyDataSetChanged();
					}
				});

				builder.create().show();
				break;
			case MENU_ID_CHANGE_NOTE:
				AlertDialog.Builder noteDialogBuilder = new AlertDialog.Builder(getContext());
				final String noteDialogTitle = getContext().getString(R.string.choose_sync_context_menu_one_change_note_dialog_title, monsterItem.getSyncedModel().getDisplayedMonsterInfo().getName());
				noteDialogBuilder.setTitle(noteDialogTitle);

				final EditText input = new EditText(getContext());
				input.setText(monsterItem.getSyncedModel().getCapturedInfo().getNote());
				noteDialogBuilder.setView(input);
				noteDialogBuilder.setPositiveButton(R.string.choose_sync_context_menu_change_note_dialog_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Log.d(getClass().getName(), "onClick");
						String newNote = input.getText().toString().trim();
						monsterItem.getSyncedModel().getCapturedInfo().setNote(newNote);
						notifyDataSetChanged();
					}
				});

				noteDialogBuilder.setNegativeButton(R.string.choose_sync_context_menu_change_note_dialog_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});
				noteDialogBuilder.create().show();

				break;
			default:
		}

		return true;
	}

	private boolean isGroup(ContextMenu.ContextMenuInfo menuInfo) {
		ExpandableListView.ExpandableListContextMenuInfo listItem = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
		int childPosition = ExpandableListView.getPackedPositionChild(listItem.packedPosition);
		return childPosition == -1;
	}

	private MonsterInfoModel getGroupMonsterItem(ContextMenu.ContextMenuInfo menuInfo) {
		Log.d(getClass().getName(), "getGroupMonsterItem : " + menuInfo);
		ExpandableListView.ExpandableListContextMenuInfo listItem = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

		int groupPosition = ExpandableListView.getPackedPositionGroup(listItem.packedPosition);

		return adapter.getGroup(groupPosition);
	}

	@SuppressWarnings("unchecked")
	private List<ChooseSyncModelContainer<SyncedMonsterModel>> getGroupChildMonsterItems(ContextMenu.ContextMenuInfo menuInfo) {
		Log.d(getClass().getName(), "getGroupChildMonsterItems : " + menuInfo);
		ExpandableListView.ExpandableListContextMenuInfo listItem = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

		int groupPosition = ExpandableListView.getPackedPositionGroup(listItem.packedPosition);

		final List<ChooseSyncModelContainer<SyncedMonsterModel>> monsterItems = new ArrayList<ChooseSyncModelContainer<SyncedMonsterModel>>();
		final int childCount = adapter.getChildrenCount(groupPosition);

		for (int i = 0; i < childCount; i++) {
			final ChooseSyncModelContainer<SyncedMonsterModel> monsterItem = adapter.getChild(groupPosition, i);
			monsterItems.add(monsterItem);
		}

		return monsterItems;
	}

	@SuppressWarnings("unchecked")
	private ChooseSyncModelContainer<SyncedMonsterModel> getChildMonsterItem(ContextMenu.ContextMenuInfo menuInfo) {
		Log.d(getClass().getName(), "getChildMonsterItem : " + menuInfo);
		ExpandableListView.ExpandableListContextMenuInfo listItem = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

		int groupPosition = ExpandableListView.getPackedPositionGroup(listItem.packedPosition);
		int childPosition = ExpandableListView.getPackedPositionChild(listItem.packedPosition);

		return adapter.getChild(groupPosition, childPosition);
	}

	protected void notifyDataSetChanged() {
		adapter.refreshData();
		adapter.notifyDataSetChanged();
	}
}
