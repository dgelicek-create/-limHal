package com.ismail.esonvpro.data.repository

import android.util.Log
import com.ismail.esonvpro.data.local.PrayerTimeDao
import com.ismail.esonvpro.data.local.PrayerTimeEntity
import com.ismail.esonvpro.data.remote.CityDataDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PrayerTimeRepository(
    private val dao: PrayerTimeDao,
    private val httpClient: HttpClient
) {
    
    // Fetch JSON from remote/CDN and save to Room DB
    suspend fun syncPrayerTimes(cdnUrl: String) {
        withContext(Dispatchers.IO) {
            try {
                Log.d("PrayerTimeRepository", "Starting download from CDN...")
                val response: CityDataDto = httpClient.get(cdnUrl).body()
                
                // Convert DTO to Entity
                val entities = response.timesList.map { dto ->
                    PrayerTimeEntity(
                        date = dto.date,
                        timestamp = dto.timestamp,
                        imsak = dto.imsak,
                        gunes = dto.gunes,
                        ogle = dto.ogle,
                        ikindi = dto.ikindi,
                        aksam = dto.aksam,
                        yatsi = dto.yatsi,
                        districtId = response.id
                    )
                }
                
                // Clear old data and insert new
                dao.clearAll()
                dao.insertPrayerTimes(entities)
                Log.d("PrayerTimeRepository", "Successfully synced and saved ${entities.size} days to Room DB.")
                
            } catch (e: Exception) {
                // Try-catch block ensures internet failure is handled gracefully
                Log.e("PrayerTimeRepository", "Failed to sync prayer times: ${e.message}")
            }
        }
    }

    fun getTodayPrayerTimes(timestamp: Long): Flow<PrayerTimeEntity?> {
        return dao.getTodayPrayerTimes(timestamp)
    }
}
