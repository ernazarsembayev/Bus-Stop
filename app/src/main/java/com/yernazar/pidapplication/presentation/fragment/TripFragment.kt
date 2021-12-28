package com.yernazar.pidapplication.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yernazar.pidapplication.data.repository.database.AppDatabase
import com.yernazar.pidapplication.databinding.FragmentTripBinding
import com.yernazar.pidapplication.domain.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class TripFragment : Fragment() {

    private lateinit var binding: FragmentTripBinding
    val bottomSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED

    private val mViewModel: SharedViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        mViewModel.liveDataRoute.observe(viewLifecycleOwner, {
//            CoroutineScope(Default).launch {
//                val db = context?.let { AppDatabase.getInstance(it) }
//
//                if (db != null && mViewModel.liveDataTrip.value != null && mViewModel.liveDataRoute.value != null) {
//                    val vehicleDao = db.vehicleDao()
//                    val stopDao = db.stopDao()
//                    val vehicle = vehicleDao.getByTripId(tripId = mViewModel.liveDataTrip.value!!.trips.uid)
//                    if (vehicle != null) {
//                        val stop = stopDao.getById(vehicle.lastStopId)
//                        withContext(Main) {
//                            val date = Date(vehicle.nextStopArrival)
//                            val format = SimpleDateFormat("HH:mm")
//                            //todo resource string
//                            binding.routeNameTv.text = mViewModel.liveDataRoute.value!!.longName + ", Trip id #" + mViewModel.liveDataTrip.value!!.uid
//                            binding.directionNameTv.text = stop?.name
//                            binding.nextBusArrivalTv.text = format.format(date)
//                            binding.delayVariableTv.text = vehicle.delay.toString()
//                        }
//                    }
//                }
//            }
//        })
    }
}