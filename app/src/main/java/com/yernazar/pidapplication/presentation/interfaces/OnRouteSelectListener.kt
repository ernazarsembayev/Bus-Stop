package com.yernazar.pidapplication.presentation.interfaces

import com.yernazar.pidapplication.data.repository.model.Route

interface OnRouteSelectListener{
    fun onRouteSelect(route: Route)
}