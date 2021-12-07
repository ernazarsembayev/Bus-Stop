package com.yernazar.pidapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yernazar.pidapplication.R
import com.yernazar.pidapplication.interfaces.OnRouteSelectListener
import org.jguniverse.pidapplicationgm.repo.model.Route

class SearchResultAdapter : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    private var routes = emptyList<Route>()
    private lateinit var listener: OnRouteSelectListener

    fun setRoutes(routes : List<Route>){
        this.routes = routes
    }

    fun setListener(listener: OnRouteSelectListener){
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val textView : TextView = itemView.findViewById(R.id.vehicle_name_tv)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = layoutPosition
            routes[position]
            if (position != RecyclerView.NO_POSITION) {
                listener.onRouteSelect(routes[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_reslt, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val route = routes[position]

        holder.textView.text = route.longName

    }

    override fun getItemCount(): Int {
        return routes.size
    }
}