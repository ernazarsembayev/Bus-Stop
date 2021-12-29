package com.yernazar.pidapplication.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yernazar.pidapplication.R
import com.yernazar.pidapplication.presentation.interfaces.OnRouteSelectListener
import com.yernazar.pidapplication.data.repository.model.Route

class SearchResultAdapter(val onRouteSelectListener: OnRouteSelectListener) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    private var routes: List<Route>? = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setRoutes(routes : List<Route>){
        this.routes = routes
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val textView : TextView = itemView.findViewById(R.id.vehicle_name_tv)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = layoutPosition
            routes?.get(position)
            if (position != RecyclerView.NO_POSITION) {
                routes?.let {
                    onRouteSelectListener.onRouteSelect(routes!![position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_reslt, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val route = routes?.get(position)

        if (route != null) {
            holder.textView.text = route.longName
        }

    }

    override fun getItemCount(): Int {
        return routes?.size ?:0
    }
}