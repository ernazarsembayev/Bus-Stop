package com.yernazar.pidapplication.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.GsonBuilder
import com.yernazar.pidapplication.R
import com.yernazar.pidapplication.utils.config.Config
import com.yernazar.pidapplication.databinding.ActivityMapsBinding
import com.yernazar.pidapplication.domain.SharedViewModel
import com.yernazar.pidapplication.data.localData.LocalData
import com.yernazar.pidapplication.presentation.fragment.SearchResultsFragment
import com.yernazar.pidapplication.presentation.fragment.StopFragment
import com.yernazar.pidapplication.presentation.fragment.TripFragment
import com.yernazar.pidapplication.presentation.interfaces.OnRouteSelectListener
import com.yernazar.pidapplication.utils.mapper.ShapeMapper
import com.yernazar.pidapplication.data.repository.server.shapeResponse.ShapeResponse
import com.yernazar.pidapplication.data.repository.database.AppDatabase
import com.yernazar.pidapplication.data.repository.model.*
import com.yernazar.pidapplication.presentation.fragment.RouteFragment
import com.yernazar.pidapplication.presentation.loginView.presentation.LoginActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    OnRouteSelectListener {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var toggle: ActionBarDrawerToggle

    private val searchResultsFragment = SearchResultsFragment()
    private var tripFragment: TripFragment? = null
    private var routeFragment: RouteFragment? = null
    private var stopFragment: StopFragment? = null

    private val viewModel by viewModel<SharedViewModel>()

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

        initBottomSheet()
        initSearchEditText()
        initToggle()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        searchResultsFragment.setListener(this)

        viewModel.liveDataBottomSheetState.observe( this, {
            bottomSheetBehavior.state = it
        })

        viewModel.liveDataStop.observe(this, {
            beginStopFragment()
        })

        populateDatabase()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.onMapReady(googleMap)
    }

    private fun initBottomSheet() {
        BottomSheetBehavior.from(binding.root.rootView.findViewById(R.id.bottom_sheet)).apply {
            val displayMetrics: DisplayMetrics = resources.displayMetrics
            val height = displayMetrics.heightPixels
            val maxHeight = (height * 0.95).toInt()
            bottomSheetBehavior = this
            peekHeight = 200
            setMaxHeight(maxHeight)
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                viewModel.onBottomSheetStateChanged(newState)
                if (newState == BottomSheetBehavior.STATE_COLLAPSED){
                    searchEditText.clearFocus()
                    val imm =
                        this@MapsActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(bottomSheet.windowToken, 0)
                } else {
                    this@MapsActivity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        }
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
    }

    private fun initSearchEditText() {
        searchEditText = binding.root.rootView.findViewById(R.id.search_et)
        searchEditText.onFocusChangeListener { hasFocus ->
            if (hasFocus) {
                beginSearchFragment()
            }
        }
        searchEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.onSearchChanged(text.toString())
        }
    }

    private fun initToggle() {
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.authorization -> {
                    val loginIntent = Intent(this, LoginActivity::class.java)
                    startActivity(loginIntent)
                }
                R.id.item2 -> Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
            }
            true
        }
        val btn: ImageButton = binding.root.rootView.findViewById(R.id.menu_ib)
        btn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
            searchEditText.clearFocus()
            val imm =
                this@MapsActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }
    }

    override fun onRouteSelect(route: Route) {

        viewModel.onRouteSelect(route)

        val imm =
            this@MapsActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        searchEditText.clearFocus()

        beginRouteFragment()
//        beginTripFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setBottomSheetState(newState: Int) {
        viewModel.setBottomSheetState(newState)
    }

//    private val busIcon: BitmapDescriptor by lazy {
//        val color = ContextCompat.getColor(this, R.color.black)
//        BitmapHelper.vectorToBitmap(this, R.drawable.ic_baseline_directions_bus_24, color)
//    }

    private fun beginSearchFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.bottom_sheet_frame,
                searchResultsFragment,
                Config.searchResultsFragmentName
            ).addToBackStack(Config.searchResultsFragmentName)
            .commit()
        viewModel.setBottomSheetState(searchResultsFragment.bottomSheetState)
    }

    private fun beginStopFragment() {
        stopFragment = StopFragment(this)
        stopFragment?.let {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.bottom_sheet_frame,
                    stopFragment!!,
                    Config.stopFragmentName)
                .addToBackStack(Config.stopFragmentName)
                .commit()
            viewModel.setBottomSheetState(it.bottomSheetState)
        }
    }

    private fun beginTripFragment() {
        tripFragment = TripFragment()
        tripFragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.bottom_sheet_frame, tripFragment!!,
                    Config.tripFragmentName)
                .addToBackStack(Config.tripFragmentName)
                .commit()
            viewModel.setBottomSheetState(it.bottomSheetState)
        }
    }
    private fun beginRouteFragment() {
        routeFragment = RouteFragment()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.bottom_sheet_frame, routeFragment!!,
                Config.tripFragmentName)
            .addToBackStack(Config.tripFragmentName)
            .commit()
        viewModel.setBottomSheetState(routeFragment!!.bottomSheetState)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.END))
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        else {
            if ((bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED ||
                        supportFragmentManager.backStackEntryCount > 0)
            ) {
                super.onBackPressed()
                if (supportFragmentManager.backStackEntryCount > 0) {
                    setBottomSheetState(
                        when (supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1].tag.toString()) {
                            Config.searchResultsFragmentName -> searchResultsFragment.bottomSheetState
                            Config.tripFragmentName -> tripFragment?.bottomSheetState?.run { bottomSheetBehavior.state }!!
                            Config.stopFragmentName -> stopFragment?.bottomSheetState?.run { bottomSheetBehavior.state }!!
                            else -> bottomSheetBehavior.state
                        }
                    )
                    return
                }
            setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        }
    }

    private fun populateDatabase() {

        val db = AppDatabase.getInstance(this)

        val builder = GsonBuilder()
        val gson = builder.create()
        val shapes = gson.fromJson(LocalData.shapeJson, ShapeResponse::class.java)

        val databaseShapes = ShapeMapper.toDatabaseShape(shapes)

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
            System.currentTimeMillis() + 600000, VehicleType.TRAM, 0)

        val stopDao = db.stopDao()
        val tripDao = db.tripDao()
        val shapeDao = db.shapeDao()
        val routeDao = db.routeDao()
        val vehicleDao = db.vehicleDao()
        val positionDao = db.positionDao()

        CoroutineScope(Default).launch {

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
