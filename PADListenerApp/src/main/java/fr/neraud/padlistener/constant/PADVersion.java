package fr.neraud.padlistener.constant;

public enum PADVersion {

	US(PADRegion.US, "api-na-adr.padsv.gungho.jp", "jp.gungho.padEN"),
	JP(PADRegion.JP, "api-adr.padsv.gungho.jp", "jp.gungho.pad"),
	HK_TW(PADRegion.HK_TW, "api-ht-adr.padsv.gungho.jp", "jp.gungho.padHT"),
	KO(PADRegion.KO, "api-kr-adr.padsv.gungho.jp", "jp.gungho.padKO"),
	AMAZON_US(PADRegion.US, "api-na-amz.padsv.gungho.jp", "jp.gungho.padKINEN");

	private final PADRegion region;
	private final String serverHostName;
	private final String applicationPackage;

	private PADVersion(PADRegion region, String serverHostName, String applicationPackage) {
		this.region = region;
		this.serverHostName = serverHostName;
		this.applicationPackage = applicationPackage;
	}

	public PADRegion getRegion() {
		return region;
	}

	public String getServerHostName() {
		return serverHostName;
	}

	public String getApplicationPackage() {
		return applicationPackage;
	}

	public static PADVersion fromName(String name) {
		for(final PADVersion server : values()) {
			if(server.name().equals(name)) {
				return server;
			}
		}
		return null;
	}

	public static PADVersion fromHostName(String hostName) {
		for(final PADVersion server : values()) {
			if(server.getServerHostName().equals(hostName)) {
				return server;
			}
		}
		return null;
	}
}
