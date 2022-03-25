package com.jotangi.nickyen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.member.LoginActivity;
import com.jotangi.nickyen.member.PhoneVerifyActivity;
import com.jotangi.nickyen.model.UserBean;

import java.io.UnsupportedEncodingException;

public class WelcomeActivity extends AppCompatActivity
{

    public static Uri uri;
    public static String storeID;
    public static String type;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));

        handler.postDelayed(runnable, 1250);

        uri = getIntent().getData();
        Log.d("豪豪", "onCreate: " + uri);

        if (uri != null)
        {

            String str = uri.getHost();
            String[] tokens = str.split("=|&");
            storeID = tokens[1];
            type = tokens[3];
            Log.d("豪豪", "onCreate: " + storeID + type);
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>()
                {
                    @Override
                    public void onComplete(@NonNull Task<String> task)
                    {
                        if (!task.isSuccessful())
                        {
                            //Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        UserBean.NOTIFICATION_TOKEN = token;
                        Log.d("豪豪", "FCM推播：" + token);
                    }
                });
    }

    Handler handler = new Handler();
    Runnable runnable = () ->
    {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    };

}