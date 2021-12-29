package com.yernazar.pidapplication.di

import com.yernazar.pidapplication.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {

    factory {
        GetAllStopsUseCase(appRepository =  get())
    }

    factory {
        GetRouteByIdUseCase(appRepository =  get())
    }

    factory {
        GetRouteByNameLikeUseCase(appRepository =  get())
    }

    factory {
        GetRouteNextArriveUseCase(appRepository =  get())
    }

    factory {
        GetShapesByIdUseCase(appRepository =  get())
    }

    factory {
        GetRouteShapeVehiclesByRouteIdUseCase(appRepository =  get())
    }

    factory {
        SignInUseCase(appRepository =  get())
    }

    factory {
        SignUpUseCase(appRepository =  get())
    }

}