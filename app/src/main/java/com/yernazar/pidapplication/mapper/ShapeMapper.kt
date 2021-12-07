package com.yernazar.pidapplication.mapper

import com.yernazar.pidapplication.repo.api.shapeResponse.ShapeResponse
import org.jguniverse.pidapplicationgm.repo.model.Shape

object ShapeMapper {
    fun toDatabaseShape(shapeResponse: ShapeResponse) : List<Shape> {
        return shapeResponse.features.map {
            Shape(

                uid = it.properties.shape_id,
                lat = it.geometry.coordinates[1],
                lon = it.geometry.coordinates[0],
                distTraveled = it.properties.shapeDistTraveled,
                ptSequence = it.properties.shape_pt_sequence
            )
        }
    }
}