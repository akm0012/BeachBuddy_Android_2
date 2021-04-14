package com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels

import android.content.Context
import com.andrewkingmarshall.beachbuddy2.database.model.DailyWeatherInfo
import com.andrewkingmarshall.beachbuddy2.extensions.capitalizeWords
import com.andrewkingmarshall.beachbuddy2.extensions.millimetersToInches
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import kotlin.math.roundToInt

class DailyWeatherItemViewModel(
    private val context: Context,
    private val dailyWeather: DailyWeatherInfo
) {

    fun getTime(): String {
        return DateTime(dailyWeather.time * 1000).withZone(DateTimeZone.getDefault())
            .toString("EE")
    }

    fun getIconUrl(): String {
        return "http://openweathermap.org/img/wn/${dailyWeather.iconTemplate}@2x.png"
    }

    fun getSummary(): String {
        return dailyWeather.mainDescription.capitalizeWords()
    }

    fun getFeelsLike(): String {
        return "${dailyWeather.feelsLikeDay.roundToInt()}°"
    }

    fun getRain(): String {

        val millimetersToInches = dailyWeather.rainMilliMeters.millimetersToInches()

        val roundedInchesOfRain = String.format("%.2f", millimetersToInches).toDouble()

        return "$roundedInchesOfRain\""
    }

}