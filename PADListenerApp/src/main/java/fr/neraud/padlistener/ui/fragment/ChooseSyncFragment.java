package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.ui.activity.ChooseSyncActivity;
import fr.neraud.padlistener.ui.helper.ChooseSyncDataPagerHelper;

/**
 * Main fragment for ChooseSync
 *
 * @author Neraud
 */
public class ChooseSyncFragment extends AbstractViewPagerFragment {

	private ChooseSyncDataPagerHelper mHelper;
	private ChooseSyncModel mChooseResult;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final Bundle extras = getActivity().getIntent().getExtras();

		mChooseResult = (ChooseSyncModel) extras.getSerializable(ChooseSyncActivity.EXTRA_CHOOSE_SYNC_RESULT_NAME);
		Log.d(getClass().getName(), "onCreate : getting mChooseResult in extras (" + ChooseSyncActivity.EXTRA_CHOOSE_SYNC_RESULT_NAME + ") : " + mChooseResult);
		mHelper = new ChooseSyncDataPagerHelper(getActivity(), mChooseResult);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected int getPageCount() {
		return mHelper.getCount();
	}

	@Override
	protected Fragment getPageFragment(int position) {
		return mHelper.createFragment(position);
	}

	@Override
	protected Integer getTabTitle(int position) {
		return mHelper.getTitle(position);
	}

	@Override
	protected void notifyFragmentSelected(int position) {
	}

}
