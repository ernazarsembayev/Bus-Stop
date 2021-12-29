package com.yernazar.pidapplication.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yernazar.pidapplication.data.repository.model.Route
import com.yernazar.pidapplication.databinding.FragmentSearchResultsBinding
import com.yernazar.pidapplication.domain.SharedViewModel
import com.yernazar.pidapplication.presentation.adapter.SearchResultAdapter
import com.yernazar.pidapplication.presentation.interfaces.OnRouteSelectListener
import com.yernazar.pidapplication.utils.config.Config
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchResultsFragment : BaseFragment(), OnRouteSelectListener {

    private val viewModel: SharedViewModel by sharedViewModel()

    private var searchResultAdapter = SearchResultAdapter(this)
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentSearchResultsBinding

    override val bottomSheetState: Int
        get() = BottomSheetBehavior.STATE_EXPANDED

    override val name: String
        get() = Config.searchResultsFragmentName

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

        recyclerView = binding.searchResultsRv
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        recyclerView.adapter = searchResultAdapter

        viewModel.liveDataSearchRoute.observe(viewLifecycleOwner, {
            searchResultAdapter.setRoutes(it)
        })
    }

    override fun onRouteSelect(route: Route) {
        viewModel.onRouteSelect(route)
    }
}