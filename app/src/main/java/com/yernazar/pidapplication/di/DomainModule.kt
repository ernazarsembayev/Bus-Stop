package com.yernazar.pidapplication.di

import com.yernazar.pidapplication.domain.usecases.*
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
        GetFavouriteRouteByUid(appRepository =  get())
    }

    factory {
        GetFavouriteRoutes(appRepository =  get())
    }

    factory {
        SaveFavouriteRoute(appRepository =  get())
    }

    factory {
        DeleteFavouriteRoute(appRepository =  get())
    }

    factory {
        SignInUseCase(appRepository =  get())
    }

    factory {
        SignUpUseCase(appRepository =  get())
    }

}