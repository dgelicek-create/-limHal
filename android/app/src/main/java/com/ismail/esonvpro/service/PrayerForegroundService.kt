package com.ismail.esonvpro.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.ismail.esonvpro.R

class PrayerForegroundService : Service() {

    private var exoPlayer: ExoPlayer? = null
    private val NOTIFICATION_ID = 101
    private val CHANNEL_ID = "EzanVaktiChannel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("PrayerForegroundService", "Service started. Initiating Media3 ExoPlayer.")

        // Show persistent notification to prevent OS from killing the service
        val notification = buildNotification()
        startForeground(NOTIFICATION_ID, notification)

        playAdhanAudio()

        // Return START_NOT_STICKY because we don't want it to restart automatically if killed.
        return START_NOT_STICKY
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Ezan Vakti Pro")
            .setContentText("Ezan okunuyor...")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm) // Using system icon as fallback
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Ezan Bildirimleri",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun playAdhanAudio() {
        exoPlayer = ExoPlayer.Builder(this).build().apply {
            // Load local raw resource or file. For now, assuming raw/ezan.mp3
            // val mediaItem = MediaItem.fromUri("android.resource://${packageName}/${R.raw.ezan}")
            // Fallback for compilation: Use a dummy URI or just log.
            val dummyUri = "android.resource://${packageName}/raw/ezan"
            val mediaItem = MediaItem.fromUri(dummyUri)
            
            setMediaItem(mediaItem)
            prepare()
            
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        Log.d("PrayerForegroundService", "Adhan finished. Releasing resources.")
                        stopSelf() // Stop service when audio ends, releasing resources
                    }
                }
            })
            play()
        }
    }

    override fun onDestroy() {
        exoPlayer?.release()
        exoPlayer = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // We don't provide binding for this service
    }
}
