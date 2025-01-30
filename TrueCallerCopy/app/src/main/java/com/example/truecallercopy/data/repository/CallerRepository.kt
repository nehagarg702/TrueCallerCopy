package com.example.truecallercopy.data.repository

import com.example.truecallercopy.data.model.CallerInfo

interface CallerRepository {
    /**
     * Fetch caller data by phone number.
     * - First attempts to fetch from the network.
     * - Falls back to the local database if network fails.
     */
    suspend fun getCallerData(phoneNumber: String): CallerInfo?
}
