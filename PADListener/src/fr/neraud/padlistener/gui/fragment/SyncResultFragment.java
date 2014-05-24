
package fr.neraud.padlistener.gui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.SyncResultModel;

public class SyncResultFragment extends Fragment {

	public static final String EXTRA_SYNC_RESULT_NAME = "sync_result";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View view = inflater.inflate(R.layout.fragment_sync_result, container, false);

		final SyncResultModel result = (SyncResultModel) getActivity().getIntent().getExtras()
		        .getSerializable(EXTRA_SYNC_RESULT_NAME);
		// TODO

		return view;
	}
}
