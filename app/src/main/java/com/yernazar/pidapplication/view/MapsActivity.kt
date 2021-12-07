package com.yernazar.pidapplication.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.GsonBuilder
import com.google.maps.android.clustering.ClusterManager
import com.yernazar.pidapplication.R
import com.yernazar.pidapplication.config.Config
import com.yernazar.pidapplication.databinding.ActivityMapsBinding
import com.yernazar.pidapplication.fragment.SearchResultsFragment
import com.yernazar.pidapplication.fragment.StopFragment
import com.yernazar.pidapplication.fragment.TripFragment
import com.yernazar.pidapplication.interfaces.OnRouteSelectListener
import com.yernazar.pidapplication.mapper.ShapeMapper
import com.yernazar.pidapplication.repo.api.shapeResponse.ShapeResponse
import com.yernazar.pidapplication.repo.database.AppDatabase
import com.yernazar.pidapplication.testData.LocalData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jguniverse.pidapplicationgm.repo.model.*
import org.jguniverse.pidapplicationgm.utils.StopRenderer


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener,
    OnRouteSelectListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<View>

    private val searchResultsFragment = SearchResultsFragment()
    private var tripFragment: TripFragment? = null
    private var stopFragment: StopFragment? = null


    private val searchTextChangeListener = searchResultsFragment.getSearchChangedListener()

    private lateinit var db: AppDatabase

    private var polyline: Polyline? = null

    private lateinit var searchEditText: EditText


    @SuppressLint("ClickableViewAccessibility")
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

        setBottomSheet()

        searchResultsFragment.setListener(this)

        searchEditText = binding.root.rootView.findViewById(R.id.search_et)
        searchEditText.onFocusChangeListener { hasFocus ->
            if (hasFocus) {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.bottom_sheet_frame,
                        searchResultsFragment,
                        Config.searchResultsFragmentName
                    ).commit()
                mBottomSheetBehavior.state = searchResultsFragment.bottomSheetState
            }
        }
        searchEditText.doOnTextChanged { text, _, _, _ ->
            searchTextChangeListener.onSearchChanged(text.toString())
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        db = AppDatabase.getInstance(this)
        populateDatabase(db)
    }

    override fun onRouteSelect(route: Route) {
        GlobalScope.launch {
            val trip = db.tripDao().getByRouteId(route.uid)
            if(trip != null) {
                withContext(Main) {
//                    tripFragment = TripFragment(trip, route)
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.bottom_sheet_frame, tripFragment!!, Config.tripFragmentName).addToBackStack(null)
//                        .commit()
//                    mBottomSheetBehavior.state = tripFragment!!.bottomSheetState
                    val imm =
                        this@MapsActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(this@MapsActivity.currentFocus?.windowToken, 0)
                    searchEditText.clearFocus()
                    loadRoute(trip)
                }
            }
        }
    }

    @DelicateCoroutinesApi
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
//        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20000, 20000, 15))
        addClusteredMarkers(mMap)
    }
    /**
     * Adds markers to the map with clustering support.
     */
    @DelicateCoroutinesApi
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
        GlobalScope.launch {
            val stopDao = db.stopDao()
            val stops = stopDao.getAll()
            if (stops != null && stops.isNotEmpty()) {
                withContext(Main) {
                    clusterManager.addItems(stops)
                    clusterManager.cluster()
                }
            }
        }

        // Show polygon
        clusterManager.setOnClusterItemClickListener { stop ->
            Log.e("OnClusterClick", stop.name)
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

    @DelicateCoroutinesApi
    private fun onClusterItemClick(stop: Stop) {
        // On bus stop select
//        val vehicleDao = db.vehicleDao()
//        val tripDao = db.tripDao()
//        GlobalScope.launch {
//            val vehicle = vehicleDao.getByNextStopId(stop.uid)
//            val trip = vehicle?.let { tripDao.getById(it.tripId) }
//            withContext(Main) {
//                if (trip != null) {
//                    loadRoute(trip)
//                } else {
//                    // If couldn't find route
//                    tripFragment?.let {
//                        supportFragmentManager.beginTransaction()
//                            .remove(it)
//                            .commit()
//                    }
//                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//                    polyline?.remove()
//                }
//            }
//        }

        stopFragment = StopFragment(stop, this)
        stopFragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.bottom_sheet_frame, stopFragment!!, Config.tripFragmentName).addToBackStack(null)
                .commit()
            mBottomSheetBehavior.state = stopFragment!!.bottomSheetState
        }
    }

    @DelicateCoroutinesApi
    private fun loadRoute(trip: Trip) {

        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED

        val shapeDao = db.shapeDao()
        val routeDao = db.routeDao()
        GlobalScope.launch {
            val shapes = shapeDao.getById(trip.shapeId)
            val route = routeDao.getById(trip.routeId)
            if (shapes != null && shapes.isNotEmpty()) {
                Log.e("shapes", shapes.size.toString())

                val polylineOptions = PolylineOptions().clickable(true)
                for (shape in shapes) {
                    polylineOptions.add(LatLng(shape.lat, shape.lon))
                }

                withContext(Main){
                    tripFragment = route?.let { TripFragment(trip, it) }
                    tripFragment?.let { supportFragmentManager.beginTransaction()
                        .replace(R.id.bottom_sheet_frame, tripFragment!!, Config.tripFragmentName).addToBackStack(null)
                        .commit()
                    mBottomSheetBehavior.state = tripFragment!!.bottomSheetState}

                    if (polyline != null)
                        polyline!!.remove()
                    polyline = mMap.addPolyline(polylineOptions)
                    polyline!!.tag = "route"
                    polyline!!.color = -0x1000000
                    polyline!!.width = 12.toFloat()
                }
            } else{
                Log.e("shapes", "empty")
            }
        }

        mMap.setOnPolylineClickListener(this)


//        if (layer == null || !layer!!.isLayerOnMap) {
//            layer = GeoJsonLayer(map, R.raw.route, this)
//
//            layer!!.addLayerToMap()
//        } else {
//            layer!!.removeLayerFromMap()
//        }
    }


//    private val busIcon: BitmapDescriptor by lazy {
//        val color = ContextCompat.getColor(this, R.color.black)
//        BitmapHelper.vectorToBitmap(this, R.drawable.ic_baseline_directions_bus_24, color)
//    }

    override fun onPolylineClick(p0: Polyline) {
        Log.e("onPolylineClick", p0.id)
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

    private fun setBottomSheet() {
        BottomSheetBehavior.from(binding.root.rootView.findViewById(R.id.bottom_sheet)).apply {
            val displayMetrics: DisplayMetrics = resources.displayMetrics
            val height = displayMetrics.heightPixels
            val maxHeight = (height * 0.95).toInt()
            mBottomSheetBehavior = this
            peekHeight = 200
            setMaxHeight(maxHeight)
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED){
                    if (currentFocus != null) {
                        searchEditText.clearFocus()
                        val imm =
                            this@MapsActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(this@MapsActivity.currentFocus?.windowToken, 0)
                    }
                } else {
                    this@MapsActivity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Do something for slide offset
            }
        }
        mBottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
    }

    override fun onBackPressed() {
        if (mBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED ||
            supportFragmentManager.backStackEntryCount > 0) {
                super.onBackPressed()
            if (supportFragmentManager.fragments.size > 0)
                mBottomSheetBehavior.state = when(supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1]?.tag.toString()){
                    Config.searchResultsFragmentName -> searchResultsFragment.bottomSheetState
                    Config.tripFragmentName -> tripFragment?.bottomSheetState!!
                    else -> BottomSheetBehavior.STATE_COLLAPSED
                }
        } else{
            mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    @DelicateCoroutinesApi
    private fun populateDatabase(db: AppDatabase) {


        val builder = GsonBuilder()
        val gson = builder.create()
        val shapes = gson.fromJson(LocalData.shapeJson, ShapeResponse::class.java)
//        val shapeJson = resources.openRawResource(R.raw.shape)
//        Log.e("shapeJson", shapeJson.toString())
//        val shapes = gson.fromJson(shapeJson.toString(), ShapeResponse::class.java)

        val databaseShapes = ShapeMapper.toDatabaseShape(shapes)

        for(shape in databaseShapes){
            Log.e("shapes", shape.ptSequence.toString() + " " + shape.lat.toString() + " " + shape.lon.toString())
        }
        // Bus stop "Václavské náměstí"
        // У остановки с названием "Václavské náměstí" есть 2 физические остановки (в одну сторону и другую)

        val stop1 = Stop("1","Václavské náměstí",
            50.08167, 14.42528, "zone_id",
            1, "parent location")

        val stop2 = Stop("U484Z2P","Václavské náměstí",
            50.08196, 14.42572, "zone_id",
            1, "parent location")

        val stop3 = Stop("U997Z2P","Myslíkova",
            50.078051, 14.41847, "zone_id",
            1, "parent location")

        val stop4 = Stop("4","Myslíkova",
            50.077998, 14.418441, "zone_id",
            1, "parent location")

        val trip = Trip("9_6952_211202", "L9",
            "1111100-1", "L9V2", 0,
            0, "Spojovací", true,
            bikesAllowed = false,  "")

        val route = Route("L9", "Sídliště Řepy - Spojovací",
            "9", "", "99", "7A0603",
            "FFFFFF", "0", "https://pid.cz/linka/9", isNight = false)

        val position = Position(12,"9_6952_211202", 50.07914, 14.42059,
            1, 10.4, "U484Z2P", "U997Z2P", false)

        val vehicle = Vehicle(12, "9_6952_211202", "9",
            "", 0, 50.07914, 14.42059,
            0, 10.4, true, 1,
            1, 1, false, "U997Z2P",
            System.currentTimeMillis() - 6000,  "U484Z2P",
            System.currentTimeMillis(), VehicleType.TRAM, 0)

        Log.e("populate", "database")
        val stopDao = db.stopDao()
        val tripDao = db.tripDao()
        val shapeDao = db.shapeDao()
        val routeDao = db.routeDao()
        val vehicleDao = db.vehicleDao()
        val positionDao = db.positionDao()

        GlobalScope.launch {

            shapeDao.deleteAllShapes()

            stopDao.insert(listOf(stop1, stop2, stop3, stop4))
            tripDao.insert(trip)
            shapeDao.insert(databaseShapes)
            routeDao.insert(route)
            vehicleDao.insert(vehicle)
            positionDao.insert(position)
        }
    }
}

private inline fun EditText.onFocusChangeListener(crossinline hasFocus: (Boolean) -> Unit) {
    setOnFocusChangeListener { _, b -> hasFocus(b)}
}
