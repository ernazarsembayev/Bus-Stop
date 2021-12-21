package com.yernazar.pidapplication.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yernazar.pidapplication.presentation.adapter.RoutesAdapter
import com.yernazar.pidapplication.databinding.FragmentStopBinding
import com.yernazar.pidapplication.presentation.interfaces.OnRouteSelectListener
import com.yernazar.pidapplication.domain.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class StopFragment(onRouteSelectListener: OnRouteSelectListener) : Fragment() {

    private lateinit var binding: FragmentStopBinding
    private lateinit var recyclerView: RecyclerView
    private var routesAdapter = RoutesAdapter(onRouteSelectListener)
    val bottomSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED

    private val mViewModel: SharedViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStopBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.routeRv

        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        recyclerView.adapter = routesAdapter

        mViewModel.liveDataStopRoutes.observe( viewLifecycleOwner, {
            routesAdapter.setRoutes(it)
        })
    }
}