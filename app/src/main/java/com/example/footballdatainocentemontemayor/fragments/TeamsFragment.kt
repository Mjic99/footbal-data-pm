package com.example.footballdatainocentemontemayor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.footballdatainocentemontemayor.MainActivity
import com.example.footballdatainocentemontemayor.R
import com.example.footballdatainocentemontemayor.adapters.TeamsAdapter


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

        val teamsAdapter = TeamsAdapter((activity as MainActivity).currTeams, requireActivity())
        teamsView!!.adapter = teamsAdapter
    }
}