package com.yernazar.pidapplication.data.repository.server.response.shapeResponse

data class FeatureShape(
    var geometry: Geometry,
    var properties: Properties,
    var type: String
)