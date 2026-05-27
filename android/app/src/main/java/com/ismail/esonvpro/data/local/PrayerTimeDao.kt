package com.ismail.esonvpro.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerTimeDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerTimes(prayerTimes: List<PrayerTimeEntity>)

    @Query("SELECT * FROM prayer_times WHERE timestamp >= :currentTimestamp ORDER BY timestamp ASC LIMIT 1")
    fun getTodayPrayerTimes(currentTimestamp: Long): Flow<PrayerTimeEntity?>

    @Query("SELECT * FROM prayer_times WHERE timestamp >= :currentTimestamp ORDER BY timestamp ASC LIMIT 5")
    suspend fun getUpcomingPrayerTimes(currentTimestamp: Long): List<PrayerTimeEntity>
    
    @Query("DELETE FROM prayer_times")
    suspend fun clearAll()
}
