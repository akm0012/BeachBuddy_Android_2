package com.andrewkingmarshall.beachbuddy2.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andrewkingmarshall.beachbuddy.network.dtos.CurrentUvDto

@Entity
data class CurrentUvInfo(

    @PrimaryKey
    var id:String = "CurrentUvInfoPrimaryKey",

    var currentUv: Double = 0.0,

    var currentUvTime: String = "",

    var maxUv: Double = 0.0,

    var maxUvTime: String = "",

    var safeExposureMinSkin1: Int? = null,

    var safeExposureMinSkin2: Int? = null,

    var safeExposureMinSkin3: Int? = null,

    var safeExposureMinSkin4: Int? = null,

    var safeExposureMinSkin5: Int? = null,

    var safeExposureMinSkin6: Int? = null

) {
    constructor(uvDto: CurrentUvDto): this() {
        currentUv = uvDto.uv
        currentUvTime = uvDto.uvTime
        maxUv = uvDto.uvMax
        maxUvTime = uvDto.uvMaxTime
        safeExposureMinSkin1 = uvDto.safeExposureTime.st1
        safeExposureMinSkin2 = uvDto.safeExposureTime.st2
        safeExposureMinSkin3 = uvDto.safeExposureTime.st3
        safeExposureMinSkin4 = uvDto.safeExposureTime.st4
        safeExposureMinSkin5 = uvDto.safeExposureTime.st5
        safeExposureMinSkin6 = uvDto.safeExposureTime.st6
    }
}