package com.andrewkingmarshall.beachbuddy2.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(primaryKeys = ["userId", "scoreId"])
data class UserScoreCrossRef(
    val userId: String,
    val scoreId: String,
)

data class UserWithScores(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "scoreId",
        associateBy = Junction(UserScoreCrossRef::class)
    )
    val scores: List<Score>
)