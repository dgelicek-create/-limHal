package com.ismail.esonvpro.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.ismail.esonvpro.service.PrayerForegroundService

class PrayerAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("PrayerAlarmReceiver", "Exact alarm triggered at exact millisecond!")

        // Immediately launch foreground service to prevent OS termination (10-second limit bypass)
        val serviceIntent = Intent(context, PrayerForegroundService::class.java)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
        
        // Note: Queue strategy. In a complete app, we'd query Room DB here and 
        // schedule the next alarm using AlarmManagerHelper to keep the queue of 5 active.
    }
}
