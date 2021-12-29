package com.yernazar.pidapplication.data.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.google.maps.android.clustering.ClusterItem

@Entity
data class Stop(
    @PrimaryKey
    @SerializedName("id")
    val uid: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("zoneId")
    val zoneId: String,
    @SerializedName("weelchair")
    val wheelchair: Int,
    @SerializedName("parentStation")
    val parentLocation: String
    ) : ClusterItem {

    override fun getPosition(): LatLng =
            LatLng(lat, lon)

    override fun getTitle(): String =
            name

    override fun getSnippet(): String =
            name

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Stop

        if (uid != other.uid) return false
        if (name != other.name) return false
        if (lat != other.lat) return false
        if (lon != other.lon) return false
        if (zoneId != other.zoneId) return false
        if (wheelchair != other.wheelchair) return false
        if (parentLocation != other.parentLocation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + lat.hashCode()
        result = 31 * result + lon.hashCode()
        result = 31 * result + zoneId.hashCode()
        result = 31 * result + wheelchair
        result = 31 * result + parentLocation.hashCode()
        return result
    }


}
