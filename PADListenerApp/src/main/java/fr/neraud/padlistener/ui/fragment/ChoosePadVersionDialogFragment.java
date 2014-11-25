package fr.neraud.padlistener.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.PADVersion;
import fr.neraud.padlistener.ui.adapter.ChoosePadVersionAdapter;

/**
 * Created by Neraud on 25/11/2014.
 */
public abstract class ChoosePadVersionDialogFragment extends DialogFragment {

	public ChoosePadVersionDialogFragment() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateDialog");
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final LayoutInflater inflater = getActivity().getLayoutInflater();

		builder.setTitle(R.string.choose_pad_version_title);

		final View view = inflater.inflate(R.layout.choose_pad_version_fragment, null);

		final ListView list = (ListView) view.findViewById(R.id.choose_pad_version_list);
		final ChoosePadVersionAdapter adapter = new ChoosePadVersionAdapter(getActivity());
		list.setAdapter(adapter);

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Log.d(getClass().getName(), "onItemClick : " + position);
				final PADVersion chosenVersion = (PADVersion) list.getAdapter().getItem(position);
				handlePadVersionChosen(chosenVersion);
				dismiss();
			}
		});

		builder.setView(view);
		builder.setNegativeButton(R.string.choose_pad_version_cancel, null);
		return builder.create();
	}

	protected abstract void handlePadVersionChosen(PADVersion chosenVersion);
}
