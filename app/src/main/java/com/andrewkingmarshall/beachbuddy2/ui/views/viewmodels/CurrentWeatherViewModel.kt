package com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.annotation.ColorRes
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.enums.FlagColor.*
import com.andrewkingmarshall.beachbuddy2.ui.domainmodels.WeatherDM
import timber.log.Timber
import java.util.*
import kotlin.math.roundToInt

const val DEFAULT_LOCALITY = "Siesta Key"

class CurrentWeatherViewModel constructor(
    val context: Context,
    weather: WeatherDM,
) {

    private val currentWeather = weather.currentWeather
    private val beachConditions = weather.beachConditions


    fun getCityName(): String {

        // Special case, if Beach is Closed
        if (beachConditions.flagColor == DOUBLE_RED) {
            return "BEACH CLOSED!"
        }

        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address> =
                geocoder.getFromLocation(currentWeather.latitude, currentWeather.longitude, 1)
            val locality = if (addresses.isNotEmpty()) {
                addresses[0].locality
            } else {
                Timber.w("Locality was not found!")
                DEFAULT_LOCALITY
            }

            return locality

        } catch (e: Exception) {
            Timber.e(e)
        }

        return DEFAULT_LOCALITY
    }

    fun getWeatherDescription(): String {
        return currentWeather.mainDescription
    }

    fun getIconUrl(): String {
        return "https://openweathermap.org/img/wn/${currentWeather.iconTemplate}@2x.png"
    }

    fun getFeelsLikeTemp(): String {
        return "${currentWeather.feelsLikeTemp.roundToInt()}°"
    }

    @ColorRes
    fun getCardBackgroundColor(): Int {

        return when (beachConditions.flagColor) {
            GREEN -> R.color.flag_green
            YELLOW -> R.color.flag_yellow
            PURPLE -> R.color.flag_purple
            RED, DOUBLE_RED -> R.color.flag_red
            UNKNOWN -> R.color.white
            else -> R.color.white
        }
    }

    @ColorRes
    fun getTextColor(): Int {
        return when (beachConditions.flagColor) {
            GREEN -> R.color.white
            YELLOW -> R.color.dark_gray
            PURPLE -> R.color.white
            RED, DOUBLE_RED -> R.color.white
            UNKNOWN -> R.color.dashboard_text_dark
            else -> R.color.dashboard_text_dark
        }
    }

    @ColorRes
    fun getSecondaryTextColor(): Int {
        return when (beachConditions.flagColor) {
            GREEN -> R.color.white
            YELLOW -> R.color.dark_gray
            PURPLE -> R.color.white
            RED, DOUBLE_RED -> R.color.white
            UNKNOWN -> R.color.dashboard_text
            else -> R.color.dashboard_text
        }
    }
}