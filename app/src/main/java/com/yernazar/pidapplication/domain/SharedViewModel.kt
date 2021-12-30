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
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.yernazar.pidapplication.R
import com.yernazar.pidapplication.data.repository.server.response.routeShapeTripsResponse.RouteShapeVehicles
import com.yernazar.pidapplication.domain.usecases.*
import com.yernazar.pidapplication.data.repository.model.Route
import com.yernazar.pidapplication.data.repository.model.Stop
import com.yernazar.pidapplication.data.repository.server.response.routeShapeTripsResponse.Vehicle
import com.yernazar.pidapplication.utils.BaseClusterItem
import com.yernazar.pidapplication.utils.IconRenderer
import com.yernazar.pidapplication.utils.config.Config.SERVER_QUERY_TIME
import kotlinx.coroutines.*
import org.koin.core.component.inject

class SharedViewModel(application: Application)
    : BaseViewModel(application), GoogleMap.OnPolylineClickListener {

    private val getRouteByNameLikeUseCase: GetRouteByNameLikeUseCase by inject()
    private val getRouteNextArriveUseCase: GetRouteNextArriveUseCase by inject()
    private val mGetRouteShapeVehiclesByRouteIdUseCase: GetRouteShapeVehiclesByRouteIdUseCase by inject()
    private val getShapesByIdUseCase: GetShapesByIdUseCase by inject()
    private val getRouteByIdUseCase: GetRouteByIdUseCase by inject()
    private val getAllStopsUseCase: GetAllStopsUseCase by inject()

    private lateinit var mMap: GoogleMap

    private val _liveDataPolyline = MutableLiveData<Polyline>()
    private val _liveDataBottomSheetState = MutableLiveData<Int>()
    private val _liveDataStop = MutableLiveData<Stop>()
    private val _liveDataRouteShapeVehicles = MutableLiveData<RouteShapeVehicles>()
    private val _liveDataRoute = MutableLiveData<Route>()
    private val _liveDataStopRoutes = MutableLiveData<List<Route>>()
    private val _liveDataSearchRoute = MutableLiveData<List<Route>>()
    private val _liveDataVehicle = MutableLiveData<Vehicle>()

    val liveDataBottomSheetState: LiveData<Int> = _liveDataBottomSheetState
    val liveDataStop: LiveData<Stop> = _liveDataStop
    val liveDataRouteShapeVehicles: LiveData<RouteShapeVehicles> = _liveDataRouteShapeVehicles
    val liveDataRoute: LiveData<Route> = _liveDataRoute
    val liveDataStopRoutes: LiveData<List<Route>> = _liveDataStopRoutes
    val liveDataSearchRoute: LiveData<List<Route>> = _liveDataSearchRoute
    val liveDataVehicle: LiveData<Vehicle> = _liveDataVehicle

    fun onRouteSelect(route: Route) {
        CoroutineScope(Dispatchers.Default).launch {
            Log.e("route uid", route.uid)

            stopUpdateVehicles()

            val routeShapeTrips = mGetRouteShapeVehiclesByRouteIdUseCase.execute(route.uid)

            routeShapeTrips?.let {
                _liveDataRouteShapeVehicles.postValue(
                    it
                )

                _liveDataRoute.postValue(
                    it.route
                )

                val vehicles = it.vehicles

                vehicles?.let {
                    if (vehicles.isNotEmpty()) {
                        CoroutineScope(Dispatchers.Main).launch {

                            addPoints(mMap, vehicles)
                            startUpdateVehicles(routeUid = route.uid)

                        }
                    }
                }

                val shapes = it.routeShape

                shapes?.let {

                    val polylineOptions = PolylineOptions().clickable(true)
                    for (shape in shapes) {
                        polylineOptions.add(LatLng(shape.lat, shape.lon))
                    }

                    CoroutineScope(Dispatchers.Main).launch {


                        _liveDataPolyline.value?.remove()
                        val polyline = mMap.addPolyline(polylineOptions)
                        polyline.tag = "route"
                        polyline.color = -0x1000000
                        polyline.width = 12.toFloat()
                        _liveDataPolyline.setValue(polyline)
                    }

                }
            }
        }
    }

    fun onSearchChanged(requestText: String) {
        if (requestText.isNotEmpty()) {
            CoroutineScope(Dispatchers.Default).launch {
                val searchResult = getRouteByNameLikeUseCase.execute(requestText)
                searchResult?.let {
                    _liveDataSearchRoute.postValue(it)
                    return@launch
                }
            }
        }
        _liveDataSearchRoute.value = emptyList()
    }

    private val scope = CoroutineScope(Dispatchers.Default) // could also use an other scope such as viewModelScope if available
    private var job: Job? = null

    private fun startUpdateVehicles(routeUid: String) {
        stopUpdateVehicles()
        job = scope.launch {
            while(true) {
                val routeShapeTrips = mGetRouteShapeVehiclesByRouteIdUseCase.execute(routeUid)

                routeShapeTrips?.let {

                    if (!it.vehicles.isNullOrEmpty()) {
                        CoroutineScope(Dispatchers.Main).launch {
                            addPoints(mMap, it.vehicles)
                        }
                    }

                }

                delay(SERVER_QUERY_TIME)
            }
        }
    }

    fun stopUpdateVehicles() {
        job?.cancel()
        job = null
    }

    fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setPadding(0,0,0, 200)

        mMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                getContext(), R.raw.map_style))


        // Add a marker and move the camera
        val prague = LatLng(50.073658, 14.418540)
        //loadMarkers(mMap)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(prague, 15f))

        mMap.setOnPolylineClickListener(this)


        CoroutineScope(Dispatchers.Default).launch {
            val stops = getAllStopsUseCase.execute()

            if (stops.isNotEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    addPoints(mMap, stops)
                }
            }

        }
    }

    private fun addPoints(map: GoogleMap, points: List<BaseClusterItem>) {
        // Create the ClusterManager class and set the custom renderer
        val clusterManager = ClusterManager<BaseClusterItem>(getContext(), map)
        clusterManager.renderer =
            IconRenderer(
                getContext(),
                map,
                clusterManager,
                points[0].drawable,
                points[0].color
            )

        // Set custom info window adapter
        //clusterManager.markerCollection.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))

        // Add the places to the ClusterManager
        clusterManager.addItems(points)
        clusterManager.cluster()

        // Show polygon
        clusterManager.setOnClusterItemClickListener { item ->
            onClusterItemClick(item)
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

    private fun <T : ClusterItem> onClusterItemClick(item: T) {
        // On cluster select
        when (item) {
            is Stop -> {
                _liveDataStop.value = item
                CoroutineScope(Dispatchers.Default).launch {
                    _liveDataStopRoutes.postValue(getRouteNextArriveUseCase.execute(stopUid = item.uid))
                }
            }
            is Vehicle -> _liveDataVehicle.value = item
        }
    }

    fun setBottomSheetState(newBottomSheetState: Int) {
        _liveDataBottomSheetState.value = newBottomSheetState
        onBottomSheetStateChanged(newBottomSheetState)
    }

    // For the Google logo on map don't overlap by bottomSheet
    fun onBottomSheetStateChanged(newBottomSheetState: Int) {
        when(newBottomSheetState) {
            BottomSheetBehavior.STATE_COLLAPSED -> mMap.setPadding(0,0,0, 200)
            BottomSheetBehavior.STATE_HALF_EXPANDED -> mMap.setPadding(0,0,0, 400)
        }
    }

    override fun onPolylineClick(p0: Polyline) {
        p0.remove()
    }

    override fun onCleared() {
        stopUpdateVehicles()
        super.onCleared()
    }
}