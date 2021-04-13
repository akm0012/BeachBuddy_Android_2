package com.andrewkingmarshall.beachbuddy2.network.dtos

data class DailyFeelsLikeSummaryDto(

    var day: Double,

    var night: Double,

    var eve: Double,

    var morn: Double
)