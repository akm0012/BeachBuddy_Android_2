package com.andrewkingmarshall.beachbuddy2.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

data class UserWithScores(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId" // This is the field in Score that references the parent id.
    )
    val scores: List<Score>
)