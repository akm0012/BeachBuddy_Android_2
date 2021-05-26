package com.andrewkingmarshall.beachbuddy2.network.service

import com.andrewkingmarshall.beachbuddy2.network.dtos.DashboardDto
import com.andrewkingmarshall.beachbuddy2.network.dtos.RequestedItemDto
import com.andrewkingmarshall.beachbuddy2.network.requests.AddDeviceRequest
import com.andrewkingmarshall.beachbuddy2.network.requests.AddGameRequest
import com.andrewkingmarshall.beachbuddy2.network.requests.UpdateRequestedItemRequest
import com.andrewkingmarshall.beachbuddy2.network.requests.UpdateScoreRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceEndpointInterface {

    @GET("api/requestedItems/notCompleted")
    suspend fun getNonCompletedRequestedItems(): List<RequestedItemDto>

    @POST("api/requestedItems/{requestedItemId}")
    suspend fun updateRequestedItem(
        @Path("requestedItemId") requestedItemId: String,
        @Body request: UpdateRequestedItemRequest
    ): RequestedItemDto

    @POST("api/devices")
    suspend fun addDevice(@Body addDeviceRequest: AddDeviceRequest): Response<Unit>

    @GET("api/dashboard")
    suspend fun getDashboard(@Query("lat") lat: Double, @Query("lon") lon: Double): DashboardDto

    @POST("api/updateScore/{scoreId}")
    suspend fun updateScore(
        @Path("scoreId") scoreId: String,
        @Body updateScoreRequest: UpdateScoreRequest
    ): Response<Unit>

    @POST("api/addScore")
    suspend fun addGame(
        @Body addGameRequest: AddGameRequest
    ): Response<Unit>

    @POST("api/dashboard/refresh")
    suspend fun refresh(): Response<Unit>

    @POST("api/sunscreenReminder/sunscreenApplied/{userId}")
    suspend fun setSunScreenReminder(
        @Path("userId") userId: String,
    ): Response<Unit>
}