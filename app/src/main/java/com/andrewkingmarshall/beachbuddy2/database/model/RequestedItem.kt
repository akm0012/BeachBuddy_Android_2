package com.andrewkingmarshall.beachbuddy2.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andrewkingmarshall.beachbuddy.network.dtos.RequestedItemDto
import org.joda.time.DateTime

@Entity
data class RequestedItem(

    @PrimaryKey
    var id: String = "",

    var name: String = "",

    var count: Int = 0,

    var isComplete: Boolean = false,

    var createdAtTime: Long = 0L,

    var completedAtTime: Long? = null,

    var requestorId: String = "",

    var requestorFirstName: String = "",

    var requestorLastName: String = "",

    var requestorPhotoUrl: String = ""
) {
    constructor(itemDto: RequestedItemDto) : this() {
        id = itemDto.id
        name = itemDto.name
        count = itemDto.count
        isComplete = itemDto.isRequestCompleted
        createdAtTime = DateTime(itemDto.createdDateTime).millis
        requestorId = itemDto.requestedByUserId
        requestorFirstName = itemDto.requestedByUser.firstName
        requestorLastName = itemDto.requestedByUser.lastName
        requestorPhotoUrl = itemDto.requestedByUser.photoUrl

        if (itemDto.completedDateTime != null) {
            completedAtTime = DateTime(itemDto.completedDateTime).millis
        }
    }

}