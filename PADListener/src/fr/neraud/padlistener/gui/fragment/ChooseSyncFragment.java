
package fr.neraud.padlistener.gui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fr.neraud.padlistener.gui.helper.ChooseSyncDataPagerHelper;
import fr.neraud.padlistener.helper.ChooseSyncInitHelper;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.ComputeSyncResultModel;

/**
 * Main fragment for ChooseSync
 * 
 * @author Neraud
 */
public class ChooseSyncFragment extends AbstractViewPagerFragment {

	public static final String EXTRA_SYNC_RESULT_NAME = "sync_result";
	private ChooseSyncDataPagerHelper helper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final ComputeSyncResultModel result = (ComputeSyncResultModel) getActivity().getIntent().getExtras()
		        .getSerializable(EXTRA_SYNC_RESULT_NAME);

		final ChooseSyncInitHelper initHelper = new ChooseSyncInitHelper(result);
		final ChooseSyncModel syncModel = initHelper.filterSyncResult();

		helper = new ChooseSyncDataPagerHelper(getActivity(), syncModel);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected int getPageCount() {
		return helper.getCount();
	}

	@Override
	protected Fragment getPageFragment(int position) {
		return helper.createFragment(position);
	}

	@Override
	protected Integer getTabTitle(int position) {
		return helper.getTitle(position);
	}

}
