package com.andrewkingmarshall.beachbuddy2.enums

enum class NotificationType {
    RequestedItemAdded,
    RequestedItemCompleted,
    RequestedItemRemoved,
    DashboardPulledToRefresh,
    ScoreUpdated,
    Unknown,
}

fun String.toNotificationType(): NotificationType {
    when (this) {
       "RequestedItemAdded" -> return NotificationType.RequestedItemAdded
       "RequestedItemCompleted" -> return NotificationType.RequestedItemCompleted
       "RequestedItemRemoved" -> return NotificationType.RequestedItemRemoved
       "DashboardPulledToRefresh" -> return NotificationType.DashboardPulledToRefresh
       "ScoreUpdated" -> return NotificationType.ScoreUpdated
    }

    return NotificationType.Unknown
}