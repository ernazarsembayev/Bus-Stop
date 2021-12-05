package com.yernazar.pidapplication.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.yernazar.pidapplication.adapter.SearchResultAdapter
import com.yernazar.pidapplication.databinding.FragmentSearchResultsBinding
import com.yernazar.pidapplication.interfaces.OnSearchChangedListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jguniverse.pidapplicationgm.repo.api.RouteApi

class SearchResultsFragment : Fragment(), OnSearchChangedListener {

    private var searchResultAdapter = SearchResultAdapter()
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentSearchResultsBinding

    fun setListener(listener: SearchResultAdapter.OnSearchItemClickListener){
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
        recyclerView.adapter = searchResultAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSearchChanged(requestText: String) {
        GlobalScope.launch {
            val request = RouteApi.create().getByNameLike(requestText)

            if (request.isSuccessful) {

                searchResultAdapter.setRoutes(request.body()!!)
                searchResultAdapter.notifyDataSetChanged()

            }
        }
    }

    fun getSearchChangedListener(): OnSearchChangedListener {
        return this
    }

}