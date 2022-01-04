package com.yernazar.pidapplication.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yernazar.pidapplication.R
import com.yernazar.pidapplication.presentation.interfaces.OnRouteSelectListener
import com.yernazar.pidapplication.data.repository.server.response.routeTimeResponse.RouteTime
import java.text.SimpleDateFormat

class RoutesAdapter(val onRouteSelectListener: OnRouteSelectListener) : RecyclerView.Adapter<RoutesAdapter.ViewHolder>() {

    private var routes: List<RouteTime>? = emptyList()


    @SuppressLint("NotifyDataSetChanged")
    fun setRoutes(routes : List<RouteTime>){
        this.routes = routes
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val routeNameTv : TextView = itemView.findViewById(R.id.route_name_tv)
        val nextInTv : TextView = itemView.findViewById(R.id.next_in_tv)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = layoutPosition
            routes?.get(position)
            if (position != RecyclerView.NO_POSITION) {
                routes?.let {
                    onRouteSelectListener.onRouteSelect(routes!![position].route)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.routes_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val routeNextArrive = routes?.get(position)

        val pattern = SimpleDateFormat("hh:mm")

        if (routeNextArrive != null) {
            holder.routeNameTv.text = routeNextArrive.route.longName
            holder.nextInTv.text = pattern.format(routeNextArrive.expectedArrival)
        }

    }

    override fun getItemCount(): Int {
        return routes?.size ?: 0
    }
}