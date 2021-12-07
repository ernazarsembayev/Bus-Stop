package org.jguniverse.pidapplicationgm.repo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class Vehicle(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val tripId: String, // Trip Entity
        val originRouteName: String,
        val cisLineId: String,
        val cisTripNumber: Int,
        val lat: Double,
        val lon: Double,
        val speed: Int,
        val distTraveled: Double,
        val tracking: Boolean,
        val tripSequenceId: Int,
        val delay: Int,
        val delayLastStop: Int,
        val isCanceled: Boolean,
        val lastStopId: String, // (previous stop) Stop Entity
        val lastStopDeparture: Long, // (previous stop)
        val nextStopId: String, // Stop Entity
        val nextStopArrival: Long,
        val vehicleType: VehicleType,
        val allPosition: Int
)
