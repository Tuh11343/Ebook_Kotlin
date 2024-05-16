package com.example.connectnodejs.utils

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.connectnodejs.MainActivity
import com.example.connectnodejs.R


class AlarmReceiver() : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "alarmChannel"
        const val INTENT_ACTION = "OPENFROMNOTIFICATION"
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        //Tien hanh tao moi intent de goi toi activity
        val putIntent = Intent(context, MainActivity::class.java)
        putIntent.action = INTENT_ACTION

        val resultPendingIntent = PendingIntent.getActivity(
            context, 0, putIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        //Tien hanh tao thanh thong bao
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
        builder.setContentTitle("Alarming")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText("Đã đến giờ đọc sách rồi")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(resultPendingIntent)
            .setSound(alarmSound)

        //Tien hanh thong bao
        val notificationManager = NotificationManagerCompat.from(context)
        try {
            notificationManager.notify(123, builder.build())
        } catch (er: SecurityException) {
            Log.e("ERROR", "Error from alarmReceiver:$er")
        }




    }


}