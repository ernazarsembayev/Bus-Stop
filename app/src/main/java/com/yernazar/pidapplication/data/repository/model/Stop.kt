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
}
