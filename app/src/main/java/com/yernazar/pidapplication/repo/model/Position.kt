package org.jguniverse.pidapplicationgm.repo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Position(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val tripId: String, // Trip Entity
        val lat: Double,
        val lon: Double,
        val tripSequenceId: Long,
        val distTraveled: Double,
        val nextStopId: String, // Stop Entity
        val prevStopId: String, // Stop Entity
        val isCanceled: Boolean
)
