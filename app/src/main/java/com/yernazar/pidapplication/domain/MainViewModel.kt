package com.yernazar.pidapplication.domain

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.clustering.ClusterManager
import com.yernazar.pidapplication.R
import com.yernazar.pidapplication.data.repository.model.RouteAndNextArrive
import com.yernazar.pidapplication.domain.usecase.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jguniverse.pidapplicationgm.repo.model.Route
import org.jguniverse.pidapplicationgm.repo.model.Stop
import org.jguniverse.pidapplicationgm.repo.model.Trip
import org.jguniverse.pidapplicationgm.utils.StopRenderer

class MainViewModel(application: Application,
                    private val getRouteByIdUseCase: GetRouteByIdUseCase,
                    private val getTripByRouteIdUseCase: GetTripByRouteIdUseCase,
                    private val getAllStopsUseCase: GetAllStopsUseCase,
                    private val getRouteByNameLikeUseCase: GetRouteByNameLikeUseCase,
                    private val getRouteNextArriveUseCase: GetRouteNextArriveUseCase,
                    private val getShapesByIdUseCase: GetShapesByIdUseCase,
)
    : BaseViewModel(application), GoogleMap.OnPolylineClickListener {

    private lateinit var mMap: GoogleMap

    private val _liveDataPolyline = MutableLiveData<Polyline>()
    private val _liveDataBottomSheetState = MutableLiveData<Int>()
    private val _liveDataStop = MutableLiveData<Stop>()
    private val _liveDataTrip = MutableLiveData<Trip>()
    private val _liveDataRoute = MutableLiveData<Route>()
    private val _liveDataStopRoutes = MutableLiveData<List<RouteAndNextArrive>>()
    private val _liveDataSearchRoute = MutableLiveData<List<Route>>()

    val liveDataBottomSheetState: LiveData<Int> = _liveDataBottomSheetState
    val liveDataStop: LiveData<Stop> = _liveDataStop
    val liveDataTrip: LiveData<Trip?> = _liveDataTrip
    val liveDataRoute: LiveData<Route> = _liveDataRoute
    val liveDataStopRoutes: LiveData<List<RouteAndNextArrive>> = _liveDataStopRoutes
    val liveDataSearchRoute: LiveData<List<Route>> = _liveDataSearchRoute

    fun onRouteSelect(route: Route) {
        CoroutineScope(Dispatchers.Default).launch {

            _liveDataTrip.postValue(
                getTripByRouteIdUseCase.execute(route.uid)
            )
            _liveDataTrip.value?.let {

                _liveDataRoute.postValue(
                    getRouteByIdUseCase.execute(it.routeId)
                )

                val shapes = getShapesByIdUseCase.execute(it.shapeId)

                if (shapes.isNotEmpty()) {

                    CoroutineScope(Dispatchers.Main).launch {
                        _liveDataBottomSheetState.postValue(BottomSheetBehavior.STATE_HALF_EXPANDED)

                        val polylineOptions = PolylineOptions().clickable(true)
                        for (shape in shapes) {
                            polylineOptions.add(LatLng(shape.lat, shape.lon))
                        }

                        if (_liveDataPolyline.value != null)
                            _liveDataPolyline.value!!.remove()
                        val polyline = mMap.addPolyline(polylineOptions)
                        polyline.tag = "route"
                        polyline.color = -0x1000000
                        polyline.width = 12.toFloat()
                        _liveDataPolyline.postValue(polyline)
                    }
                }
            }
        }
//            trip?.let {
//                loadRoute(trip)
//            }

    }

    fun onSearchChanged(requestText: String) {
        if (requestText.isNotEmpty()) {
            CoroutineScope(Dispatchers.Default).launch {
                val searchResult = getRouteByNameLikeUseCase.execute(requestText)
                if (searchResult != null) {
                    _liveDataSearchRoute.postValue(searchResult!!)
                    return@launch
                }
            }
        }
        _liveDataSearchRoute.postValue(emptyList())
    }

    fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                getContext(), R.raw.map_style))


        // Add a marker and move the camera
        val prague = LatLng(50.073658, 14.418540)
        //loadMarkers(mMap)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(prague, 15f))

        mMap.setOnPolylineClickListener(this)

        addClusteredMarkers(mMap)
    }

    /**
     * Adds markers to the map with clustering support.
     */
    private fun addClusteredMarkers(map: GoogleMap) {
        // Create the ClusterManager class and set the custom renderer
        val clusterManager = ClusterManager<Stop>(getContext(), map)
        clusterManager.renderer =
            StopRenderer(
                getContext(),
                map,
                clusterManager
            )

        // Set custom info window adapter
        //clusterManager.markerCollection.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))

        // Add the places to the ClusterManager
        CoroutineScope(Dispatchers.Default).launch {
            val stops = getAllStopsUseCase.execute()
            if (stops.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    clusterManager.addItems(stops)
                    clusterManager.cluster()
                }
            }
        }

        // Show polygon
        clusterManager.setOnClusterItemClickListener { stop ->
            onClusterItemClick(stop)
            return@setOnClusterItemClickListener false
        }

        // When the camera starts moving, change the alpha value of the marker to translucent
        map.setOnCameraMoveStartedListener {
            clusterManager.markerCollection.markers.forEach { it.alpha = 0.3f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 0.3f }
        }

        map.setOnCameraIdleListener {
            // When the camera stops moving, change the alpha value back to opaque
            clusterManager.markerCollection.markers.forEach { it.alpha = 1.0f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 1.0f }

            // Call clusterManager.onCameraIdle() when the camera stops moving so that re-clustering
            // can be performed when the camera stops moving
            clusterManager.onCameraIdle()
        }
    }

    private fun onClusterItemClick(stop: Stop) {
        // On bus stop select
        _liveDataStop.postValue(stop)
        CoroutineScope(Dispatchers.Default).launch {
            _liveDataStopRoutes.postValue(getRouteNextArriveUseCase.execute(stopUid = stop.uid))
        }
    }

    fun onFragmentChanged(newBottomSheetState: Int) {
        _liveDataBottomSheetState.value = newBottomSheetState
    }


    override fun onPolylineClick(p0: Polyline) {
        Log.e("onPolylineClick", p0.id)
        p0.remove()
    }
}