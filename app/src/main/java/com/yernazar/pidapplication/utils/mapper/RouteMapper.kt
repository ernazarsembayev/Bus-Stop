package com.yernazar.pidapplication.utils.mapper

import com.yernazar.pidapplication.data.repository.model.RouteAndNextArrive
import com.yernazar.pidapplication.data.repository.model.Route

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