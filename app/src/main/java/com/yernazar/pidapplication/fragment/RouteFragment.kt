package com.yernazar.pidapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yernazar.pidapplication.databinding.FragmentRouteBinding
import org.jguniverse.pidapplicationgm.repo.model.Route

class RouteFragment(val route: Route) : Fragment() {

    private lateinit var binding: FragmentRouteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRouteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//todo
//        binding.fromStopNameTv =
//        binding.fromStopNameTv =
    }

}