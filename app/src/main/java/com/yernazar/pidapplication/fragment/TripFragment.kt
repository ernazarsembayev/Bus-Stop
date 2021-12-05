package com.yernazar.pidapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yernazar.pidapplication.databinding.FragmentTripBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jguniverse.pidapplicationgm.repo.model.Trip

class TripFragment(val trip: Trip) : Fragment() {

    private lateinit var binding: FragmentTripBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch {

        }

        binding.directionNameTv.text = trip.uid
        binding.delayTv.text = trip.direction.toString()
        binding.routeNameTv.text = trip.routeId
    }

}