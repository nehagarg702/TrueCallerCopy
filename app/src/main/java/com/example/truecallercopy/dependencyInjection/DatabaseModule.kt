package com.example.truecallercopy.dependencyInjection

import androidx.room.Room
import com.example.truecallercopy.data.local.CallerDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(get(), CallerDatabase::class.java, "caller_database").build()
    }
    single { get<CallerDatabase>().callerDao() }
}