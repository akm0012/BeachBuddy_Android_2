package com.andrewkingmarshall.beachbuddy2.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andrewkingmarshall.beachbuddy.network.dtos.HourlyWeatherDto

@Entity
data class HourlyWeatherInfo(

    /**
     * We only want to save how ever many we want to show. So this is the index of the RecyclerView.
     *
     * So if we want to show 6 hours, this would be an Int from 0 - 5. That way old data is naturally purged.
     */
    @PrimaryKey
    var id: Int = 0,

    var time: Long = 0,

    var temp: Double = 0.0,

    var feelsLike: Double = 0.0,

    var humidity: Int = 0,

    var clouds: Int = 0,

    var windSpeed: Double = 0.0,

    var mainDescription: String = "",

    var secondaryDescription: String = "",

    var iconTemplate: String = ""
) {
    constructor(index: Int, hourDto: HourlyWeatherDto) : this() {

        id = index

        time = hourDto.dt
        temp = hourDto.temp
        feelsLike = hourDto.feelsLikeTemp
        humidity = hourDto.humidity
        clouds = hourDto.clouds
        windSpeed = hourDto.windSpeed
        mainDescription = hourDto.weather?.get(0)?.main ?: ""
        secondaryDescription = hourDto.weather?.get(0)?.description ?: ""
        iconTemplate = hourDto.weather?.get(0)?.icon ?: ""
    }
}