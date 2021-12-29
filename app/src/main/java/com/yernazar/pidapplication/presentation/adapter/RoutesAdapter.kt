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

class RoutesAdapter(val onRouteSelectListener: OnRouteSelectListener) : RecyclerView.Adapter<RoutesAdapter.ViewHolder>() {

    private var routes: List<Route>? = emptyList()


    @SuppressLint("NotifyDataSetChanged")
    fun setRoutes(routes : List<Route>){
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
                    onRouteSelectListener.onRouteSelect(routes!![position])
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

        if (routeNextArrive != null) {
            holder.routeNameTv.text = routeNextArrive.longName
//            holder.nextInTv.text = routeNextArrive.
        }

    }

    override fun getItemCount(): Int {
        return routes?.size ?: 0
    }
}