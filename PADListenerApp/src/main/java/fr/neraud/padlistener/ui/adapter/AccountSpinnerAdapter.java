package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.util.SparseIntArray;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.padlistener.model.PADHerderAccountModel;

/**
 * Adapter for the account Spinner on the ComputeSyncFragment
 *
 * @author Neraud
 */
public class AccountSpinnerAdapter extends ArrayAdapter<String> {

	private final SparseIntArray accountIdsByPosition;

	public AccountSpinnerAdapter(Context context, List<PADHerderAccountModel> accounts) {
		super(context, android.R.layout.simple_spinner_item);

		final List<String> labels = new ArrayList<String>();
		accountIdsByPosition = new SparseIntArray();
		int i = 0 ;
		for(final PADHerderAccountModel account : accounts)  {
			labels.add(account.getLogin());
			accountIdsByPosition.put(i, account.getAccountId());
			i++;
		}
		super.addAll(labels);
	}

	@Override
	public long getItemId(int position) {
		return accountIdsByPosition.get(position);
	}

	public int getPositionById(int accountId) {
		return accountIdsByPosition.indexOfValue(accountId);
	}
}
