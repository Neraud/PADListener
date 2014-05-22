
package fr.neraud.padlistener.service;

import java.io.IOException;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import fr.neraud.padlistener.util.MonsterImageAssetHelper;

public class InstallMonsterImagesService extends IntentService {

	public InstallMonsterImagesService() {
		super("InstallMonsterInfoService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(getClass().getName(), "onHandleIntent");
		final MonsterImageAssetHelper helper = new MonsterImageAssetHelper(getApplicationContext());
		try {
			helper.copyMonsterImagesFromAssets();
			Log.d(getClass().getName(), "onHandleIntent : images copied");
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
