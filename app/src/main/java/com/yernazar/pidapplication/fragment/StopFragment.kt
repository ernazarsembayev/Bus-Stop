package com.yernazar.pidapplication.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yernazar.pidapplication.adapter.RoutesAdapter
import com.yernazar.pidapplication.databinding.FragmentStopBinding
import com.yernazar.pidapplication.interfaces.OnRouteSelectListener
import com.yernazar.pidapplication.repo.database.AppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jguniverse.pidapplicationgm.repo.model.Stop

class StopFragment(val stop: Stop, private val onRouteSelectListener: OnRouteSelectListener) : Fragment() {

    private lateinit var binding: FragmentStopBinding
    private lateinit var recyclerView: RecyclerView
    private var routesAdapter = RoutesAdapter(onRouteSelectListener)
    private var db: AppDatabase? = null
    val bottomSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED

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

        db = context?.let { AppDatabase.getInstance(it) }
        val routeDao = db?.routeDao()

        GlobalScope.launch {
            val routes = routeDao?.getRouteNextArrive(stop.uid)
            Log.e("routes", routes?.size.toString())
            if (routes != null) {
                routesAdapter.setRoutes(routes)
            }
        }


    }
}