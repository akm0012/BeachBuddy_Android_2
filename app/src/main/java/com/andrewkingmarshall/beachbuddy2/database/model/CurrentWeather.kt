package com.andrewkingmarshall.beachbuddy2.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andrewkingmarshall.beachbuddy.network.dtos.BeachConditionsDto
import com.andrewkingmarshall.beachbuddy.network.dtos.CurrentUvDto
import com.andrewkingmarshall.beachbuddy.network.dtos.WeatherInfoDto

@Entity
data class CurrentWeather(
    @PrimaryKey
    var id: String = "CurrentWeatherPrimaryKey",

    var latitude: Double = 0.0,

    var longitude: Double = 0.0,

    var timeUpdated: Long = 0,

    var sunrise: Long = 0,

    var sunset: Long = 0,

    var temp: Double = 0.0,

    var feelsLikeTemp: Double = 0.0,

    var humidityPercent: Int = 0,

    var cloudPercent: Int = 0,

    // Unit: m/hr
    var windSpeed: Double = 0.0,

    var windGust: Double = 0.0,

    var windDeg: Int = 0,

    var mainDescription: String = "",

    var secondaryDescription: String = "",

    var iconTemplate: String = "",

    var beachConditions: BeachConditions? = null,

    var currentUvInfo: CurrentUvInfo? = null
) {
    constructor(
        weatherDto: WeatherInfoDto,
        beachConditionsDto: BeachConditionsDto?,
        uvDto: CurrentUvDto?
    ) : this() {

        val currentWeatherDto = weatherDto.current

        latitude = weatherDto.lat
        longitude = weatherDto.lon

        timeUpdated = currentWeatherDto.dt
        sunrise = currentWeatherDto.sunrise
        sunset = currentWeatherDto.sunset
        temp = currentWeatherDto.temp
        feelsLikeTemp = currentWeatherDto.feelsLikeTemp
        humidityPercent = currentWeatherDto.humidity
        cloudPercent = currentWeatherDto.clouds
        windSpeed = currentWeatherDto.windSpeed
        windGust = currentWeatherDto.windGust
        windDeg = currentWeatherDto.windDeg
        mainDescription = currentWeatherDto.weather[0].main
        secondaryDescription = currentWeatherDto.weather[0].description
        iconTemplate = currentWeatherDto.weather[0].icon

        beachConditions = beachConditionsDto?.let { BeachConditions(it) }

        currentUvInfo = uvDto?.let { CurrentUvInfo(it) }
    }
}