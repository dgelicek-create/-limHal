package com.ismail.esonvpro.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ismail.esonvpro.data.local.PrayerTimeEntity
import com.ismail.esonvpro.data.repository.PrayerTimeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.Calendar

class MainViewModel(
    private val repository: PrayerTimeRepository
) : ViewModel() {

    private val _todayPrayerTime = MutableStateFlow<PrayerTimeEntity?>(null)
    val todayPrayerTime: StateFlow<PrayerTimeEntity?> = _todayPrayerTime.asStateFlow()

    private val _countdownText = MutableStateFlow("00:00:00")
    val countdownText: StateFlow<String> = _countdownText.asStateFlow()

    // Example mock CDN URL
    private val CDN_URL = "https://example.com/Turkey_Istanbul_12000.json"

    init {
        syncAndLoad()
    }

    private fun syncAndLoad() {
        viewModelScope.launch {
            // Unblock UI immediately: try to sync in background
            launch { repository.syncPrayerTimes(CDN_URL) }

            // Observe today's data from DB
            val todayStart = getTodayMidnightTimestamp()
            repository.getTodayPrayerTimes(todayStart).collect { entity ->
                _todayPrayerTime.value = entity
                entity?.let {
                    // Start countdown to the next prayer
                    startCountdownEngine(it)
                }
            }
        }
    }

    private fun startCountdownEngine(times: PrayerTimeEntity) {
        viewModelScope.launch {
            getNextPrayerTimestamp(times).collect { targetTime ->
                val remaining = targetTime - System.currentTimeMillis()
                if (remaining > 0) {
                    val hours = (remaining / (1000 * 60 * 60)) % 24
                    val minutes = (remaining / (1000 * 60)) % 60
                    val seconds = (remaining / 1000) % 60
                    _countdownText.value = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                } else {
                    _countdownText.value = "00:00:00"
                }
            }
        }
    }

    // Precise Flow engine emitting every second
    private fun getNextPrayerTimestamp(times: PrayerTimeEntity): Flow<Long> = flow {
        while (true) {
            val now = System.currentTimeMillis()
            val target = when {
                now < times.imsak -> times.imsak
                now < times.gunes -> times.gunes
                now < times.ogle -> times.ogle
                now < times.ikindi -> times.ikindi
                now < times.aksam -> times.aksam
                now < times.yatsi -> times.yatsi
                else -> times.imsak + 86400000 // Next day imsak
            }
            emit(target)
            delay(1000)
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
