package com.yernazar.pidapplication.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.yernazar.pidapplication.R
import com.yernazar.pidapplication.adapter.SearchResultAdapter
import com.yernazar.pidapplication.databinding.ActivityMapsBinding
import com.yernazar.pidapplication.fragment.SearchResultsFragment
import com.yernazar.pidapplication.fragment.TripFragment
import com.yernazar.pidapplication.repo.database.AppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jguniverse.pidapplicationgm.repo.model.*
import org.jguniverse.pidapplicationgm.utils.BitmapHelper
import org.jguniverse.pidapplicationgm.utils.StopLoader
import org.jguniverse.pidapplicationgm.utils.StopRenderer
import java.sql.Timestamp

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener, SearchResultAdapter.OnSearchItemClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val searchResultsFragment = SearchResultsFragment()
    private val searchTextChangeListener = searchResultsFragment.getSearchChangedListener()

    private lateinit var db: AppDatabase

    private var layer: GeoJsonLayer? = null
    private val stops: List<Stop> by lazy {
        StopLoader(this).load()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BottomSheetBehavior.from(binding.root.rootView.findViewById(R.id.bottom_sheet)).apply {
            peekHeight = 200
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        supportFragmentManager.beginTransaction().replace(R.id.bottom_sheet_frame, searchResultsFragment).commit()
        searchResultsFragment.setListener(this)

        val searchEditText: EditText = binding.root.rootView.findViewById(R.id.search_et)
        searchEditText.doOnTextChanged { text, start, before, count ->
            searchTextChangeListener.onSearchChanged(text.toString())
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        db = AppDatabase.getInstance(this)
    }

    private fun prepareMap(map: GoogleMap) {
        mMap = map
        mMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map_style))


        // Add a marker and move the camera
        val prague = LatLng(50.073658, 14.418540)
        //loadMarkers(mMap)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(prague, 15f))


//        val bounds = LatLngBounds(
//                LatLng((49.974726), 14.709795), // NE bounds
//                LatLng((50.147639), 14.233386)  // SW bounds
//        )
//        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0))
        addClusteredMarkers(mMap)
    }
    /**
     * Adds markers to the map with clustering support.
     */
    private fun addClusteredMarkers(map: GoogleMap) {
        // Create the ClusterManager class and set the custom renderer
        val clusterManager = ClusterManager<Stop>(this, map)
        clusterManager.renderer =
            StopRenderer(
                this,
                map,
                clusterManager
            )

        // Set custom info window adapter
        //clusterManager.markerCollection.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))




        //todo
        // Add the places to the ClusterManager
//        val stop1 = Stop("1", "Václavské náměstí", LatLng(50.08167, 14.42528))
//        val stop2 = Stop("2", "Václavské náměstí", LatLng(50.08196, 14.42572))
//        clusterManager.addItems(listOf(stop1, stop2))
//        clusterManager.cluster()

        // Show polygon
        clusterManager.setOnClusterItemClickListener { item ->
            loadRoute(map)
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
    }    private fun loadStops(map: GoogleMap) {
        // call backend endpoint
        // parse response
        // load stops

        val stop1 = LatLng(50.08167, 14.42528)
        val stop2 = LatLng(50.08196, 14.42572)

        map.addMarker(
            MarkerOptions()
                .position(stop1)
                .title("Václavské náměstí")
                .icon(busIcon))
        map.addMarker(
            MarkerOptions()
                .position(stop2)
                .title("Václavské náměstí 2")
                .icon(busIcon))
    }

    private fun loadRoute(map: GoogleMap) {
        val polyline1: Polyline = map.addPolyline(
            PolylineOptions()
            .clickable(true)
            .add(
                LatLng(50.085165410265276, 14.42500591278076),
                LatLng(50.08704474330281, 14.428052902221678),
                LatLng(50.08861423970004, 14.428600072860718),
                LatLng(50.09129190218363, 14.427666664123535),
                LatLng(50.09514637209521, 14.426765441894531),
                LatLng(50.09806455021389, 14.437322616577147)))
        polyline1.tag = "route"
        polyline1.color = -0x1000000
        polyline1.width = 12.toFloat()
        map.setOnPolylineClickListener(this)



//        if (layer == null || !layer!!.isLayerOnMap) {
//            layer = GeoJsonLayer(map, R.raw.route, this)
//
//            layer!!.addLayerToMap()
//        } else {
//            layer!!.removeLayerFromMap()
//        }
    }

    private val busIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this, R.color.black)
        BitmapHelper.vectorToBitmap(this, R.drawable.ic_baseline_directions_bus_24, color)
    }

    override fun onPolylineClick(p0: Polyline) {
        p0.remove()
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        prepareMap(googleMap)
    }

    override fun onSearchItemClick(route: Route) {
        GlobalScope.launch {
            val trip = db.tripDao().getByRouteId(routeId = route.uid)
            val tripFragment = TripFragment(trip)
            supportFragmentManager.beginTransaction().replace(R.id.bottom_sheet_frame, tripFragment).commit()
        }
    }
}