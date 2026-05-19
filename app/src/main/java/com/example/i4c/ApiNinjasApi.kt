package com.example.i4c

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiNinjasApi {

    @GET("whois")
    suspend fun getWhois(
        @Header("X-Api-Key") apiKey: String,
        @Query("domain") domain: String
    ): Response<WhoisResponse>
}
data class WhoisResponse(
    val registrar: String?,
    val country: String?,
    val creation_date: Any?,
    val expiration_date: Any?
)

