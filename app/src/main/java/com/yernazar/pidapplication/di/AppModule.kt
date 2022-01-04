package com.yernazar.pidapplication.di

import com.yernazar.pidapplication.App
import com.yernazar.pidapplication.domain.LoginViewModel
import com.yernazar.pidapplication.domain.SharedViewModel
import com.yernazar.pidapplication.domain.usecases.ClearFavouritesUseCase
import com.yernazar.pidapplication.domain.usecases.SaveFavouriteRoutesUseCase
import com.yernazar.pidapplication.domain.usecases.SaveFavouriteTripsUseCase
import com.yernazar.pidapplication.presentation.fragment.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { App() }

    viewModel {
        SharedViewModel(application = get())
    }

    viewModel {
        LoginViewModel(application = get())
    }

    factory {
        RouteFragment()
    }

    factory {
        SearchResultsFragment()
    }

    factory {
        StopFragment()
    }

    factory {
        TripFragment()
    }

    factory {
        LoginFragment()
    }

    factory {
        SignUpFragment()
    }

    factory {
        FavouriteRoutesFragment()
    }
}