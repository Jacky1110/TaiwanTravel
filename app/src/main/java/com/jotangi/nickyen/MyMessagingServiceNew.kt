package com.jotangi.nickyen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

/**
/\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/11/18
\/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
class MyMessagingServiceNew : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        //喚醒手機
//        wakeUpPhone()

        //O以上需要添加channel
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("msg", "消息", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        //點擊跳轉畫面
        val pendingIntent =
            PendingIntent.getActivity(this,
                0,
                Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                , PendingIntent.FLAG_ONE_SHOT)

        //通知欄
        val notification = NotificationCompat.Builder(this, "msg")
            .setContentTitle(remoteMessage.notification?.title) //Data收到
            .setContentText(remoteMessage.notification?.body) //Data收到
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(Random.nextInt(5), notification)
    }

    //第一次取得Token,後續取不到
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

//    private fun wakeUpPhone() {
//        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
//        val wl = powerManager.newWakeLock(
//            PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK,
//            "bright")
//        //開啟螢幕(10分鐘)
//        wl.acquire(10 * 60 * 1000L)
//        //釋放資源
//        wl.release()
//    }
}