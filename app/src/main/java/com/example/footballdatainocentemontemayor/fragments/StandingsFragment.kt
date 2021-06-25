package com.example.footballdatainocentemontemayor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.footballdatainocentemontemayor.R
import com.example.footballdatainocentemontemayor.adapters.StandingsAdapter
import com.example.footballdatainocentemontemayor.models.managers.StandingsManager

class StandingsFragment: Fragment() {

    var standingsView : RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_standings,
            container,
            false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        standingsView = view.findViewById(R.id.standingsView)

        val competitionId = requireArguments().getInt("competitionId")
        StandingsManager.getInstance().getCompetitionStandingsRoom(competitionId, requireContext()) { standings ->
            requireActivity().runOnUiThread {
                val standingsAdapter = StandingsAdapter(standings)
                standingsView!!.adapter = standingsAdapter
            }
        }
    }
}