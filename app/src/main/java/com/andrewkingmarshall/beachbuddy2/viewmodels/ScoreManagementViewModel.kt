package com.andrewkingmarshall.beachbuddy2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.andrewkingmarshall.beachbuddy2.database.model.Score
import com.andrewkingmarshall.beachbuddy2.repository.DashboardRepository
import com.andrewkingmarshall.beachbuddy2.repository.ScoreRepository
import com.andrewkingmarshall.beachbuddy2.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ScoreManagementViewModel @Inject constructor(
    private val scoreRepository: ScoreRepository,
    private val dashboardRepository: DashboardRepository,
) : ViewModel() {

    val showToast: SingleLiveEvent<String> = SingleLiveEvent()

    val usersWithScores = dashboardRepository.userWithScoresFlow.asLiveData()

    fun onAddNewGame(gameName: String) {
        launchSimpleCall { scoreRepository.addGame(gameName) }
    }

    fun onScoreIncremented(score: Score) {
        launchSimpleCall { scoreRepository.updateScore(score.scoreId, score.winCount + 1) }
    }

    fun onScoreDecremented(score: Score) {
        val winCount = score.winCount - 1

        if (winCount < 0) {
            Timber.d("Score is already 0. Not updating.")
            return
        }

        launchSimpleCall { scoreRepository.updateScore(score.scoreId, winCount) }
    }

    private fun launchSimpleCall(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                block()
            } catch (error: Exception) {
                showToast.value = error.message
            }
        }
    }
}