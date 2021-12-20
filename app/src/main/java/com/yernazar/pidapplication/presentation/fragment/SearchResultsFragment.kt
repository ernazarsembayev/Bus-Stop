package com.yernazar.pidapplication.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yernazar.pidapplication.databinding.FragmentSearchResultsBinding
import com.yernazar.pidapplication.data.repository.database.AppDatabase
import com.yernazar.pidapplication.domain.MainViewModel
import com.yernazar.pidapplication.presentation.adapter.SearchResultAdapter
import com.yernazar.pidapplication.presentation.interfaces.OnRouteSelectListener
import com.yernazar.pidapplication.presentation.interfaces.OnSearchChangedListener
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchResultsFragment : Fragment(), OnSearchChangedListener {

    private val viewModel: MainViewModel by sharedViewModel()

    private var searchResultAdapter = SearchResultAdapter()
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentSearchResultsBinding
    private var db: AppDatabase? = null
    val bottomSheetState = BottomSheetBehavior.STATE_EXPANDED

    fun setListener(listener: OnRouteSelectListener){
        searchResultAdapter.setListener(listener)
    }
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

        db = context?.let { AppDatabase.getInstance(it) }

        viewModel.liveDataSearchRoute.observe(viewLifecycleOwner, {
            searchResultAdapter.setRoutes(it)
        })

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSearchChanged(requestText: String) {
        viewModel.onSearchChanged(requestText)
    }

    fun getSearchChangedListener(): OnSearchChangedListener {
        return this
    }

}