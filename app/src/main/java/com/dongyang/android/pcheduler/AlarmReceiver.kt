package com.dongyang.android.pcheduler

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-18
 * @Description : 알람 설정 브로드캐스트 리시버
 */

class AlarmReceiver : BroadcastReceiver() {

    // ID는 별도로 빼놓음
    companion object {
        const val NOTIFICATION_ID = 100
        const val NOTIFICATION_CHANNEL_ID = "alarm_channel"
        const val NOTIFICATION_CHANNEL_NAME = "My Alarm"
    }

    // PendingIntent 를 받아서 처리하는 함수 -> Notification으로 처리
    override fun onReceive(context: Context, intent: Intent?) {

        val content: String = intent?.getStringExtra("content")!!
        val requestCode = intent.getIntExtra("id", NOTIFICATION_ID)


        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val importance = NotificationManager.IMPORTANCE_HIGH

        // 26버전 이상부터는 notification 처리를 위해 채널이 필요
        createNotificationChannel(context)
        notifyNotification(context, content, requestCode)


//        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_alarm)
//            .setContentTitle(content)
//            .setAutoCancel(true) // 알림 클릭 시 알림 제거 여부
//            .setContentIntent(pendingIntent)
//            .build()
//
//        notificationManager.notify(importance, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context) {
        // context -> 앱의 글로벌 정보, API, 시스템에 대한 정보, SharedPreferences, 리소스 파일등 기능들을 저장을 해 둔 접근할 때 필요한 객체
        // activity 자체가 리소스 접근이 용이하여 context라고 말할 수 있음,
        // 하지만 broadcastReceiver는 백그라운드에서 broadcast에서 pedning intent를 처리하므로 context를 받아와야함
        val descriptionText = "my alarm"
        val importance = NotificationManager.IMPORTANCE_HIGH

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                importance
            ).apply {
                enableLights(true)
                description = descriptionText
            }
            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }
    }

    private fun notifyNotification(context: Context, content: String, requestCode: Int) {

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent =
            PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)

        val ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(
            context,
            RingtoneManager.TYPE_NOTIFICATION
        )

        with(NotificationManagerCompat.from(context)) {
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(content)
                .setAutoCancel(true) // 알림 클릭 시 알림 제거 여부
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(ringtoneUri)
                .setContentIntent(pendingIntent)
            notify(requestCode, build.build())
        }
    }
}
