package org.jguniverse.pidapplicationgm.repo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

@Entity
data class Stop(
    @PrimaryKey
    val uid: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val zoneId: String,
    val wheelchair: Int,
    val parentLocation: String
    ) : ClusterItem {

    override fun getPosition(): LatLng =
            LatLng(lat, lon)

    override fun getTitle(): String =
            name

    override fun getSnippet(): String =
            name
}
