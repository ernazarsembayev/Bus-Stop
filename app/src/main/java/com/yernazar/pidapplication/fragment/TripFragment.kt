package com.yernazar.pidapplication.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yernazar.pidapplication.databinding.FragmentTripBinding
import com.yernazar.pidapplication.repo.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jguniverse.pidapplicationgm.repo.model.Route
import org.jguniverse.pidapplicationgm.repo.model.Trip
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TripFragment(val trip: Trip, val route: Route) : Fragment() {

    private lateinit var binding: FragmentTripBinding
    val bottomSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch {
            val db = context?.let { AppDatabase.getInstance(it) }

            if (db != null) {
                val vehicleDao = db.vehicleDao()
                val stopDao = db.stopDao()
                val vehicle = vehicleDao.getByTripId(tripId = trip.uid)
                if (vehicle != null){
                    val stop = stopDao.getById(vehicle.lastStopId)
                    withContext(Dispatchers.Main) {
                        val date = Date(vehicle.nextStopArrival)
                        val format = SimpleDateFormat("HH:mm")
                        //todo resource string
                        binding.routeNameTv.text = route.longName + ", Trip id #" + trip.uid
                        binding.directionNameTv.text = stop?.name
                        binding.nextBusArrivalTv.text = format.format(date)
                        binding.delayVariableTv.text = vehicle.delay.toString()
                    }
                }
            }
        }

    }

}