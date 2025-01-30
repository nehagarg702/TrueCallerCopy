package com.example.truecallercopy.data.global

import com.example.truecallercopy.data.model.CallerInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CallerNetworkService {
    @GET("getCallerData")
    suspend fun getCallerData(@Query("phoneNumber") phoneNumber: String): Response<CallerInfo>
}