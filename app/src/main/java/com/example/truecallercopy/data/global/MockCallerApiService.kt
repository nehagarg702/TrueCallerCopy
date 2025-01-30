package com.example.truecallercopy.data.global

import com.example.truecallercopy.data.model.CallerInfo
import retrofit2.Response

class MockCallerApiService : CallerNetworkService {
    override suspend fun getCallerData(phoneNumber: String): Response<CallerInfo> {
        // Simulate mock data
        val mockData = CallerInfo(
            phoneNumber = phoneNumber,
            name = "Name ",
            location = "Location",
            isSpam =true
        )
        return Response.success(mockData)
    }
}
