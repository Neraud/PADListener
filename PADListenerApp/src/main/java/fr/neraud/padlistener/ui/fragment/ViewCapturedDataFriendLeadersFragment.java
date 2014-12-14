package fr.neraud.padlistener.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.CapturedFriendModel;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendLeaderDescriptor;
import fr.neraud.padlistener.ui.adapter.CapturedFriendLeadersCursorAdapter;

/**
 * ViewCapturedData fragment for a friend's leader history
 *
 * @author Neraud
 */
public class ViewCapturedDataFriendLeadersFragment extends DialogFragment implements LoaderCallbacks<Cursor> {

	private static final String CAPTURE_FRIEND_MODEL_NAME = "capturedFriendFullInfoModel";

	private CapturedFriendLeadersCursorAdapter mAdapter;
	private CapturedFriendModel mModel;

	@InjectView(R.id.view_captured_data_friend_leaders_list)
	ListView mListView;

	public static void fillCapturedFriendModel(Bundle args, CapturedFriendModel model) {
		args.putSerializable(CAPTURE_FRIEND_MODEL_NAME, model);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.entry();
		super.onCreate(savedInstanceState);
		mModel = (CapturedFriendModel) getArguments().getSerializable(CAPTURE_FRIEND_MODEL_NAME);
		MyLog.exit();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		MyLog.entry();

		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getString(R.string.view_captured_friend_leader_title, mModel.getName()));
		builder.setPositiveButton(getString(R.string.view_captured_friend_leader_close), null);

		final View view = getActivity().getLayoutInflater().inflate(R.layout.view_captured_data_fragment_friend_leaders, null);
		ButterKnife.inject(this, view);
		builder.setView(view);

		mAdapter = new CapturedFriendLeadersCursorAdapter(getActivity());
		mListView.setAdapter(mAdapter);
		getLoaderManager().initLoader(0, null, this);

		final Dialog dialog = builder.create();

		MyLog.exit();
		return dialog;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		MyLog.entry();
		final String selection = CapturedPlayerFriendLeaderDescriptor.Fields.PLAYER_ID.getColName() + "=" + mModel.getId();

		final Loader<Cursor> loader = new CursorLoader(getActivity(), CapturedPlayerFriendLeaderDescriptor.UriHelper.uriForAllWithInfo(),
				null, selection, null, CapturedPlayerFriendLeaderDescriptor.TABLE_NAME + "." + CapturedPlayerFriendLeaderDescriptor.Fields.LAST_SEEN.getColName());

		MyLog.exit();
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		MyLog.entry();
		mAdapter.swapCursor(data);
		MyLog.exit();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		MyLog.entry();
		mAdapter.swapCursor(null);
		MyLog.exit();
	}

}
