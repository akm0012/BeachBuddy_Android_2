package com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels

import android.text.TextUtils
import androidx.annotation.DrawableRes
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.enums.BeachConditionItemType
import com.andrewkingmarshall.beachbuddy2.enums.BeachConditionItemType.*
import com.andrewkingmarshall.beachbuddy2.extensions.capitalizeWords
import com.andrewkingmarshall.beachbuddy2.ui.domainmodels.WeatherDM
import org.joda.time.DateTime

class BeachConditionItemViewModel(
    private val itemType: BeachConditionItemType,
    weatherDM: WeatherDM,
) {

    private val weatherInfo = weatherDM.currentWeather
    private val beachConditions = weatherDM.beachConditions

    @DrawableRes
    fun getIconDrawable(): Int {
        return when (itemType) {
            CLOUD_COVERAGE_PERCENT -> R.drawable.ic_clouds_100
            WIND -> R.drawable.ic_air_100
            RESPIRATORY_IRRITATION -> R.drawable.ic_particle_100
            SURF -> R.drawable.ic_surfing_100
            JELLY_FISH -> R.drawable.ic_jellyfish_100
            TIME_UPDATED -> R.drawable.ic_delivery_time_100
        }
    }

    fun getTitle(): String {
        return when (itemType) {
            CLOUD_COVERAGE_PERCENT -> "Cloud Coverage"
            WIND -> "Wind"
            RESPIRATORY_IRRITATION -> "Respiratory Irritation"
            SURF -> "Surf"
            JELLY_FISH -> "Jelly Fish"
            TIME_UPDATED -> "Updated At"
        }
    }

    fun getContent(): String {
        return when (itemType) {

            CLOUD_COVERAGE_PERCENT -> {
                "${weatherInfo.cloudPercent}%"
            }

            WIND -> {
                "${weatherInfo.windSpeed.toInt()} mph ${weatherInfo.windDeg}°"
            }

            RESPIRATORY_IRRITATION -> beachConditions.respiratoryIrritation.capitalizeWords()
                ?: "N/A"

            SURF -> {

                val surfOverview = beachConditions.surfCondition.capitalizeWords()
                    ?: "N/A"

                var surfHeight = beachConditions.surfHeight.capitalizeWords()
                    ?: ""

                if (!TextUtils.isEmpty(surfHeight)) {
                    surfHeight = "($surfHeight)"
                }

                "$surfOverview $surfHeight".trim()
            }

            JELLY_FISH -> beachConditions.jellyFish.capitalizeWords()
                ?: "N/A"

            TIME_UPDATED -> {
                DateTime(beachConditions.timeUpdated).toString("EE h:mm a")
            }
        }
    }

}