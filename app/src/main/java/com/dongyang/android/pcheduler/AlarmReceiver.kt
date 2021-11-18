package com.dongyang.android.pcheduler

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-18
 * @Description :
 */

class AlarmReceiver : BroadcastReceiver() {

    val NOTIFICATION_CHANNEL_ID = "alarm_channel"
    val NOTIFICATION_CHANNEL_NAME = "My Alarm"


    override fun onReceive(context: Context, intent: Intent?) {

        val intent = Intent(context, MainActivity::class.java)
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val importance = NotificationManager.IMPORTANCE_HIGH

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager, context)
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle("알람 테스트")
            .setContentText("테스트 입니둥 ㅎㅎ")
            .setAutoCancel(true) // 알림 클릭 시 알림 제거 여부
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(importance, notification)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(notificationManager: NotificationManager, context: Context) {
        val descriptionText = "my alarm"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            importance
        ).apply {
            enableLights(true)
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }
}
