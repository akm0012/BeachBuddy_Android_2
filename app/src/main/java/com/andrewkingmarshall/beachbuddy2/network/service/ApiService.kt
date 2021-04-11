package com.andrewkingmarshall.beachbuddy2.network.service

import com.andrewkingmarshall.beachbuddy.network.dtos.DashboardDto
import com.andrewkingmarshall.beachbuddy.network.dtos.RequestedItemDto
import com.andrewkingmarshall.beachbuddy.network.requests.AddDeviceRequest
import com.andrewkingmarshall.beachbuddy.network.requests.AddGameRequest
import com.andrewkingmarshall.beachbuddy.network.requests.UpdateRequestedItemRequest
import com.andrewkingmarshall.beachbuddy.network.requests.UpdateScoreRequest
import retrofit2.Callback
import retrofit2.Retrofit
import javax.inject.Inject

class ApiService @Inject constructor(retrofit: Retrofit) {

    private val apiServiceInterface by lazy {
        retrofit.create(ApiServiceEndpointInterface::class.java)
    }

    suspend fun getNotCompletedRequestedItems(): List<RequestedItemDto> {
        return apiServiceInterface.getNonCompletedRequestedItems()
    }

    suspend fun updateRequestedItem(
        requestedItemId: String,
        request: UpdateRequestedItemRequest
    ): RequestedItemDto {
        return apiServiceInterface.updateRequestedItem(requestedItemId, request)
    }

    suspend fun addDevice(addDeviceRequest: AddDeviceRequest): Void {
        return apiServiceInterface.addDevice(addDeviceRequest)
    }

    suspend fun getDashboard(lat: Double, lon: Double): DashboardDto {
        return apiServiceInterface.getDashboard(lat, lon)
    }

    suspend fun updateScore(scoreId: String, updateScoreRequest: UpdateScoreRequest): Void {
        return apiServiceInterface.updateScore(scoreId, updateScoreRequest)
    }

    suspend fun addGame(addGameRequest: AddGameRequest): Void {
        return apiServiceInterface.addGame(addGameRequest)
    }
}
