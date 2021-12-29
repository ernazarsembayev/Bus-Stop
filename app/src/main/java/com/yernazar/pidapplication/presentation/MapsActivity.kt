package com.yernazar.pidapplication.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yernazar.pidapplication.R
import com.yernazar.pidapplication.utils.config.Config
import com.yernazar.pidapplication.databinding.ActivityMapsBinding
import com.yernazar.pidapplication.domain.SharedViewModel
import com.yernazar.pidapplication.data.repository.model.*
import com.yernazar.pidapplication.presentation.fragment.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var toggle: ActionBarDrawerToggle

    private val searchResultsFragment: SearchResultsFragment by inject()
    private val tripFragment: TripFragment by inject()
    private val routeFragment: RouteFragment by inject()
    private val stopFragment: StopFragment by inject()

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

        viewModel.liveDataBottomSheetState.observe( this, {
            bottomSheetBehavior.state = it
        })

        viewModel.liveDataStop.observe(this, {
            hideKeyboard()
            showFragment(stopFragment)
        })

        viewModel.liveDataVehicle.observe(this, {
            hideKeyboard()
            showFragment(tripFragment)
        })

        viewModel.liveDataRoute.observe(this, {
            hideKeyboard()
            showFragment(routeFragment)
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.onMapReady(googleMap)
    }

    private fun setBottomSheetState(newState: Int) {
        viewModel.setBottomSheetState(newState)
    }

    private fun <T : BaseFragment> showFragment(fragment: T) {
        Log.e("bottomSheetState", fragment.bottomSheetState.toString())
        viewModel.setBottomSheetState(fragment.bottomSheetState)
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.bottom_sheet_frame, fragment,
                fragment.name)
            .addToBackStack(fragment.name)
            .commit()
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
                            Config.tripFragmentName -> tripFragment.bottomSheetState.run { bottomSheetBehavior.state }
                            Config.stopFragmentName -> stopFragment.bottomSheetState.run { bottomSheetBehavior.state }
                            else -> bottomSheetBehavior.state
                        }
                    )
                    return
                }
            setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun hideKeyboard() {
        val imm =
            this@MapsActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        searchEditText.clearFocus()
    }

    private fun initBottomSheet() {
        BottomSheetBehavior.from(binding.root.rootView.findViewById(R.id.bottom_sheet)).apply {
//            val displayMetrics: DisplayMetrics = resources.displayMetrics
//            val height = displayMetrics.heightPixels
//            val maxHeight = (height * 0.95).toInt()
            bottomSheetBehavior = this
            peekHeight = 200
//            setMaxHeight(maxHeight)
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
                showFragment(searchResultsFragment)
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
                    viewModel.stopUpdateVehicles()
                    val loginIntent = Intent(this, LoginActivity::class.java)
                    startActivity(loginIntent)
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
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
}

private inline fun EditText.onFocusChangeListener(crossinline hasFocus: (Boolean) -> Unit) {
    setOnFocusChangeListener { _, b -> hasFocus(b)}
}
