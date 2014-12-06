package fr.neraud.padlistener.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.helper.MonsterInfoHelper;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;
import fr.neraud.padlistener.provider.helper.MonsterInfoProviderHelper;

/**
 * Created by Neraud on 17/08/2014.
 */
public class ManageIgnoreListTaskFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	public static final String TAG_TASK_FRAGMENT = "manage_ignore_list_task_fragment";
	private MonsterInfoHelper monsterInfoHelper = null;
	private Set<Integer> ignoredIds = null;

	private ManageIgnoreListViewListFragment listFragment;
	private ManageIgnoreListQuickActionsFragment quickActionsFragment;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.entry();
		super.onCreate(savedInstanceState);

		ignoredIds = new HashSet<Integer>();
		final DefaultSharedPreferencesHelper helper = new DefaultSharedPreferencesHelper(getActivity());
		ignoredIds.addAll(helper.getMonsterIgnoreList());

		getLoaderManager().initLoader(0, null, this);

		setRetainInstance(true);
		MyLog.exit();
	}

	@Override
	public void onDetach() {
		MyLog.entry();
		super.onDetach();
		listFragment = null;
		quickActionsFragment = null;
		MyLog.exit();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		MyLog.entry();
		final Loader<Cursor> loader = new CursorLoader(getActivity(), MonsterInfoDescriptor.UriHelper.uriForAll(), null, null, null, null);
		MyLog.exit();
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		MyLog.entry();

		final Map<Integer, MonsterInfoModel> monsterInfoById = new HashMap<Integer, MonsterInfoModel>();

		while (data.moveToNext()) {
			final MonsterInfoModel model = MonsterInfoProviderHelper.cursorToModel(data);
			monsterInfoById.put(model.getIdJP(), model);
		}

		monsterInfoHelper = new MonsterInfoHelper(monsterInfoById);

		refreshAdapters();

		MyLog.exit();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		MyLog.entry();
		MyLog.exit();
	}

	public Set<Integer> getIgnoredIds() {
		return ignoredIds;
	}

	public void registerListFragment(ManageIgnoreListViewListFragment listFragment) {
		MyLog.entry();
		this.listFragment = listFragment;

		if (listFragment != null && monsterInfoHelper != null) {
			listFragment.refreshAdapter(monsterInfoHelper, ignoredIds);
		}
		MyLog.exit();
	}

	public void registerQuickActionsFragment(ManageIgnoreListQuickActionsFragment quickActionsFragment) {
		MyLog.entry();
		this.quickActionsFragment = quickActionsFragment;
		MyLog.exit();
	}

	public void clearIgnoredIds() {
		MyLog.entry();
		ignoredIds.clear();

		new DefaultSharedPreferencesHelper(getActivity()).setMonsterIgnoreList(ignoredIds);
		refreshAdapters();
		MyLog.exit();
	}

	public void removeIgnoredIds(Integer... ids) {
		MyLog.entry("ids = " + Arrays.toString(ids));
		for (final Integer id : ids) {
			ignoredIds.remove(id);
		}

		new DefaultSharedPreferencesHelper(getActivity()).setMonsterIgnoreList(ignoredIds);
		refreshAdapters();
		MyLog.exit();
	}

	public void addIgnoredIds(Integer... ids) {
		MyLog.entry("ids = " + Arrays.toString(ids));
		Collections.addAll(ignoredIds, ids);

		new DefaultSharedPreferencesHelper(getActivity()).setMonsterIgnoreList(ignoredIds);
		refreshAdapters();
		MyLog.exit();
	}

	public void refreshAdapters() {
		MyLog.entry();

		if (monsterInfoHelper != null) {
			if (listFragment != null) {
				listFragment.refreshAdapter(monsterInfoHelper, ignoredIds);
			}
			if (quickActionsFragment != null) {
				quickActionsFragment.refreshAdapter(monsterInfoHelper, ignoredIds);
			}
		}
		MyLog.exit();
	}

	public MonsterInfoHelper getMonsterInfoHelper() {
		return monsterInfoHelper;
	}
}
