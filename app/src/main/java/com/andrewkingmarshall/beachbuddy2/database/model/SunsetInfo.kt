package com.andrewkingmarshall.beachbuddy2.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andrewkingmarshall.beachbuddy2.network.dtos.WeatherInfoDto
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

@Entity
data class SunsetInfo(
    @PrimaryKey
    var id: String = "SunsetInfoPrimaryKey",

    var sunrise: Long = 0,

    var sunset: Long = 0,

    var sunriseNextDay: Long = 0,

    var sunsetPrevDay: Long = 0
) {
    constructor(weatherInfoDto: WeatherInfoDto) : this() {

        sunrise =
            DateTime(weatherInfoDto.current.sunrise * 1000).withZone(DateTimeZone.getDefault()).millis

        sunset =
            DateTime(weatherInfoDto.current.sunset * 1000).withZone(DateTimeZone.getDefault()).millis

        // Fixme: could make this more accurate by not assuming sunset/sunrise times
        sunriseNextDay = DateTime(sunrise).plusDays(1).withZone(DateTimeZone.getDefault()).millis
        sunsetPrevDay = DateTime(sunset).minusDays(1).withZone(DateTimeZone.getDefault()).millis
    }
}
