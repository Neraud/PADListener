package fr.neraud.padlistener.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import fr.neraud.padlistener.constant.InstallAsset;

/**
 * Helper to copy the monster images bundled in the assets
 *
 * @author Neraud
 */
public class MonsterImageAssetHelper extends AbstractAssetHelper {

	public MonsterImageAssetHelper(Context context) {
		super(context);
	}

	public void copyMonsterImagesFromAssets() throws IOException {
		Log.d(getClass().getName(), "copyMonsterImagesFromAssets");

		final String targetImageDir = getContext().getFilesDir().getPath() + "/monster_images/";
		new File(targetImageDir).mkdirs();

		final String[] imageNames = getContext().getAssets().list(InstallAsset.MONSTER_IMAGES_DIR.getFileName());

		for (final String imageName : imageNames) {
			Log.d(getClass().getName(), "copyMonsterImagesFromAssets : imageName = " + imageName);
			copyAsset(InstallAsset.MONSTER_IMAGES_DIR.getFileName() + "/" + imageName, targetImageDir + "/" + imageName);
		}
	}
}
