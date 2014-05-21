
package fr.neraud.padlistener.constant;

public enum ScriptAsset {

	ENABLE_IPTABLES("enable_iptables.sh"),
	DISABLE_IPTABLES("disable_iptables.sh");

	private final String scriptName;

	private ScriptAsset(String scriptName) {
		this.scriptName = scriptName;
	}

	public String getScriptName() {
		return scriptName;
	}

}
