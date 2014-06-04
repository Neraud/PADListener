
package fr.neraud.padlistener.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.http.client.ImageDownloadClient;
import fr.neraud.padlistener.http.constant.HttpMethod;
import fr.neraud.padlistener.http.helper.PadHerderDescriptor;
import fr.neraud.padlistener.http.model.ImageDownloadResponse;
import fr.neraud.padlistener.http.model.MyHttpRequest;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;
import fr.neraud.padlistener.service.receiver.AbstractRestResultReceiver;

/**
 * Service used to refresh MonsterImages from PADherder
 * 
 * @author Neraud
 */
public class FetchPadHerderMonsterImageService extends IntentService {

	public FetchPadHerderMonsterImageService() {
		super("FetchPadHerderMonsterInfoService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(getClass().getName(), "onHandleIntent");

		final ResultReceiver receiver = intent.getParcelableExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME);

		final String[] selection = new String[] { MonsterInfoDescriptor.Fields.ID_JP.getColName(),
		        MonsterInfoDescriptor.Fields.IMAGE_60_URL.getColName() };

		final Cursor cursor = getContentResolver().query(MonsterInfoDescriptor.UriHelper.uriForAll(), selection, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			final int imagesToDownload = cursor.getCount() + 1;
			int imagesDownloaded = 0;
			do {
				final int monsterId = cursor.getInt(cursor.getColumnIndex(MonsterInfoDescriptor.Fields.ID_JP.getColName()));
				final String imageUrl = cursor.getString(cursor.getColumnIndex(MonsterInfoDescriptor.Fields.IMAGE_60_URL
				        .getColName()));
				notifyProgress(receiver, monsterId, imagesDownloaded, imagesToDownload, imageUrl);
				downloadImage(monsterId, imageUrl);

				imagesDownloaded++;

				/*
				if (imagesDownloaded >= 10) {
					// FIXME mock
					break;
				}
				*/
			} while (cursor.moveToNext());

			new TechnicalSharedPreferencesHelper(getApplicationContext()).setMonsterImagesRefreshDate(new Date());
			notifyFinished(receiver, imagesDownloaded);
		}
	}

	private void downloadImage(int monsterId, String imageUrl) {
		Log.d(getClass().getName(), "downloadImage : " + monsterId);

		InputStream in = null;
		OutputStream out = null;
		try {
			final ImageDownloadClient client = new ImageDownloadClient(getApplicationContext(), PadHerderDescriptor.serverUrl);
			final MyHttpRequest request = new MyHttpRequest();
			request.setMethod(HttpMethod.GET);
			request.setUrl(imageUrl);
			final ImageDownloadResponse response = client.call(request);
			in = response.getInputStream();
			if (response.isResponseOk()) {
				Log.d(getClass().getName(), "downloadImage : download sucess, saving");
				final Uri uri = MonsterInfoDescriptor.UriHelper.uriForImage(monsterId);
				out = getContentResolver().openOutputStream(uri);
				IOUtils.copy(in, out);
				Log.d(getClass().getName(), "downloadImage : saved");
			}
		} catch (final Exception e) {
			Log.e(getClass().getName(), "downloadImage : error downloading " + imageUrl, e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (final IOException e) {
				}
			}
		}
	}

	private void notifyProgress(ResultReceiver receiver, int monsterId, int imagesDownloaded, int imagesToDownload, String imageUrl) {
		Log.d(getClass().getName(), "notifyProgress : " + imagesDownloaded + " / " + imagesToDownload);
		if (receiver != null) {
			final Bundle bundle = new Bundle();
			bundle.putInt("monsterId", monsterId);
			bundle.putInt("imagesDownloaded", imagesDownloaded);
			bundle.putInt("imagesToDownload", imagesToDownload);
			bundle.putString("imageUrl", imageUrl);
			receiver.send(0, bundle);
		}
	}

	private void notifyFinished(ResultReceiver receiver, int imagesDownloaded) {
		Log.d(getClass().getName(), "notifyFinished : " + imagesDownloaded);
		if (receiver != null) {
			final Bundle bundle = new Bundle();
			bundle.putInt("imagesDownloaded", imagesDownloaded);
			receiver.send(1, bundle);
		}
	}
}
