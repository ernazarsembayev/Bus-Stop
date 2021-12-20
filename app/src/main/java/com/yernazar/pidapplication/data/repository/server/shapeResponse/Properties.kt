package com.yernazar.pidapplication.data.repository.server.shapeResponse

import com.google.gson.annotations.SerializedName

data class Properties(
    @SerializedName("shape_dist_traveled")
    val shapeDistTraveled: Double,
    @SerializedName("shape_id")
    val shapeId: String,
    @SerializedName("shape_pt_sequence")
    val shapePtSequence: Int
)