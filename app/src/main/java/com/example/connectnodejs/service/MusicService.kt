package com.example.connectnodejs.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.ACTION_SEEK_TO
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.connectnodejs.MainActivity
import com.example.connectnodejs.R
import com.example.connectnodejs.utils.AppInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MusicService : Service() {
    var mediaPlayer: MediaPlayer? = null
    private val binder = LocalBinder()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    var isNotificationCreated=false
    private lateinit var bitmap:Bitmap

    var loadIsDone=MutableLiveData<Boolean>()
    var btnPlayClick=MutableLiveData<Unit>()
    var btnRewindClick=MutableLiveData<Unit>()
    var btnFastForwardClick=MutableLiveData<Unit>()
    var btnCloseClick=MutableLiveData<Unit>()
    var seekBarSlide=MutableLiveData<Unit>()

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    inner class LocalBinder : Binder() {
        fun getService(): MusicService{
            return this@MusicService
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.Start.toString()->{
                Log.i("Nothing","Notifi btnPlayClick")
                play()
                btnPlayClick.postValue(Unit)
            }
            Actions.Rewind.toString()->{
                Log.i("Nothing","Notifi btnRewindClick")
                rewind(2000)

                btnRewindClick.postValue(Unit)
            }
            Actions.FastForward.toString()->{
                Log.i("Nothing","Notifi btnFastForwardClick")
                fastForward(2000)

                btnFastForwardClick.postValue(Unit)
            }
            ACTION_SEEK_TO.toString()->{
                Log.i("Nothing","Notifi seekBar")
            }
            Actions.Load.toString()->{

            }
            Actions.Close.toString()->{
                cancelNotification()
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name="Music Channel"
            val description="Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            channel.setSound(null,null)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(songName:String,songAuthor:String,action:Actions): Notification {
        Log.i("Nothing","Create Notification")

        val mediaSession = MediaSessionCompat(this, "TuhPlayer")
        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(mediaSession.sessionToken)

        setUpSeekBar(mediaSession, action)

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("data", "test")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(songName)
            .setContentText(songAuthor)
            .setSmallIcon(R.drawable.icon_favorite)
            .setStyle(mediaStyle)
            .addAction(getRewindAction())
            .addAction(getPlayAction())
            .addAction(getFFAction())
            .addAction(getCloseAction())
            .setLargeIcon(bitmap)
            .setSound(null)
            .setContentIntent(pendingIntent)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
    }

    private fun setUpSeekBar(
        mediaSession: MediaSessionCompat,
        action: Actions
    ) {
        val metadata = MediaMetadataCompat.Builder()
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer!!.duration.toLong())
            .build()
        mediaSession.setMetadata(metadata)

        // Add this to set the playback state for the media session
        val stateBuilder = PlaybackStateCompat.Builder()
            .setActions(ACTION_SEEK_TO)
            .setState(getPlayBackState(action), mediaPlayer!!.currentPosition.toLong(), 1f)
        mediaSession.setPlaybackState(stateBuilder.build())

        val mediaSessionCallback = object : MediaSessionCompat.Callback() {
            override fun onSeekTo(pos: Long) {
                seekTo(pos.toInt())
            }
        }

        val handler = Handler(Looper.getMainLooper())
        handler.post {
            mediaSession.setCallback(mediaSessionCallback)
        }
    }

    private fun getPlayBackState(action:Actions):Int{
        when(action){
            Actions.Start->{
                if(mediaPlayer!!.isPlaying)
                    return PlaybackStateCompat.STATE_PLAYING
                return PlaybackStateCompat.STATE_PAUSED
            }
            Actions.Rewind->{
                return PlaybackStateCompat.STATE_REWINDING
            }
            Actions.FastForward->{
                return PlaybackStateCompat.STATE_FAST_FORWARDING
            }
            Actions.SeekTo->{
                if(mediaPlayer!!.isPlaying)
                    return PlaybackStateCompat.STATE_PLAYING
                return PlaybackStateCompat.STATE_PAUSED
            }

            else -> return -1
        }
    }

    fun cancelNotification() {
        if (isNotificationCreated) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            isNotificationCreated = false
            if(mediaPlayer!!.isPlaying){
                btnCloseClick.value=Unit
            }
        }else{
            Log.i("Nothing","Maybe something wrong")
        }
    }

    fun play(){
        if (loadIsDone.value==true) {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer?.pause()
            } else {
                mediaPlayer?.start()
            }

            updateNotification(Actions.Start)
        }else{
            Log.i("DEBUG","Can't Play")
        }
    }

    private fun getPlayAction(): NotificationCompat.Action {
        val playIntent = Intent(this, MusicService::class.java)
        playIntent.action = Actions.Start.toString()
        val playPendingIntent =
            PendingIntent.getService(
                this,
                0,
                playIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val playPauseIcon = if (mediaPlayer!!.isPlaying) {
            R.drawable.icon_pause
        } else {
            R.drawable.icon_play
        }
        val playPauseAction = NotificationCompat.Action(
            playPauseIcon,
            "Play",
            playPendingIntent
        )
        return playPauseAction
    }

    private fun getRewindAction(): NotificationCompat.Action {
        val rewindIntent = Intent(this, MusicService::class.java).apply {
            action = Actions.Rewind.toString()
        }
        val rewindPendingIntent: PendingIntent = PendingIntent.getService(
            this,
            AppInstance.REWIND_REQUEST_CODE,
            rewindIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        return NotificationCompat.Action(
            R.drawable.icon_rewind_24,
            "Rewind",
            rewindPendingIntent
        )
    }

    private fun getFFAction(): NotificationCompat.Action {
        val fastForwardIntent = Intent(this, MusicService::class.java).apply {
            action = Actions.FastForward.toString()
        }

        val rewindPendingIntent: PendingIntent = PendingIntent.getService(
            this,
            AppInstance.FF_REQUEST_CODE,
            fastForwardIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        return NotificationCompat.Action(
            R.drawable.icon_ff_24,
            "Fast Forward",
            rewindPendingIntent
        )
    }

    private fun getCloseAction(): NotificationCompat.Action {
        val closeIntent = Intent(this, MusicService::class.java).apply {
            action = Actions.Close.toString()
        }

        val rewindPendingIntent: PendingIntent = PendingIntent.getService(
            this,
            AppInstance.CLOSE_REQUEST_CODE,
            closeIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        return NotificationCompat.Action(
            R.drawable.icon_cancel,
            "Close",
            rewindPendingIntent
        )
    }


    fun load(url:String){
        if(mediaPlayer!=null){
            mediaPlayer!!.release()
            btnPlayClick.postValue(Unit)
        }

        createNotificationChannel()
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(url)
        mediaPlayer?.setOnPreparedListener {
            Log.i("Nothing","Media Player Set Up is Done")
            loadIsDone.postValue(true)
            Glide.with(this)
                .asBitmap()
                .load(AppInstance.bookImg)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        bitmap = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Xử lý khi việc tải bị xóa
                    }
                })
            /*bitmap = BitmapFactory.decodeResource(this.resources, R.drawable.song_circle)*/
        }
        mediaPlayer?.prepareAsync()
    }

    fun seekTo(time:Int){
        mediaPlayer!!.seekTo(time)
        seekBarSlide.postValue(Unit)
        updateNotification(Actions.SeekTo)
    }

    fun rewind(time:Int){
        mediaPlayer!!.seekTo(mediaPlayer!!.currentPosition-time)
        updateNotification(Actions.Rewind)
    }

    fun fastForward(time:Int){
        mediaPlayer!!.seekTo(mediaPlayer!!.currentPosition+time)
        updateNotification(Actions.FastForward)
    }

    private fun updateNotification(action:Actions){
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                if (!isNotificationCreated) {
                    startForeground(NOTIFICATION_ID, createNotification(AppInstance.bookName!!, AppInstance.bookAuthorName!!,action))
                    isNotificationCreated = true
                } else {
                    val notification = createNotification(AppInstance.bookName!!, AppInstance.bookAuthorName!!,action)
                    if (ActivityCompat.checkSelfPermission(
                            this@MusicService,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                    }
                    NotificationManagerCompat.from(this@MusicService).notify(NOTIFICATION_ID, notification)
                }
            }
        }
    }

    enum class Actions{
        Start,Stop,Load,Rewind,FastForward,Close,SeekTo
    }

    companion object {
        private const val CHANNEL_ID = "MusicServiceChannel"
        private const val NOTIFICATION_ID = 101
    }
}