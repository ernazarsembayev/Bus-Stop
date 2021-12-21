package com.yernazar.pidapplication.di

import com.yernazar.pidapplication.App
import com.yernazar.pidapplication.domain.SharedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { App() }

    viewModel {
        SharedViewModel(application = get())
    }
}