package com.yernazar.pidapplication.utils.mapper

import com.yernazar.pidapplication.data.repository.server.shapeResponse.ShapeResponse
import org.jguniverse.pidapplicationgm.repo.model.Shape

object ShapeMapper {
    fun toDatabaseShape(shapeResponse: ShapeResponse) : List<Shape> {
        return shapeResponse.features.map {
            Shape(

                uid = it.properties.shapeId,
                lat = it.geometry.coordinates[1],
                lon = it.geometry.coordinates[0],
                distTraveled = it.properties.shapeDistTraveled,
                ptSequence = it.properties.shapePtSequence
            )
        }
    }
}