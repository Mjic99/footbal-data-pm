package com.example.footballdatainocentemontemayor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.footballdatainocentemontemayor.R
import com.example.footballdatainocentemontemayor.models.beans.Competition

interface OnCompetitionItemClickListener {
    fun onClick(competition: Competition)
}

class CompetitionsAdapter(
    competitions: ArrayList<Competition>,
    listener: OnCompetitionItemClickListener,
    context: Context
) : RecyclerView.Adapter<CompetitionsAdapter.ViewHolder>()
{
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var competitionNameView : TextView? = null
        var competitionSeasonsView : TextView? = null

        init {
            competitionNameView = view.findViewById(R.id.competitionName)
            competitionSeasonsView = view.findViewById(R.id.competitionSeasons)
        }
    }

    private var competitions : ArrayList<Competition>? = competitions
    private var listener : OnCompetitionItemClickListener? = listener
    private var context : Context? = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_competition, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val competition = competitions!![position]

        holder.competitionNameView!!.text = competition.name
        holder.competitionSeasonsView!!.text = "Nro de temporadas: ${competition.numberOfAvailableSeasons}"

        holder.itemView.setOnClickListener { v : View ->
            listener!!.onClick(competitions!![position])
        }
    }

    override fun getItemCount(): Int {
        return competitions!!.size
    }
}