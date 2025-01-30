package com.example.truecallercopy.data.repository

import com.example.truecallercopy.data.global.CallerNetworkService
import com.example.truecallercopy.data.local.dao.CallerDao
import com.example.truecallercopy.data.model.CallerInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CallerRepositoryImpl(
    private val api: CallerNetworkService,
    private val dao: CallerDao
) : CallerRepository {

    override suspend fun getCallerData(phoneNumber: String): CallerInfo {
        return withContext(Dispatchers.IO) {
            try {
                // Try to get data from the network first
                val response = api.getCallerData(phoneNumber)
                if (response.isSuccessful) {
                    response.body()?.let { callerInfo ->
                        // If network data is received, store it in the database and return it
                        dao.insertCallerInfo(callerInfo)
                        return@withContext callerInfo
                    }
                }

                // If no network data, try to get data from the database
                val callerFromDb = dao.getCallerInfo(phoneNumber)

                // If data is found in the database, return it
                if (callerFromDb != null) {
                    return@withContext callerFromDb
                }

                // If no data found in both network and database, return a caller with the phone number as name
                return@withContext CallerInfo(phoneNumber, phoneNumber, "", false)

            } catch (e: Exception) {
                e.printStackTrace()
                // If an error occurs, return a caller with the phone number as name and other details blank
                return@withContext CallerInfo(phoneNumber, phoneNumber, "", false)
            }
        }
    }
}
