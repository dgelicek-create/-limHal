package com.ismail.esonvpro.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityDataDto(
    @SerialName("country") val country: String,
    @SerialName("city") val city: String,
    @SerialName("district") val district: String,
    @SerialName("id") val id: Int,
    @SerialName("times_list") val timesList: List<PrayerTimesDto>
)

@Serializable
data class PrayerTimesDto(
    @SerialName("date") val date: String,
    @SerialName("imsak") val imsak: Long,
    @SerialName("gunes") val gunes: Long,
    @SerialName("ogle") val ogle: Long,
    @SerialName("ikindi") val ikindi: Long,
    @SerialName("aksam") val aksam: Long,
    @SerialName("yatsi") val yatsi: Long,
    @SerialName("timestamp") val timestamp: Long
)
