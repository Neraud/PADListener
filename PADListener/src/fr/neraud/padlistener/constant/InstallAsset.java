
package fr.neraud.padlistener.constant;

public enum InstallAsset {

	MONSTER_INFO("monsters.json"),
	MONSTER_IMAGES_DIR("monster_images");

	private final String fileName;

	private InstallAsset(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

}
