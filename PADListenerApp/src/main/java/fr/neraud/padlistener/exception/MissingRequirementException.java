package fr.neraud.padlistener.exception;

import fr.neraud.padlistener.R;

/**
 * Exception thrown when a requirement is missing
 *
 * @author Neraud
 */
public class MissingRequirementException extends Exception {

	private static final long serialVersionUID = 1L;

	public static enum Requirement {

		ROOT(R.string.switch_listener_settings_require_root);

		private final int errorTextResId;

		private Requirement(int errorTextResId) {
			this.errorTextResId = errorTextResId;
		}

		public int getErrorTextResId() {
			return errorTextResId;
		}
	}

	private final Requirement requirement;

	public MissingRequirementException(Requirement requirement) {
		super("Missing requirement " + requirement);
		this.requirement = requirement;
	}

	public Requirement getRequirement() {
		return requirement;
	}
}
