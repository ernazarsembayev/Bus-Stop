package com.yernazar.pidapplication.data.repository.server.shapeResponse

data class FeatureShape(
    var geometry: Geometry,
    var properties: Properties,
    var type: String
)