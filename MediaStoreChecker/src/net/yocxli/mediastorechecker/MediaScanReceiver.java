package net.yocxli.mediastorechecker;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MediaScanReceiver extends BroadcastReceiver {
    private static final String TAG = "MediaScanReceiver";
    private static final boolean LOCAL_LOGV = false;
    
    private static final int NOTIFICATION_ID = 4444;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (LOCAL_LOGV) {
            Log.v(TAG, "[onReceiver] " + intent.getAction());
        }
        
        final String action = intent.getAction();
        final Uri data = intent.getData();
        
        final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final Resources res = context.getResources();
        if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)) {
            final String title = res.getString(R.string.notification_scan_started);
            manager.notify(NOTIFICATION_ID, createNotification(context, title, data.getPath()));
        } else if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
            final String title = res.getString(R.string.notification_scan_completed);
            manager.notify(NOTIFICATION_ID, createNotification(context, title, data.getPath()));
        }
    }
    
    public static void updateNotification(Context context, Notification notification) {
        final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);
    }

    public static Notification createNotification(Context context, String title, String msg) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setTicker(title);
        return builder.build();
    }
}
