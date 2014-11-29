package fr.neraud.padlistener.constant;

/**
 * Enum of executable assets
 *
 * @author Neraud
 */
public enum ExecutableAsset {

	IPTABLES("iptables_armv7_pie", "iptables");

	private final String assetName;
	private final String targetName;

	private ExecutableAsset(String assetName, String targetName) {
		this.assetName = assetName;
		this.targetName = targetName;
	}

	public String getAssetName() {
		return assetName;
	}

	public String getTargetName() {
		return targetName;
	}
}
