package com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels

import android.content.Context
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.database.model.Score
import com.andrewkingmarshall.beachbuddy2.database.model.UserWithScores

class LeaderBoardItemViewModel(
    private val context: Context,
    userWithScore: UserWithScores
) {

    val user = userWithScore.user
    val scores = userWithScore.scores

    fun getName(): String = user.firstName

    fun getSubtitle(): String {

        var highScore = Score()

        for (score in scores) {
            if (score.winCount > highScore.winCount) {
                highScore = score
            }
        }

        val highScoreGame = when (highScore.winCount) {
            0 -> return "No wins yet..."
            else -> highScore.name
        }

        return "$highScoreGame (${highScore.winCount})"
    }

    fun getScore(): String {

        var totalWinCount = 0

        for (score in scores) {
            totalWinCount += score.winCount
        }

        return "$totalWinCount"
    }

    fun getProfilePhotoUrl(): String {
        return "${context.getString(R.string.base_endpoint)}${user.photoUrl}"
    }

}