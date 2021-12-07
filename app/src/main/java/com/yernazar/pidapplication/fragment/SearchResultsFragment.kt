package com.yernazar.pidapplication.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yernazar.pidapplication.adapter.SearchResultAdapter
import com.yernazar.pidapplication.databinding.FragmentSearchResultsBinding
import com.yernazar.pidapplication.interfaces.OnRouteSelectListener
import com.yernazar.pidapplication.interfaces.OnSearchChangedListener
import com.yernazar.pidapplication.repo.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchResultsFragment : Fragment(), OnSearchChangedListener {

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
        db = context?.let { AppDatabase.getInstance(it) }!!
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSearchChanged(requestText: String) {
        Log.e("onSearchChanged", requestText)
        if (requestText.isNotEmpty()) {
        GlobalScope.launch {
//            val request = RouteApi.create().getByNameLike(requestText)
//
//            if (request.isExecuted) {
//
//                searchResultAdapter.setRoutes(request.request().!!)
//                searchResultAdapter.notifyDataSetChanged()
//
//            } else {

                if (db != null) {
                    val routeDao = db!!.routeDao()
                    val routes = routeDao.getByNameLike(requestText)
                    if (routes != null) {
                        searchResultAdapter.setRoutes(routes)
                        withContext(Dispatchers.Main) {
                            searchResultAdapter.notifyDataSetChanged()
                        }
                    }
//                }
                }
            }
        } else {
            searchResultAdapter.setRoutes(emptyList())
            searchResultAdapter.notifyDataSetChanged()
        }
    }

    fun getSearchChangedListener(): OnSearchChangedListener {
        return this
    }

}