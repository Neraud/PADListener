package fr.neraud.padlistener.gui.fragment;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.text.InputType;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;

/**
 * PreferenceFragment handling padherder accounts
 *
 * @author Neraud
 */
public class PADherderAccountsPreferenceFragment extends PreferenceFragment {

	private int numberOfAccounts;
	private Map<Integer, Preference> accountsByPosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preference_fragment_padherder);

		getPreferenceScreen().getPreference(0).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object o) {
				Log.d(getClass().getName(), "onPreferenceChange : " + o);
				if ("padherder_account_number".equals(preference.getKey())) {
					final String newNumberOfAccountsString = (String) o;
					final int newNumberOfAccounts = StringUtils.isNotBlank(newNumberOfAccountsString) ? Integer.parseInt(newNumberOfAccountsString) : 3;

					if (newNumberOfAccounts < numberOfAccounts) {
						for (int i = newNumberOfAccounts + 1; i <= numberOfAccounts; i++) {
							removePreferencesForOneAccount(i);
						}
					} else if (newNumberOfAccounts > numberOfAccounts) {
						for (int i = numberOfAccounts + 1; i <= newNumberOfAccounts; i++) {
							addPreferencesForOneAccount(i);
						}
					}

					numberOfAccounts = newNumberOfAccounts;
					return true;
				}
				return false;
			}
		});
		accountsByPosition = new HashMap<Integer, Preference>();
		numberOfAccounts = new DefaultSharedPreferencesHelper(getActivity()).getPadHerderAccountNumber();

		for (int i = 1; i <= numberOfAccounts; i++) {
			addPreferencesForOneAccount(i);
		}
	}

	private void addPreferencesForOneAccount(int accountId) {
		Log.d(getClass().getName(), "addPreferencesForOneAccount : " + accountId);
		final PreferenceCategory accountCategory = new PreferenceCategory(getActivity());
		accountCategory.setTitle(getString(R.string.settings_padherder_account_category, accountId));
		accountsByPosition.put(accountId, accountCategory);
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

	private void removePreferencesForOneAccount(int accountId) {
		final Preference category = accountsByPosition.get(accountId);
		getPreferenceScreen().removePreference(category);
		accountsByPosition.remove(accountId);
	}
}
