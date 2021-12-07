package com.yernazar.pidapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yernazar.pidapplication.R
import com.yernazar.pidapplication.interfaces.OnRouteSelectListener
import com.yernazar.pidapplication.mapper.RouteMapper
import com.yernazar.pidapplication.repo.model.RouteAndNextArrive

class RoutesAdapter(val onRouteSelectListener: OnRouteSelectListener) : RecyclerView.Adapter<RoutesAdapter.ViewHolder>() {

    private var routes: List<RouteAndNextArrive>? = emptyList()


    fun setRoutes(routes : List<RouteAndNextArrive>){
        this.routes = routes
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
                    val route = RouteMapper.toRoute(routes!![position])
                    onRouteSelectListener.onRouteSelect(route)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.routes_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val routeNextArrive = routes?.get(position)

        if (routeNextArrive != null) {
            holder.routeNameTv.text = routeNextArrive.longName
            holder.nextInTv.text = routeNextArrive.nextArrive.toString()
        }

    }

    override fun getItemCount(): Int {
        return routes?.size ?: 0
    }
}