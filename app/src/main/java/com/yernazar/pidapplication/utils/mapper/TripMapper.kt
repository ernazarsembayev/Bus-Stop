package com.yernazar.pidapplication.utils.mapper

import com.yernazar.pidapplication.data.repository.model.ShapeOld
import com.yernazar.pidapplication.data.repository.model.Trip

class TripMapper {
    companion object {
        fun toTrip(
            trip: com.yernazar.pidapplication.data.repository.
            server.response.routeShapeTripsResponse.Trip
        ): Trip {
            return Trip(
                uid = trip.uid,
                routeId = trip.route.id,
                serviceId = "",
                shapeId = trip.shapeId,
                direction = trip.direction,
                exception = trip.exceptional,
                headsign = trip.headsign,
                wheelchair = trip.wheelchair,
                bikesAllowed = trip.bikesAllowed,
                blockId = trip.blockId,
            )
        }
    }
}