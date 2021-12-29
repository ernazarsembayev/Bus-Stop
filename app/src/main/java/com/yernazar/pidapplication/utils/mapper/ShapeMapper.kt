package com.yernazar.pidapplication.utils.mapper

import com.yernazar.pidapplication.data.repository.server.response.shapeResponse.ShapeResponse
import com.yernazar.pidapplication.data.repository.model.ShapeOld

object ShapeMapper {
    fun toDatabaseShape(shapeResponse: ShapeResponse) : List<ShapeOld> {
        return shapeResponse.features.map {
            ShapeOld(

                uid = it.properties.shapeId,
                lat = it.geometry.coordinates[1],
                lon = it.geometry.coordinates[0],
                distTraveled = it.properties.shapeDistTraveled,
                ptSequence = it.properties.shapePtSequence
            )
        }
    }
}