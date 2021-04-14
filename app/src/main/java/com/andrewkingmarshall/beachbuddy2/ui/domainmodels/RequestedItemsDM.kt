package com.andrewkingmarshall.beachbuddy2.ui.domainmodels

import com.andrewkingmarshall.beachbuddy2.database.model.RequestedItem

data class RequestedItemsDM (
    val nonCompletedItems: List<RequestedItem>,
    val completedItems: List<RequestedItem>,
)
