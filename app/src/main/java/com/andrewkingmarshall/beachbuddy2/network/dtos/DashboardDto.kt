package com.andrewkingmarshall.beachbuddy2.network.dtos

import com.google.gson.annotations.SerializedName

data class DashboardDto(

    var users: List<UserDto>,

    var beachConditions: BeachConditionsDto,

    @SerializedName("dashboardUvDto")
    var currentUvDto: CurrentUvDto,

    @SerializedName("weatherInfo")
    var weatherDto: WeatherInfoDto

)