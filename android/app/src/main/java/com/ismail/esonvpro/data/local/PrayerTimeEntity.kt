package com.ismail.esonvpro.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "prayer_times",
    indices = [Index(value = ["timestamp"], unique = true)] // Indexed timestamp for ultra-fast queries
)
data class PrayerTimeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val timestamp: Long, // Midnight UTC of the day
    val imsak: Long,
    val gunes: Long,
    val ogle: Long,
    val ikindi: Long,
    val aksam: Long,
    val yatsi: Long,
    val districtId: Int
)
