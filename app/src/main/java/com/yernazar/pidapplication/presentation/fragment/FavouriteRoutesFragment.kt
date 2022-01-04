package com.yernazar.pidapplication.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yernazar.pidapplication.data.repository.model.Route
import com.yernazar.pidapplication.databinding.FragmentFavouriteRoutesBinding
import com.yernazar.pidapplication.domain.MapsSharedViewModel
import com.yernazar.pidapplication.presentation.adapter.SearchResultAdapter
import com.yernazar.pidapplication.presentation.interfaces.OnRouteSelectListener
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FavouriteRoutesFragment : DialogFragment(), OnRouteSelectListener {

    private lateinit var binding: FragmentFavouriteRoutesBinding

    private val viewModelMaps: MapsSharedViewModel by sharedViewModel()

    private var searchResultAdapter = SearchResultAdapter(this)
    private lateinit var routeRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteRoutesBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        routeRecyclerView = binding.routesRv
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        routeRecyclerView.layoutManager = llm
        routeRecyclerView.adapter = searchResultAdapter

        viewModelMaps.getFavouriteRoutes()
        viewModelMaps.liveDataFavouriteRoutes.observe(viewLifecycleOwner, {
            searchResultAdapter.setRoutes(it)
        })
    }

    override fun onRouteSelect(route: Route) {
        viewModelMaps.onRouteSelect(route)
        dismiss()
    }

}