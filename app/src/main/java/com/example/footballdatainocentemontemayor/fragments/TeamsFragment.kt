package com.example.footballdatainocentemontemayor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.footballdatainocentemontemayor.MainActivity
import com.example.footballdatainocentemontemayor.R
import com.example.footballdatainocentemontemayor.adapters.StandingsAdapter
import com.example.footballdatainocentemontemayor.adapters.TeamsAdapter
import com.example.footballdatainocentemontemayor.models.managers.StandingsManager
import com.example.footballdatainocentemontemayor.models.managers.TeamManager


class TeamsFragment: Fragment() {

    var teamsView : RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_teams,
            container,
            false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        teamsView = view.findViewById(R.id.teamsView)

        val competitionId = requireArguments().getInt("competitionId")
        TeamManager.getInstance().getTeamsByCompetitionId(competitionId, requireContext()) { teams ->
            requireActivity().runOnUiThread {
                val teamsAdapter = TeamsAdapter(teams)
                teamsView!!.adapter = teamsAdapter
            }
        }
    }
}