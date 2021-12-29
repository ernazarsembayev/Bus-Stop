package com.yernazar.pidapplication.data.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.google.maps.android.clustering.ClusterItem
import java.sql.Timestamp

@Entity
data class Vehicle(
    @PrimaryKey
        val id: String,
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
        val lastStopDeparture: String, // (previous stop)
        val nextStopId: String, // Stop Entity
        val nextStopArrival: String,
        val vehicleType: String,
        val allPosition: Int
) : ClusterItem {
    override fun getPosition(): LatLng =
        LatLng(lat, lon)

    override fun getTitle(): String =
        originRouteName

    override fun getSnippet(): String =
        originRouteName

}
