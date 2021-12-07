package com.yernazar.pidapplication.repo.api.shapeResponse

import com.google.gson.annotations.SerializedName

data class Properties(
    @SerializedName("shape_dist_traveled")
    val shapeDistTraveled: Double,
    val shape_id: String,
    val shape_pt_sequence: Int
)