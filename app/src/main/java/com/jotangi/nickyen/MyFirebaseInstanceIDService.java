package com.jotangi.nickyen;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/10/4
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class MyFirebaseInstanceIDService extends FirebaseMessagingService
{
    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null)
        {
            Log.i("MyFirebaseService", "title: " + remoteMessage.getNotification().getTitle());
            Log.i("MyFirebaseService", "body: " + remoteMessage.getNotification().getBody());

        }
        //need
        new NotificationMethod(this)
                .sendNormalNotification(
                        remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), R.mipmap.ic_launcher_round, null);
    }

    @Override
    public void onNewToken(@NonNull @NotNull String s)
    {
        super.onNewToken(s);
        Log.i("MyFirebaseService", "onNewToken: "+ s);
    }
}
