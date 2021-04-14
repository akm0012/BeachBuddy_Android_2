package com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels

import android.content.Context
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.database.model.RequestedItem
import org.joda.time.DateTime

class RequestedItemViewModel(
    private val context: Context,
    private val requestedItem: RequestedItem) {

    fun getTitle(): String {
        return if (requestedItem.count > 1) {
            "${requestedItem.name} (${requestedItem.count})"
        } else {
            requestedItem.name
        }
    }

    fun getSubTitle(): String {

        val dayAndTime = DateTime(requestedItem.createdAtTime).toString("EE h:mm a")

        return "${requestedItem.requestorFirstName} • $dayAndTime"
    }

    fun getProfilePhotoUrl(): String {
        return "${context.getString(R.string.base_endpoint)}${requestedItem.requestorPhotoUrl}"
    }

    fun isChecked(): Boolean {
        return requestedItem.isComplete
    }
}