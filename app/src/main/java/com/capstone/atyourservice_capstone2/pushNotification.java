package com.capstone.atyourservice_capstone2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class pushNotification {

    private static final String CHANNEL_ID="urservice_channel";
    private static final String CHANNEL_NAME="urservice";
    private static final String CHANNEL_DESC="urservice notifications";



    public void displayNotification(Context context, String title,String text){



        NotificationCompat.Builder mBuilder=
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.final_logo_removedbackground)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notifCompat= NotificationManagerCompat.from(context);
        notifCompat.notify(1, mBuilder.build());

    }
}
