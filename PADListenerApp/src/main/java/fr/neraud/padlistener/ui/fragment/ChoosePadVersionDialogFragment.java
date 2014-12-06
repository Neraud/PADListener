package fr.neraud.padlistener.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.PADVersion;
import fr.neraud.padlistener.ui.adapter.ChoosePadVersionAdapter;

/**
 * Created by Neraud on 25/11/2014.
 */
public abstract class ChoosePadVersionDialogFragment extends DialogFragment {

	@InjectView(R.id.choose_pad_version_list)
	ListView mList;

	public ChoosePadVersionDialogFragment() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		MyLog.entry();

		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final LayoutInflater inflater = getActivity().getLayoutInflater();

		builder.setTitle(R.string.choose_pad_version_title);

		final View view = inflater.inflate(R.layout.choose_pad_version_fragment, null);
		ButterKnife.inject(this, view);

		final ChoosePadVersionAdapter adapter = new ChoosePadVersionAdapter(getActivity());
		mList.setAdapter(adapter);

		builder.setView(view);
		builder.setNegativeButton(R.string.choose_pad_version_cancel, null);
		final Dialog dialog = builder.create();

		MyLog.exit();
		return dialog;
	}

	@OnItemClick(R.id.choose_pad_version_list)
	@SuppressWarnings("unused")
	void onItemClick(int position) {
		MyLog.entry();
		final PADVersion chosenVersion = (PADVersion) mList.getAdapter().getItem(position);
		handlePadVersionChosen(chosenVersion);
		dismiss();
		MyLog.exit();
	}

	protected abstract void handlePadVersionChosen(PADVersion chosenVersion);
}
