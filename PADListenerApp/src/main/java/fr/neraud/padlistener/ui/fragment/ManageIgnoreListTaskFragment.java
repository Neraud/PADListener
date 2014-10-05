package fr.neraud.padlistener.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;
import fr.neraud.padlistener.helper.MonsterInfoHelper;
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
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);

		ignoredIds = new HashSet<Integer>();
		final DefaultSharedPreferencesHelper helper = new DefaultSharedPreferencesHelper(getActivity());
		ignoredIds.addAll(helper.getMonsterIgnoreList());

		getLoaderManager().initLoader(0, null, this);

		setRetainInstance(true);
	}

	@Override
	public void onDetach() {
		Log.d(getClass().getName(), "onDetach");
		super.onDetach();
		listFragment = null;
		quickActionsFragment = null;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.d(getClass().getName(), "onCreateLoader");
		return new CursorLoader(getActivity(), MonsterInfoDescriptor.UriHelper.uriForAll(), null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(getClass().getName(), "onLoadFinished");
		final Map<Integer, MonsterInfoModel> monsterInfoById = new HashMap<Integer, MonsterInfoModel>();

		while (data.moveToNext()) {
			final MonsterInfoModel model = MonsterInfoProviderHelper.cursorToModel(data);
			monsterInfoById.put(model.getIdJP(), model);
		}

		monsterInfoHelper = new MonsterInfoHelper(monsterInfoById);

		refreshAdapters();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d(getClass().getName(), "onLoaderReset");
	}

	public Set<Integer> getIgnoredIds() {
		return ignoredIds;
	}

	public void registerListFragment(ManageIgnoreListViewListFragment listFragment) {
		Log.d(getClass().getName(), "registerListFragment : " + listFragment);
		this.listFragment = listFragment;

		if (listFragment != null && monsterInfoHelper != null) {
			listFragment.refreshAdapter(monsterInfoHelper, ignoredIds);
		}
	}

	public void registerQuickActionsFragment(ManageIgnoreListQuickActionsFragment quickActionsFragment) {
		Log.d(getClass().getName(), "registerQuickActionsFragment : " + quickActionsFragment);
		this.quickActionsFragment = quickActionsFragment;
	}

	public void clearIgnoredIds() {
		Log.d(getClass().getName(), "clearIgnoredIds");
		ignoredIds.clear();

		new DefaultSharedPreferencesHelper(getActivity()).setMonsterIgnoreList(ignoredIds);
		refreshAdapters();
	}

	public void removeIgnoredIds(Integer... ids) {
		Log.d(getClass().getName(), "removeIgnoredIds : " + Arrays.toString(ids));
		for (final Integer id : ids) {
			ignoredIds.remove(id);
		}

		new DefaultSharedPreferencesHelper(getActivity()).setMonsterIgnoreList(ignoredIds);
		refreshAdapters();
	}

	public void addIgnoredIds(Integer... ids) {
		Log.d(getClass().getName(), "addIgnoredIds : " + Arrays.toString(ids));
		Collections.addAll(ignoredIds, ids);

		new DefaultSharedPreferencesHelper(getActivity()).setMonsterIgnoreList(ignoredIds);
		refreshAdapters();
	}

	public void refreshAdapters() {
		Log.d(getClass().getName(), "refreshAdapters : " + ignoredIds);

		if(monsterInfoHelper != null) {
			if (listFragment != null) {
				listFragment.refreshAdapter(monsterInfoHelper, ignoredIds);
			}
			if (quickActionsFragment != null) {
				quickActionsFragment.refreshAdapter(monsterInfoHelper, ignoredIds);
			}
		}
	}

	public MonsterInfoHelper getMonsterInfoHelper() {
		return monsterInfoHelper;
	}
}
