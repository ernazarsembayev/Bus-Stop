package com.yernazar.pidapplication.di

import com.yernazar.pidapplication.data.repository.AppRepositoryImpl
import com.yernazar.pidapplication.data.repository.database.AppDatabase
import com.yernazar.pidapplication.data.repository.server.ServerCommunicator
import com.yernazar.pidapplication.domain.repository.AppRepository
import org.koin.dsl.module

val dataModule = module {

    single { ServerCommunicator() }

    single { AppDatabase.getInstance( get() ) }

    single<AppRepository> {
        AppRepositoryImpl(
            serverCommunicator = get(),
            database = get()) }

}