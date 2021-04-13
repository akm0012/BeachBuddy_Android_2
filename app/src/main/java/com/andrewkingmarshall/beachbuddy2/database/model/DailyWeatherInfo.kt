package com.andrewkingmarshall.beachbuddy2.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andrewkingmarshall.beachbuddy2.network.dtos.DailyWeatherDto

@Entity
data class DailyWeatherInfo(

    /**
     * We only want to save how ever many we want to show. So this is the index of the RecyclerView.
     *
     * So if we want to show 6 hours, this would be an Int from 0 - 5. That way old data is naturally purged.
     */
    @PrimaryKey
    var id: Int = 0,

    var time: Long = 0,

    var feelsLikeDay: Double = 0.0,

    var humidity: Int = 0,

    var rainMilliMeters: Double = 0.0,

    var mainDescription: String = "",

    var secondaryDescription: String = "",

    var iconTemplate: String = ""
) {
    constructor(index: Int, dayDto: DailyWeatherDto) : this() {

        id = index

        time = dayDto.dt
        feelsLikeDay = dayDto.feelsLike.day
        humidity = dayDto.humidity
        rainMilliMeters = dayDto.rain
        mainDescription = dayDto.weather?.get(0)?.main ?: ""
        secondaryDescription = dayDto.weather?.get(0)?.description ?: ""
        iconTemplate = dayDto.weather?.get(0)?.icon ?: ""
    }
}