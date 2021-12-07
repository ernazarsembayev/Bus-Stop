package com.yernazar.pidapplication.interfaces

import org.jguniverse.pidapplicationgm.repo.model.Route

interface OnRouteSelectListener{
    fun onRouteSelect(route: Route)
}