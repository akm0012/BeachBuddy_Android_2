package com.andrewkingmarshall.beachbuddy2.repository

import com.andrewkingmarshall.beachbuddy2.database.dao.UserDao
import com.andrewkingmarshall.beachbuddy2.database.model.RequestedItem
import com.andrewkingmarshall.beachbuddy2.network.requests.UpdateRequestedItemRequest
import com.andrewkingmarshall.beachbuddy2.network.service.ApiService
import com.andrewkingmarshall.beachbuddy2.ui.domainmodels.RequestedItemsDM
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class RequestedItemRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
) {


    fun getRequestedItemsDomainModel(): Flow<RequestedItemsDM> {
        GlobalScope.launch {
            refreshRequestedItems()
        }

        return userDao.getNotCompletedRequestedItems()
            .zip(userDao.getCompletedTodayRequestedItems()) { notCompletedItems, completedItems ->
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

}
