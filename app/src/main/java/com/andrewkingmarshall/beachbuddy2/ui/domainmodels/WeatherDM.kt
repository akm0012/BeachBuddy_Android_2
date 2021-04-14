package com.andrewkingmarshall.beachbuddy2.ui.domainmodels

import com.andrewkingmarshall.beachbuddy2.database.model.BeachConditions
import com.andrewkingmarshall.beachbuddy2.database.model.CurrentUvInfo
import com.andrewkingmarshall.beachbuddy2.database.model.CurrentWeather

data class WeatherDM(
    val currentWeather: CurrentWeather,
    val beachConditions: BeachConditions,
    val uvInfo: CurrentUvInfo,
)
