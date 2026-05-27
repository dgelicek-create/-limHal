package com.ismail.esonvpro.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.ismail.esonvpro.data.local.AppDatabase
import com.ismail.esonvpro.util.AlarmManagerHelper
import java.util.Calendar

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Device rebooted. Rescheduling prayer alarms in background.")
            
            // Read active schedule from PrayerTimeDao in the background
            val pendingResult = goAsync()
            val dao = AppDatabase.getDatabase(context).prayerTimeDao()
            
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val currentTimestamp = getTodayMidnightTimestamp()
                    // Fetch upcoming 5 days to ensure we have data
                    val upcomingTimes = dao.getUpcomingPrayerTimes(currentTimestamp)
                    
                    var alarmCount = 0
                    val now = System.currentTimeMillis()
                    
                    // Iterate and reschedule the exact alarms (Up to 5 to respect system limits)
                    for (times in upcomingTimes) {
                        val prayerArray = arrayOf(times.imsak, times.gunes, times.ogle, times.ikindi, times.aksam, times.yatsi)
                        for ((index, prayerTimeMillis) in prayerArray.withIndex()) {
                            if (prayerTimeMillis > now && alarmCount < 5) {
                                // Unique request code per time
                                val requestCode = (times.timestamp / 100000).toInt() + index
                                AlarmManagerHelper.scheduleExactAlarm(context, prayerTimeMillis, requestCode)
                                alarmCount++
                            }
                        }
                    }
                    Log.d("BootReceiver", "Successfully rescheduled $alarmCount upcoming alarms.")
                } catch (e: Exception) {
                    Log.e("BootReceiver", "Failed to reschedule alarms: ${e.message}")
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }

    private fun getTodayMidnightTimestamp(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }
}
