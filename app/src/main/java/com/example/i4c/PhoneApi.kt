package com.example.i4c

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import com.google.gson.annotations.SerializedName

interface PhoneApi {

    // IPQualityScore Phone Validation Endpoint
    // Example:
    // https://ipqualityscore.com/api/json/phone/API_KEY/PHONE_NUMBER

    @GET("phone/{apiKey}/{phone}")
    suspend fun checkPhone(
        @Path("apiKey") apiKey: String,
        @Path("phone") phone: String
    ): Response<PhoneResponse>
}

// ---------------- RESPONSE MODEL ----------------

data class PhoneResponse(

    val success: Boolean = false,
    val valid: Boolean = false,

    @SerializedName("fraud_score")
    val fraudScore: Int = 0,

    @SerializedName("recent_abuse")
    val recentAbuse: Boolean = false,

    val spam: Boolean = false,

    @SerializedName("VOIP")
    val voip: Boolean = false,

    val carrier: String? = null,
    val country: String? = null,

    @SerializedName("line_type")
    val lineType: String? = null,

    val active: Boolean = false
)
