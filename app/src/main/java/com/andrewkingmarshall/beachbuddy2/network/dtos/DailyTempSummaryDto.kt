package com.andrewkingmarshall.beachbuddy2.network.dtos

data class DailyTempSummaryDto(

    var day: Double,

    var min: Double,

    var max: Double,

    var night: Double,

    var eve: Double,

    var morn: Double
)