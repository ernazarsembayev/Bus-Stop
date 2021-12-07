package org.jguniverse.pidapplicationgm.repo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Shape(
        val uid: String,
        val lat: Double,
        val lon: Double,
        val distTraveled: Double,
        val ptSequence: Int
) {
        @PrimaryKey(autoGenerate = true)
        var id :Int = 0
}