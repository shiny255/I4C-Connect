package com.example.i4c

import retrofit2.Response
import retrofit2.http.*
import com.google.gson.annotations.SerializedName

interface VirusTotalApi {

    @FormUrlEncoded
    @POST("urls")
    suspend fun scanUrl(
        @Header("x-apikey") apiKey: String,
        @Field("url") url: String
    ): Response<ScanResponse>

    @GET("analyses/{id}")
    suspend fun getAnalysis(
        @Header("x-apikey") apiKey: String,
        @Path("id") id: String
    ): Response<AnalysisResponse>

    @GET("files/{hash}")
    suspend fun getFileReport(
        @Header("x-apikey") apiKey: String,
        @Path("hash") hash: String
    ): Response<FileResponse>

    @GET("urls/{id}")
    suspend fun getUrlReport(
        @Header("x-apikey") apiKey: String,
        @Path("id") urlId: String
    ): Response<AnalysisResponse>

}

// ---------------- URL SCAN ----------------

data class ScanResponse(
    val data: ScanData
)

data class ScanData(
    val id: String
)

data class AnalysisResponse(
    val data: AnalysisData
)

data class AnalysisData(
    val attributes: AnalysisAttributes
)

data class AnalysisAttributes(

    // 🔥 VERY IMPORTANT: Needed for polling
    val status: String,

    @SerializedName("last_analysis_stats")
    val lastAnalysisStats: AnalysisStats
)

data class AnalysisStats(
    val malicious: Int = 0,
    val suspicious: Int = 0,
    val harmless: Int = 0,
    val undetected: Int = 0
)

// ---------------- FILE SCAN ----------------

data class FileResponse(
    val data: FileData
)

data class FileData(
    val attributes: FileAttributes
)

data class FileAttributes(
    @SerializedName("last_analysis_stats")
    val lastAnalysisStats: AnalysisStats
)
