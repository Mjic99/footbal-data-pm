package com.example.footballdatainocentemontemayor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.footballdatainocentemontemayor.R
import com.example.footballdatainocentemontemayor.models.beans.Team


class TeamsAdapter(
    teams: ArrayList<Team>,
) : RecyclerView.Adapter<TeamsAdapter.ViewHolder>()
{
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var teamNameView : TextView? = null
        var venueNameView : TextView? = null

        init {
            teamNameView = view.findViewById(R.id.teamName)
            venueNameView = view.findViewById(R.id.venueName)
        }
    }

    private var teams : ArrayList<Team>? = teams

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_team, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val team = teams!![position]
        holder.teamNameView!!.text = team.name
        holder.venueNameView!!.text = team.venue
    }

    override fun getItemCount(): Int {
        return teams!!.size
    }
}