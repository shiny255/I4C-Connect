package com.example.i4c

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

/* ---------------- API KEYS ---------------- */

const val VIRUS_API_KEY = "f25567286fcd4c53c19f3ea9e6cf7f82f5823360959ab639171039fda3c249f4"
const val NINJA_API_KEY = "S6BEsFGtFeXQh6mhwu3FuqOMGNEhCBzfG0G7uQnq"

/* ---------------- RETROFIT CLIENT ---------------- */

object RetrofitClient {

    private val client = OkHttpClient.Builder().build()

    /* ---------- VIRUSTOTAL ---------- */

    private val virusRetrofit = Retrofit.Builder()
        .baseUrl("https://www.virustotal.com/api/v3/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val virusApi: VirusTotalApi =
        virusRetrofit.create(VirusTotalApi::class.java)


    /* ---------- API NINJA ---------- */

    private val ninjaRetrofit = Retrofit.Builder()
        .baseUrl("https://api.api-ninjas.com/v1/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val ninjaApi: ApiNinjasApi =
        ninjaRetrofit.create(ApiNinjasApi::class.java)

    private val phoneRetrofit = retrofit2.Retrofit.Builder()
        .baseUrl("https://ipqualityscore.com/api/json/")
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val phoneApi: PhoneApi = phoneRetrofit.create(PhoneApi::class.java)

}
