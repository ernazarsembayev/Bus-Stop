package com.yernazar.pidapplication.di

import com.yernazar.pidapplication.App
import com.yernazar.pidapplication.domain.LoginSharedViewModel
import com.yernazar.pidapplication.domain.MapsSharedViewModel
import com.yernazar.pidapplication.presentation.fragment.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { App() }

    viewModel {
        MapsSharedViewModel(application = get())
    }

    viewModel {
        LoginSharedViewModel(application = get())
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