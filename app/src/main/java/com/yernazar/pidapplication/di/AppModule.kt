package com.yernazar.pidapplication.di

import com.yernazar.pidapplication.App
import com.yernazar.pidapplication.domain.SharedViewModel
import com.yernazar.pidapplication.presentation.fragment.RouteFragment
import com.yernazar.pidapplication.presentation.fragment.SearchResultsFragment
import com.yernazar.pidapplication.presentation.fragment.StopFragment
import com.yernazar.pidapplication.presentation.fragment.TripFragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { App() }

    viewModel {
        SharedViewModel(application = get())
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
}