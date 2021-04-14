package com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels

import androidx.annotation.ColorRes
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.ui.domainmodels.WeatherDM
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import timber.log.Timber
import kotlin.math.roundToInt

class CurrentUvViewModel(
    weather: WeatherDM
) {

    private val currentWeather = weather.currentWeather
    private val currentUvInfo = weather.uvInfo

    private var lastLoadedSkinType: Int? = null

    fun getSunViewStartTime(): String {
        return DateTime(currentWeather.sunrise * 1000).withZone(DateTimeZone.getDefault()).toString("H:mm")
    }

    fun getSunViewEndTime(): String {
        return DateTime(currentWeather.sunset * 1000).withZone(DateTimeZone.getDefault()).toString("H:mm")
    }

    fun getSunViewCurrentTime(): String {
        return DateTime().withZone(DateTimeZone.getDefault()).toString("H:mm")
    }

    @ColorRes
    fun getUvColor(): Int {

        try {
            return when (getUvIndex().toDouble()) {
                in 0.0..2.999 -> R.color.uv_low
                in 3.0..5.999 -> R.color.uv_moderate
                in 6.0..7.999 -> R.color.uv_high
                in 8.0..10.999 -> R.color.uv_very_high
                else -> R.color.uv_extreme
            }
        } catch (e: NumberFormatException) {
            Timber.w(e, "Can not parse UV Index...")
        }

        return R.color.colorAccent
    }

    fun getUvIndex(): String {
        val currentUv = currentUvInfo.currentUv ?: return "N/A"

        val cloudMultiplier = when (currentWeather.cloudPercent) {
            in 0..20 -> 1.0
            in 21..70 -> 0.89
            in 70..90 -> 0.73
            in 91..100 -> 0.31
            else -> 1.0
        }

        val adjustedUvIndex = currentUv * cloudMultiplier

        val roundedUvIndex = String.format("%.2f", adjustedUvIndex).toDouble()

        return roundedUvIndex.toString()
    }

    fun getTimeToBurn(skinType: Int?): String {

        var skinTypeToUse = skinType

        if (skinType == null) {
            skinTypeToUse = lastLoadedSkinType
        } else {
            lastLoadedSkinType = skinType
        }

        try {
            val uvIndex = getUvIndex().toDouble()

            if (uvIndex == 0.0) {
                return "∞"
            }

            val skinTypeMultiplier = when (skinTypeToUse) {
                1 -> 2.5
                2 -> 3.0
                3 -> 4.0
                4 -> 5.0
                5 -> 8.0
                6 -> 15.0
                else -> 2.5
            }

            val minToBurn = ((200 * skinTypeMultiplier) / (3 * uvIndex)).roundToInt()
            return "$minToBurn min"

        } catch (e: NumberFormatException) {
            Timber.w(e, "Can not parse UV Index...")
        }

        return "N/A"
    }
}