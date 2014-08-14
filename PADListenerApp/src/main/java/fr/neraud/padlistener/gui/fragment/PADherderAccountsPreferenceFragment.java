package fr.neraud.padlistener.gui.fragment;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.text.InputType;
import android.util.Log;

import fr.neraud.padlistener.R;

/**
 * PreferenceFragment handling padherder accounts
 *
 * @author Neraud
 */
public class PADherderAccountsPreferenceFragment extends PreferenceFragment {

	public static final int MAX_PADHERDER_ACCOUNTS = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preference_fragment_padherder);

		for (int i = 1; i <= MAX_PADHERDER_ACCOUNTS; i++) {
			addPreferencesForOneAccount(i);
		}
	}

	private void addPreferencesForOneAccount(int accountId) {
		Log.d(getClass().getName(), "addPreferencesForOneAccount : " + accountId);
		final PreferenceCategory accountCategory = new PreferenceCategory(getActivity());
		accountCategory.setTitle(getString(R.string.settings_padherder_account_category, accountId));
		getPreferenceScreen().addPreference(accountCategory);

		final EditTextPreference accountLogin = new EditTextPreference(getActivity());
		accountLogin.setKey("padherder_login_" + accountId);
		accountLogin.setTitle(R.string.settings_padherder_login_title);
		accountLogin.setDialogTitle(R.string.settings_padherder_login_title);
		accountLogin.setSummary(R.string.settings_padherder_login_summary);
		accountCategory.addPreference(accountLogin);

		final EditTextPreference accountPassword = new EditTextPreference(getActivity());
		accountPassword.setKey("padherder_password_" + accountId);
		accountPassword.setTitle(R.string.settings_padherder_password_title);
		accountPassword.setDialogTitle(R.string.settings_padherder_password_title);
		accountPassword.setSummary(R.string.settings_padherder_password_summary);
		accountPassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		accountCategory.addPreference(accountPassword);
	}

}
