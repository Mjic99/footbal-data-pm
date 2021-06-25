package com.example.footballdatainocentemontemayor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.footballdatainocentemontemayor.R
import com.example.footballdatainocentemontemayor.models.beans.PlainStanding

class StandingsAdapter(
    standings: ArrayList<PlainStanding>
) : RecyclerView.Adapter<StandingsAdapter.ViewHolder>()
{
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var standingNameView : TextView? = null
        var standingScoreView : TextView? = null

        init {
            standingNameView = view.findViewById(R.id.standingName)
            standingScoreView = view.findViewById(R.id.standingScore)
        }
    }

    private var standings : ArrayList<PlainStanding>? = standings

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_standing, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val standing = standings!![position]
        holder.standingNameView!!.text = "${standing.position}. ${standing.name}"
        holder.standingScoreView!!.text = "Puntos: ${standing.score}"
    }

    override fun getItemCount(): Int {
        return standings!!.size
    }
}