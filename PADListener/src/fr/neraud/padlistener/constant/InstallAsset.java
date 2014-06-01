
package fr.neraud.padlistener.constant;

/**
 * Enum of assets used in the install phase
 * 
 * @author Neraud
 */
public enum InstallAsset {

	MONSTER_INFO("monsters.json"),
	MONSTER_INFO_DATE("monsters.date"),
	MONSTER_IMAGES_DIR("monster_images"),
	MONSTER_IMAGES_DATE("monster_images.date");

	private final String fileName;

	private InstallAsset(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

}
