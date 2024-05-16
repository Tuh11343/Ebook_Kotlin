/*
package com.example.connectnodejs.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.ACTION_FAST_FORWARD
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE
import android.support.v4.media.session.PlaybackStateCompat.ACTION_REWIND
import android.support.v4.media.session.PlaybackStateCompat.ACTION_STOP
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.connectnodejs.R
import com.example.connectnodejs.viewmodels.SongViewModel


class SongNotification(private val context: Context, var activity: AppCompatActivity){

    private val notificationId = 1
    private val channelId = "channel_id"
    private var songViewModel = ViewModelProvider(activity)[SongViewModel::class.java]

    companion object {
        lateinit var mediaSession: MediaSessionCompat
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Channel Name"
            val channelDescription = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification() {

        mediaSession = MediaSessionCompat(context, "ExoPlayer")
        mediaSession.isActive = true
        mediaSession.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlay() {
                super.onPlay()
                songViewModel.updatePauseSong(true)
            }

            override fun onPause() {
                super.onPause()
                songViewModel.updatePauseSong(false)
            }

            override fun onRewind() {
                super.onRewind()
            }

            override fun onFastForward() {
                super.onFastForward()
            }
        })

        val playIntent = Intent(context, SongReceiver::class.java).apply {
            action = ACTION_PLAY_PAUSE.toString()
            putExtra("Play_Pause", songViewModel.isSongPlaying.value != true)
        }

        val playPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context,
            AppInstance.PLAY_PAUSE_REQUEST_CODE,
            playIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(mediaSession.sessionToken)
            .setShowCancelButton(true)
            .setShowActionsInCompactView(0,1,2,3)

        val playPauseIcon = if (songViewModel.isSongPlaying.value == true) {
            R.drawable.icon_pause // Biểu tượng pause nếu bài hát đang phát
        } else {
            R.drawable.icon_play // Biểu tượng play nếu bài hát đang tạm dừng
        }
        val playPauseAction = NotificationCompat.Action(
            playPauseIcon, // Sử dụng biểu tượng mới
            "Play",
            playPendingIntent
        )

        val metadata = MediaMetadataCompat.Builder()
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, songViewModel.totalTime.value!!)
            .build()
        mediaSession.setMetadata(metadata)

        // Add this to set the playback state for the media session
        val stateBuilder = PlaybackStateCompat.Builder()
            .setActions(ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_SEEK_TO)
            .setState(PlaybackStateCompat.STATE_PLAYING,  0, 1f)
        mediaSession.setPlaybackState(stateBuilder.build())


        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.song_circle)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(songViewModel.songName.value)
            .setContentText(songViewModel.songAuthor.value)
            .setSmallIcon(R.drawable.icon_favorite)
            .setLargeIcon(bitmap)
            .addAction(getRewindAction())
            .addAction(playPauseAction)
            .addAction(getFFAction())
            .addAction(getCloseAction())
            .setStyle(mediaStyle)
            .build()



            // Hiển thị notification
            val notificationManager = NotificationManagerCompat.from(context)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notificationManager.notify(notificationId, notification)
    }

    fun cancelNotification() {
        mediaSession.release()
        val stateBuilder = PlaybackStateCompat.Builder()
            .setActions(0)
            .setState(PlaybackStateCompat.STATE_STOPPED, 0, 1f);
        mediaSession.setPlaybackState(stateBuilder.build());

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(notificationId)
    }

    private fun getRewindAction(): NotificationCompat.Action {
        val rewindIntent = Intent(context, SongReceiver::class.java).apply {
            action = ACTION_REWIND.toString()
        }

        val rewindPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context,
            AppInstance.REWIND_REQUEST_CODE,
            rewindIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        return NotificationCompat.Action(
            R.drawable.icon_bf, // Sử dụng biểu tượng mới
            "Rewind",
            rewindPendingIntent
        )
    }


    private fun getFFAction(): NotificationCompat.Action {
        val rewindIntent = Intent(context, SongReceiver::class.java).apply {
            action = ACTION_FAST_FORWARD.toString()
        }

        val rewindPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context,
            AppInstance.FF_REQUEST_CODE,
            rewindIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        return NotificationCompat.Action(
            R.drawable.icon_ff, // Sử dụng biểu tượng mới
            "Fast Forward",
            rewindPendingIntent
        )
    }

    private fun getCloseAction(): NotificationCompat.Action {
        val closeIntent = Intent(context, SongReceiver::class.java).apply {
            action = ACTION_STOP.toString()
        }

        val rewindPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context,
            AppInstance.CLOSE_REQUEST_CODE,
            closeIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        return NotificationCompat.Action(
            R.drawable.icon_cancel, // Sử dụng biểu tượng mới
            "Close",
            rewindPendingIntent
        )
    }

}*/
