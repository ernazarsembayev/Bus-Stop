package com.yernazar.pidapplication.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yernazar.pidapplication.databinding.FragmentTripBinding
import com.yernazar.pidapplication.domain.SharedViewModel
import com.yernazar.pidapplication.utils.config.Config
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat

class TripFragment : BaseFragment() {

    private lateinit var binding: FragmentTripBinding
    override val bottomSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED
    override val name = Config.tripFragmentName

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

        val pattern = SimpleDateFormat("hh:mm")

        mViewModel.liveDataVehicle.observe(viewLifecycleOwner, {
            binding.routeNameTv.text = it.originRouteName
            binding.directionNameTv.text = it.lastStop.name
            binding.nextBusArrivalTv.text = pattern.format(it.nextStopArrival)
            binding.delayVariableTv.text = it.delay.toString()
        })
    }
}