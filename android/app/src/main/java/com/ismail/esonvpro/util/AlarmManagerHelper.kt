package com.ismail.esonvpro.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.ismail.esonvpro.receiver.PrayerAlarmReceiver

object AlarmManagerHelper {

    fun scheduleExactAlarm(context: Context, timeInMillis: Long, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        // Android 12+ (API 31) check for exact alarm permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.e("AlarmManagerHelper", "Cannot schedule exact alarms. Missing permission.")
                return
            }
        }

        val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            putExtra("ALARM_REQUEST_CODE", requestCode)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            // Bypass doze mode and trigger at the exact millisecond
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
            Log.d("AlarmManagerHelper", "Exact alarm scheduled for $timeInMillis with code $requestCode")
        } catch (e: SecurityException) {
            Log.e("AlarmManagerHelper", "SecurityException scheduling exact alarm: ${e.message}")
        }
    }
}
