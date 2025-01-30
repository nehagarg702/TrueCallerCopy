package com.example.truecallercopy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "caller_info")
data class CallerInfo(
    @PrimaryKey val phoneNumber: String,
    val name: String,
    val location: String,
    val isSpam: Boolean
)
