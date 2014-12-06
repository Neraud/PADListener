package fr.neraud.padlistener.ui.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.SyncMode;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.ui.fragment.ChooseSyncMaterialsFragment;
import fr.neraud.padlistener.ui.fragment.ChooseSyncMonstersGroupedFragment;
import fr.neraud.padlistener.ui.fragment.ChooseSyncMonstersSimpleFragment;

/**
 * Helper for the ChooseSync
 *
 * @author Neraud
 */
public class ChooseSyncDataPagerHelper {

	public static final String ARGUMENT_SYNC_RESULT_NAME = "result";
	public static final String ARGUMENT_SYNC_MODE_NAME = "mode";
	private final ChooseSyncModel mResult;
	private final Map<Integer, Fragment> mFragmentsByPosition;
	private final Map<Integer, Integer> mTitlesByPosition;
	private final DefaultSharedPreferencesHelper mPrefHelper;
	private int mCount = 0;

	@SuppressLint("UseSparseArrays")
	public ChooseSyncDataPagerHelper(Context context, ChooseSyncModel result) {
		this.mResult = result;
		mFragmentsByPosition = new HashMap<Integer, Fragment>();
		mTitlesByPosition = new HashMap<Integer, Integer>();
		mPrefHelper = new DefaultSharedPreferencesHelper(context);

		populate();
	}

	private void populate() {
		MyLog.entry();
		populateMaterialsUpdated();
		populateMonsterFragment(SyncMode.UPDATED, R.string.choose_sync_monstersUpdated_label);
		populateMonsterFragment(SyncMode.CREATED, R.string.choose_sync_monstersCreated_label);
		populateMonsterFragment(SyncMode.DELETED, R.string.choose_sync_monstersDeleted_label);
		MyLog.exit();
	}

	private void populateMaterialsUpdated() {
		MyLog.entry();
		if (mResult.getSyncedMaterialsToUpdate().size() > 0) {
			final Fragment fragment = new ChooseSyncMaterialsFragment();
			addResultToFragmentArguments(fragment);
			mFragmentsByPosition.put(mCount, fragment);
			mTitlesByPosition.put(mCount, R.string.choose_sync_materialsUpdated_label);
			mCount++;
		}
		MyLog.exit();
	}

	private void populateMonsterFragment(SyncMode mode, int titleId) {
		MyLog.entry("mode = " + mode);
		if (mResult.getSyncedMonsters(mode).size() > 0) {
			final Fragment fragment = createChoiceFragment(mPrefHelper.isChooseSyncGroupMonsters(mode));
			addResultToFragmentArguments(fragment);
			addModeToFragmentArguments(fragment, mode);
			mFragmentsByPosition.put(mCount, fragment);
			mTitlesByPosition.put(mCount, titleId);
			mCount++;
		}
		MyLog.exit();
	}

	private Fragment createChoiceFragment(boolean isGrouped) {
		if (isGrouped) {
			return new ChooseSyncMonstersGroupedFragment();
		} else {
			return new ChooseSyncMonstersSimpleFragment();
		}
	}

	private void addResultToFragmentArguments(Fragment fragment) {
		if (fragment.getArguments() == null) {
			fragment.setArguments(new Bundle());
		}
		fragment.getArguments().putSerializable(ARGUMENT_SYNC_RESULT_NAME, mResult);
	}

	private void addModeToFragmentArguments(Fragment fragment, SyncMode mode) {
		if (fragment.getArguments() == null) {
			fragment.setArguments(new Bundle());
		}
		fragment.getArguments().putSerializable(ARGUMENT_SYNC_MODE_NAME, mode.name());
	}

	public int getCount() {
		return mCount;
	}

	public Fragment createFragment(int position) {
		return mFragmentsByPosition.get(position);
	}

	public Integer getTitle(int position) {
		return mTitlesByPosition.get(position);
	}

}
