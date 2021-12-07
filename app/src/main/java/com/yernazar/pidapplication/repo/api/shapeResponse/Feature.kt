package com.yernazar.pidapplication.repo.api.shapeResponse

data class FeatureShape(
    var geometry: Geometry,
    var properties: Properties,
    var type: String
)