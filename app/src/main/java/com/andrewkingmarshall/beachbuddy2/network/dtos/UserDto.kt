package com.andrewkingmarshall.beachbuddy2.network.dtos

import com.andrewkingmarshall.beachbuddy2.network.dtos.ScoreDto

data class UserDto(

    val id: String,

    val firstName: String,

    val lastName: String,

    val fullName: String,

    val skinType: Int,

    val phoneNumber: String,

    val photoUrl: String,

    val scores: List<ScoreDto>
)