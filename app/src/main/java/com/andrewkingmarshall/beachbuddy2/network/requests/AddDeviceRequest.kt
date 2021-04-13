package com.andrewkingmarshall.beachbuddy2.network.requests

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddDeviceRequest(
    @SerializedName("deviceToken") val deviceToken: String
): Serializable