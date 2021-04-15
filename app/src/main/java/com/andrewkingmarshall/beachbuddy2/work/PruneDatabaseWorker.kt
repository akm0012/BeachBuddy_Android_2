package com.andrewkingmarshall.beachbuddy2.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.andrewkingmarshall.beachbuddy2.repository.RequestedItemRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.lang.Exception

@HiltWorker
class PruneDatabaseWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val itemRepository: RequestedItemRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Timber.d("Starting to prune the data base.")

        try {
            itemRepository.deleteAllOldCompletedItems()
        } catch (cause: Exception) {
            Timber.w(cause, "Database prune failed. Retrying...")
            return Result.retry()
        }

        Timber.d("Database prune complete.")
        return Result.success()
    }

}