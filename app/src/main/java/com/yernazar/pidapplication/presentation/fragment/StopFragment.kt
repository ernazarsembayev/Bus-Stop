package com.yernazar.pidapplication.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yernazar.pidapplication.data.repository.model.Route
import com.yernazar.pidapplication.presentation.adapter.RoutesAdapter
import com.yernazar.pidapplication.databinding.FragmentStopBinding
import com.yernazar.pidapplication.presentation.interfaces.OnRouteSelectListener
import com.yernazar.pidapplication.domain.MapsSharedViewModel
import com.yernazar.pidapplication.utils.config.Config
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class StopFragment : BaseFragment(), OnRouteSelectListener {

    private lateinit var binding: FragmentStopBinding
    private lateinit var recyclerView: RecyclerView
    private var routesAdapter = RoutesAdapter(this)
    override val bottomSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED
    override val name = Config.STOP_FRAGMENT_NAME

    private val viewModelMaps: MapsSharedViewModel by sharedViewModel()

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

        viewModelMaps.liveDataStopRoutes.observe( viewLifecycleOwner, {
            routesAdapter.setRoutes(it)
        })
    }

    override fun onRouteSelect(route: Route) {
        viewModelMaps.onRouteSelect(route)
    }
}