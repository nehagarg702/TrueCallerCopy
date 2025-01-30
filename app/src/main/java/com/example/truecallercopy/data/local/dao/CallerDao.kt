package com.example.truecallercopy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.truecallercopy.data.model.CallerInfo

@Dao
interface CallerDao {

    @Query("SELECT * FROM caller_info WHERE phoneNumber = :phoneNumber")
    suspend fun getCallerInfo(phoneNumber: String): CallerInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallerInfo(callerInfo: CallerInfo)

    @Query("SELECT * FROM caller_info")
    suspend fun getAllCallerInfo(): List<CallerInfo>
}
