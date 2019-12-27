package net.smiguel.app.config.sync;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import net.smiguel.app.App;
import net.smiguel.app.config.repository.SyncLocalOperation;
import net.smiguel.app.constants.AppConstants;

public abstract class SyncLifecycleObserver<T> implements LifecycleObserver, SyncLocalOperation<T> {

    private NotificationManager mNotificationManager;

    public SyncLifecycleObserver() {
        mNotificationManager = (NotificationManager) App.getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public abstract void onResume();

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public abstract void onPause();

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public abstract void onDestroy();

    public abstract void showNotification(T item, Throwable throwable);

    public void showNotification(String channelId, String channelName, String title, String description) {
        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = mNotificationManager.getNotificationChannel(channelId);
            if (mChannel == null) {
                mChannel = new NotificationChannel(channelId, channelName, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mNotificationManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(App.getApplication().getApplicationContext(), channelId);

            builder.setContentTitle(title)
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(description)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setTicker(description)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {
            builder = new NotificationCompat.Builder(App.getApplication().getApplicationContext(), channelId);

            builder.setContentTitle(title)
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)
                    .setContentText(description)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setTicker(description)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_DEFAULT);
        }

        Notification notification = builder.build();
        mNotificationManager.notify(AppConstants.View.NOTIFICATION_ID, notification);
    }
}
