package com.andrewkingmarshall.beachbuddy2.repository

import com.andrewkingmarshall.beachbuddy2.database.dao.UserDao
import com.andrewkingmarshall.beachbuddy2.database.model.RequestedItem
import com.andrewkingmarshall.beachbuddy2.network.requests.UpdateRequestedItemRequest
import com.andrewkingmarshall.beachbuddy2.network.service.ApiService
import com.andrewkingmarshall.beachbuddy2.ui.domainmodels.RequestedItemsDM
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestedItemRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
) {

    fun getRequestedItemsDomainModel(): Flow<RequestedItemsDM> {
        GlobalScope.launch {
            refreshRequestedItems()
        }

        val todayStartOfDay =
            DateTime(DateTime.now(DateTimeZone.getDefault()))
                .withTimeAtStartOfDay().millis

        val tomorrowStartOfDay =
            DateTime(DateTime.now(DateTimeZone.getDefault())).plusDays(1)
                .withTimeAtStartOfDay().millis

        return userDao.getNotCompletedRequestedItems()
            .zip(userDao.getCompletedTodayRequestedItems(todayStartOfDay, tomorrowStartOfDay)) { notCompletedItems, completedItems ->
                RequestedItemsDM(notCompletedItems, completedItems)
            }
    }

    suspend fun refreshRequestedItems() {
        try {
            val itemDtos = apiService.getNotCompletedRequestedItems()

            val itemsToSave = ArrayList<RequestedItem>()
            itemDtos.forEach {
                try {
                    itemsToSave.add(RequestedItem(it))
                } catch (e: Exception) {
                    Timber.w(e, "Unable to process item. Skipping it. $it")
                }
            }
            userDao.insertRequestedItems(itemsToSave)

            // Purge all the invalid Items that were deleted else where
            val invalidItems = userDao.getNotCompletedRequestedItems().first().toMutableList()
            itemsToSave.forEach {
                // Remove any valid items that were just pulled down
                invalidItems.remove(it)
            }
            userDao.deleteRequestedItems(invalidItems)

        } catch (cause: Exception) {
            Timber.w(cause, "Unable to refresh the requested items.")
            throw cause
        }
    }

    suspend fun markRequestedItemAsComplete(requestedItem: RequestedItem) {

        val requestedItemId = requestedItem.id
        val updateItemRequest = UpdateRequestedItemRequest(
            name = requestedItem.name,
            count = requestedItem.count
        )

        try {
            val itemDto = apiService.updateRequestedItem(requestedItemId, updateItemRequest)
            userDao.insertRequestedItem(RequestedItem(itemDto))
            Timber.d("${itemDto.name} marked as completed.")
        } catch (cause: Exception) {
            Timber.w(cause, "Unable to mark the item as complete.")
            throw cause
        }
    }

    /**
     * Deletes all Requested Items that were completed before the start of today.
     */
    suspend fun deleteAllOldCompletedItems() {

        val todayStartOfDay =
            DateTime(DateTime.now(DateTimeZone.getDefault()))
                .withTimeAtStartOfDay().millis

        try {
            val oldCompletedItems = userDao.getOldCompletedRequestedItems(todayStartOfDay).first()
            userDao.deleteRequestedItems(oldCompletedItems)
        } catch (cause: Exception) {
            Timber.w(cause, "Unable to delete old completed Items.")
        }
    }

}
