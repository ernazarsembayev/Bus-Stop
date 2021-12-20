package com.yernazar.pidapplication.di

import com.yernazar.pidapplication.App
import com.yernazar.pidapplication.domain.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { App() }

    viewModel {
        MainViewModel(
            application = get(),
            getRouteByIdUseCase = get(),
            getTripByRouteIdUseCase = get(),
            getAllStopsUseCase = get(),
            getRouteByNameLikeUseCase = get(),
            getRouteNextArriveUseCase = get(),
            getShapesByIdUseCase =  get(),
        )
    }
}