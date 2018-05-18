package com.sport_ucl.Administrator;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.graphics.BitmapFactory;

import com.sport_ucl.R;

/**
 * Created by Nour-Eddine on 3/04/18.
 */

public class NotificationHelper extends ContextWrapper {


    /**
     * C'est ici qu'on va configurer la structure des notifications
     * qui seront envoyé aux utilisateurs enregistré à un event qui va changé.
     * Cette structure peut être utilisé pour une tout autre utilisation de
     * notification.
     */

    public static final String channel_id = "idChannel";
    public static final String channel_name = "nameChannel" ;
    private NotificationManager mManager ;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channel_id,channel_name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(R.color.colorPrimary);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(channel);
        }

    }

    public NotificationManager getManager(){
        if(mManager == null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        }
        return mManager ;
    }
    public NotificationCompat.Builder getChannelNotification (String title, String message){
        return new NotificationCompat.Builder(getApplicationContext(),channel_id)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));    }

}
