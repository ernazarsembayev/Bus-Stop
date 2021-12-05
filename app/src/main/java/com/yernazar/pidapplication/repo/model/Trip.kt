package org.jguniverse.pidapplicationgm.repo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Trip(
        @PrimaryKey
        val uid: String,
        val routeId: String,
        val serviceId: String,
        val shapeId: String,
        val direction: Int,
        val exception: Int,
        val headsign: String,
        val wheelchair: Boolean,
        val bikesAllowed:Boolean,
        val blockId: String
)