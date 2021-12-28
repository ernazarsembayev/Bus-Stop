package com.yernazar.pidapplication.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

/**
 * A custom cluster renderer for Stop objects.
 */
class IconRenderer<T : ClusterItem>(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<T>,
    @DrawableRes iconDrawable: Int,
    @ColorRes iconColor: Int,
) : DefaultClusterRenderer<T>(context, map, clusterManager) {

    /**
     * The icon to use for each cluster item
     */
    private val bicycleIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(context,
                iconColor
        )
        BitmapHelper.vectorToBitmap(
                context,
                iconDrawable,
                color
        )
    }

    /**
     * Method called before the cluster item (i.e. the marker) is rendered. This is where marker
     * options should be set
     */
    override fun onBeforeClusterItemRendered(item: T, markerOptions: MarkerOptions) {
        markerOptions.title(item.title)
                .position(item.position)
                .icon(bicycleIcon)
    }

    /**
     * Method called right after the cluster item (i.e. the marker) is rendered. This is where
     * properties for the Marker object should be set.
     */
    override fun onClusterItemRendered(clusterItem: T, marker: Marker) {
        marker.tag = clusterItem
    }
}