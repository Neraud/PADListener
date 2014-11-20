package fr.neraud.padlistener.constant;

public enum PADServer {

	US(PADRegion.US, "api-na-adr.padsv.gungho.jp"),
	JP(PADRegion.JP, "api-adr-pad.gungho.jp"),
	HK_TW(PADRegion.HK_TW, "api-ht-adr.padsv.gungho.jp"),
	AMAZON_US(PADRegion.US, "api-na-amz-pad.gungho.jp");

	private final PADRegion region;
	private final String hostName;

	private PADServer(PADRegion region, String hostName) {
		this.region = region;
		this.hostName = hostName;
	}

	public PADRegion getRegion() {
		return region;
	}

	public String getHostName() {
		return hostName;
	}

	public static PADServer fromName(String name) {
		for(final PADServer server : values()) {
			if(server.name().equals(name)) {
				return server;
			}
		}
		return null;
	}

	public static PADServer fromHostName(String hostName) {
		for(final PADServer server : values()) {
			if(server.getHostName().equals(hostName)) {
				return server;
			}
		}
		return null;
	}
}
