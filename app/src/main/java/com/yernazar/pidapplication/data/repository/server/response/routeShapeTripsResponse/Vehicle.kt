package com.yernazar.pidapplication.data.repository.server.response.routeShapeTripsResponse

import com.google.android.gms.maps.model.LatLng
import com.yernazar.pidapplication.R
import com.yernazar.pidapplication.utils.BaseClusterItem
import java.sql.Timestamp

data class Vehicle(
    val agencyName: String,
    val allPosition: Int,
    val bearing: Int,
    val canceled: Boolean,
    val cisLineId: String,
    val cisTripNumber: Int,
    val delay: Int,
    val delayLastStop: Int,
    val distTraveled: Double,
    val id: String,
    val lastModifiedTimestamp: Timestamp,
    val lastStop: LastStop,
    val lastStopDeparture: String,
    val lat: Double,
    val lon: Double,
    val nextStop: NextStop,
    val nextStopArrival: Timestamp,
    val originRouteName: String,
    val registrationNumber: String,
    val scheduledAgencyName: String,
    val speed: Int,
    val startTimestamp: Timestamp,
    val tracking: Boolean,
    val trip: Trip,
    val tripSequenceId: Int,
    val vehicleType: String
) : BaseClusterItem() {
    override fun getPosition(): LatLng =
        LatLng(lat, lon)

    override fun getTitle(): String =
        originRouteName

    override fun getSnippet(): String =
        originRouteName

    override val drawable: Int
        get() = R.drawable.ic_vehicle_point

    override val color: Int
        get() = R.color.red
}