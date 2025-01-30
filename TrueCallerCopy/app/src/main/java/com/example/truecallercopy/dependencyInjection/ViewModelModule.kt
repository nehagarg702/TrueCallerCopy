package com.example.truecallercopy.dependencyInjection

import com.example.truecallercopy.ui.viewmodel.CallerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { CallerViewModel(get()) }
}