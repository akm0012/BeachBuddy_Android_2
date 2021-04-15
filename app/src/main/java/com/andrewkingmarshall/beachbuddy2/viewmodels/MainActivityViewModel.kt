package com.andrewkingmarshall.beachbuddy2.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.andrewkingmarshall.beachbuddy2.repository.FirebaseRepository
import com.andrewkingmarshall.beachbuddy2.repository.RequestedItemRepository
import com.andrewkingmarshall.beachbuddy2.work.PruneDatabaseWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.time.hours

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseRepository: FirebaseRepository,
    private val requestedItemRepository: RequestedItemRepository,
) : ViewModel() {

    val notCompletedItemCount = requestedItemRepository.getRequestedItemsDomainModel()
        .map { it.nonCompletedItems.count() }
        .asLiveData()

    init {
        viewModelScope.launch {
            firebaseRepository.registerDevice()
        }
        scheduleDatabasePruning()
    }

    private fun scheduleDatabasePruning() {
        Timber.d("Scheduling prune database work.")

        val pruneWorkRequest = PeriodicWorkRequestBuilder<PruneDatabaseWorker>(
            4, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(context).enqueue(pruneWorkRequest)
    }

}