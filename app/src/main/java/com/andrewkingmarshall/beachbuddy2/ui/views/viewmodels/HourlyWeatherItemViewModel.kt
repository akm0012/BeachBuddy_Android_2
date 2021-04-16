package com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels

import android.content.Context
import com.andrewkingmarshall.beachbuddy2.database.model.HourlyWeatherInfo
import com.andrewkingmarshall.beachbuddy2.extensions.capitalizeWords
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import kotlin.math.roundToInt

class HourlyWeatherItemViewModel(
    private val context: Context,
    private val hourlyWeather: HourlyWeatherInfo
) {

    fun getTime(): String {
        return DateTime(hourlyWeather.time * 1000).withZone(DateTimeZone.getDefault())
            .toString("h:mm a")
    }

    fun getIconUrl(): String {
        return "https://openweathermap.org/img/wn/${hourlyWeather.iconTemplate}@2x.png"
    }

    fun getSummary(): String {
        return hourlyWeather.mainDescription.capitalizeWords()
    }

    fun getFeelsLike(): String {
        return "${hourlyWeather.feelsLike.roundToInt()}°"
    }

    fun getHumidity(): String {
        return "${hourlyWeather.humidity}%"
    }

}