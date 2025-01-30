package com.example.truecallercopy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.truecallercopy.data.local.dao.CallerDao
import com.example.truecallercopy.data.model.CallerInfo

@Database(entities = [CallerInfo::class], version = 1)
abstract class CallerDatabase : RoomDatabase() {
    abstract fun callerDao(): CallerDao
}
