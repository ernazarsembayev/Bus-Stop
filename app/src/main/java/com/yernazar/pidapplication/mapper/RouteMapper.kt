package com.yernazar.pidapplication.mapper

import com.yernazar.pidapplication.repo.model.RouteAndNextArrive
import org.jguniverse.pidapplicationgm.repo.model.Route

object RouteMapper {
    fun toRoute(list: RouteAndNextArrive) : Route {
        return Route(
                uid = list.uid,
                longName = list.longName,
                shortName = list.shortName,
                desc = "",
                agency = "",
                color = "",
                textColor = "",
                type = list.type,
                url = list.url,
                isNight = list.isNight
            )
    }
}