package com.example.truecallercopy.dependencyInjection

import com.example.truecallercopy.data.repository.CallerRepository
import com.example.truecallercopy.data.repository.CallerRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<CallerRepository> { CallerRepositoryImpl(api = get(), dao = get()) }
}
