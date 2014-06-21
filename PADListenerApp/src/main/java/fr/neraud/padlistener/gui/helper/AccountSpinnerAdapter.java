
package fr.neraud.padlistener.gui.helper;

import java.util.Map;

import android.content.Context;
import android.util.SparseIntArray;
import android.widget.ArrayAdapter;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;

/**
 * Adapter for the account Spinner on the ComputeSyncFragment
 * 
 * @author Neraud
 */
public class AccountSpinnerAdapter extends ArrayAdapter<String> {

	private final Map<Integer, String> accounts;
	private final SparseIntArray accountIdsByPosition;

	public AccountSpinnerAdapter(Context context) {
		super(context, android.R.layout.simple_spinner_item);
		accounts = new DefaultSharedPreferencesHelper(context).getPadHerderAccounts();

		super.addAll(accounts.values());

		accountIdsByPosition = new SparseIntArray();
		final Integer[] accountIds = accounts.keySet().toArray(new Integer[0]);
		for (int i = 0; i < accountIds.length; i++) {
			accountIdsByPosition.put(i, accountIds[i]);
		}
	}

	@Override
	public long getItemId(int position) {
		return accountIdsByPosition.get(position);
	}

	public int getPositionById(int accountId) {
		return accountIdsByPosition.indexOfValue(accountId);
	}
}
