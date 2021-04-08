package com.andrewkingmarshall.beachbuddy2.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andrewkingmarshall.beachbuddy.network.dtos.BeachConditionsDto
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

@Entity
data class BeachConditions(

    @PrimaryKey
    var id: String = "BeachConditionsPrimaryKey",

    var timeUpdated: Long = 0,

    var flagColor: String = "Green",

    var surfCondition: String = "",

    var surfHeight: String = "",

    var respiratoryIrritation: String = "",

    var jellyFish: String = "",
) {

    constructor(beachConditionsDto: BeachConditionsDto): this() {
        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ") // Example: "2021-04-04T13:56:23+00:00"
        val dateTime = formatter.parseDateTime(beachConditionsDto.updatedTime).withZone(DateTimeZone.UTC)
        timeUpdated = dateTime.millis

        flagColor = beachConditionsDto.flagColor
        surfCondition = beachConditionsDto.surfCondition
        surfHeight = beachConditionsDto.surfHeight
        respiratoryIrritation = beachConditionsDto.respiratoryIrritation
        jellyFish = beachConditionsDto.jellyFishPresent
    }

}
