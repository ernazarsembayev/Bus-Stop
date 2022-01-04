package com.yernazar.pidapplication.presentation.fragment

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yernazar.pidapplication.data.repository.model.Route
import com.yernazar.pidapplication.data.repository.server.response.routeTimeResponse.RouteTime
import com.yernazar.pidapplication.databinding.FragmentSearchResultsBinding
import com.yernazar.pidapplication.domain.SharedViewModel
import com.yernazar.pidapplication.domain.usecases.GetFavouriteRoutes
import com.yernazar.pidapplication.presentation.adapter.SearchResultAdapter
import com.yernazar.pidapplication.presentation.interfaces.OnRouteSelectListener
import com.yernazar.pidapplication.utils.config.Config
import com.yernazar.pidapplication.utils.config.Config.SHARED_PREFERENCES
import com.yernazar.pidapplication.utils.config.Config.SP_TOKEN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchResultsFragment : BaseFragment(), OnRouteSelectListener {

    private val viewModel: SharedViewModel by sharedViewModel()

    private val getFavouriteRoutes: GetFavouriteRoutes by inject()

    private var searchResultAdapter = SearchResultAdapter(this)
    private var favouriteRoutesAdapter = SearchResultAdapter(this)
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var favouriteRoutesRecyclerView: RecyclerView
    private lateinit var binding: FragmentSearchResultsBinding

    override val bottomSheetState: Int
        get() = BottomSheetBehavior.STATE_EXPANDED

    override val name: String
        get() = Config.SEARCH_FRAGMENT_NAME

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchResultsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchRecyclerView = binding.searchResultsRv
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        searchRecyclerView.layoutManager = llm
        searchRecyclerView.adapter = searchResultAdapter

        viewModel.liveDataSearchRoute.observe(viewLifecycleOwner, {
            searchResultAdapter.setRoutes(it)
        })

        val sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        if (sharedPreferences.getString(SP_TOKEN, "") != "") {

            CoroutineScope(Dispatchers.Default).launch {
                val favouriteRoutes = getFavouriteRoutes.execute()

                CoroutineScope(Dispatchers.Main).launch {
                    binding.favouritesGroup.visibility = View.VISIBLE
                    favouriteRoutesRecyclerView = binding.searchResultsRv
                    val manager = LinearLayoutManager(context)
                    manager.orientation = LinearLayoutManager.VERTICAL
                    favouriteRoutesRecyclerView.layoutManager = llm
                    favouriteRoutesRecyclerView.adapter = favouriteRoutesAdapter

                    favouriteRoutesAdapter.setRoutes(favouriteRoutes)
                }
            }
        }
    }

    override fun onRouteSelect(route: Route) {
        viewModel.onRouteSelect(route)
    }
}