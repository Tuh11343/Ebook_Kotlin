package com.example.connectnodejs.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat.ACTION_FAST_FORWARD
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE
import android.support.v4.media.session.PlaybackStateCompat.ACTION_REWIND
import android.support.v4.media.session.PlaybackStateCompat.ACTION_SEEK_TO
import android.util.Log
import androidx.lifecycle.MutableLiveData

/*
class SongReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val controller = MediaControllerCompat(context, SongNotification.mediaSession.sessionToken)
        val transportControls = controller.transportControls

        when (intent!!.action) {
            ACTION_PLAY_PAUSE.toString()->{
                if(intent.getBooleanExtra("Play_Pause",false)){
                    transportControls.play()
                }else{
                    transportControls.pause()
                }
            }
            ACTION_REWIND.toString()->{
                transportControls.seekTo(SongNotification.mediaSession.controller.playbackState.position-2000)
            }
            ACTION_FAST_FORWARD.toString()->{
                transportControls.seekTo(SongNotification.mediaSession.controller.playbackState.position+2000)
            }
            ACTION_SEEK_TO.toString()->{

            }
            "Close"->{
                Log.i("Nothing","Close")
            }
        }
    }
}*/
