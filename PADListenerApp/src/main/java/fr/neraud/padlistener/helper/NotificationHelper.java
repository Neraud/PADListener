package fr.neraud.padlistener.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Date;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.MyNotification;
import fr.neraud.padlistener.ui.activity.HomeActivity;

/**
 * Created by Neraud on 30/11/2014.
 */
public class NotificationHelper {

	private final Context mContext;
	private final MyNotification mNotification;
	private final int mTitleId;

	public NotificationHelper(Context context, MyNotification notification, int titleId) {
		this.mContext = context;
		this.mNotification = notification;
		this.mTitleId = titleId;
	}

	private NotificationCompat.Builder prepareNotification() {
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
		builder.setSmallIcon(R.drawable.ic_notification);
		builder.setColor(mContext.getResources().getColor(R.color.theme_primary));
		builder.setWhen(new Date().getTime());
		builder.setContentTitle(mContext.getString(mTitleId));

		final Intent notificationIntent = new Intent(mContext, HomeActivity.class);
		final PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
		builder.setContentIntent(pendingIntent);

		return builder;
	}

	private void showNotification(NotificationCompat.Builder builder) {
		final NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(mNotification.getId(), builder.build());
	}

	public void displayNotification(final String notificationMessage) {
		final NotificationCompat.Builder builder = prepareNotification();
		builder.setContentText(notificationMessage);
		showNotification(builder);
	}

	public void displayNotificationWithProgress(final String notificationMessage, int nb, int max) {
		final NotificationCompat.Builder builder = prepareNotification();
		builder.setContentText(notificationMessage);
		builder.setProgress(max, nb, false);
		showNotification(builder);
	}
}
