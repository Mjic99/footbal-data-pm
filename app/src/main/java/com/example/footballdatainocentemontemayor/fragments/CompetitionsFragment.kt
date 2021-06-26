package com.example.footballdatainocentemontemayor.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.footballdatainocentemontemayor.CompetitionActivity
import com.example.footballdatainocentemontemayor.MainActivity
import com.example.footballdatainocentemontemayor.R
import com.example.footballdatainocentemontemayor.adapters.CompetitionsAdapter
import com.example.footballdatainocentemontemayor.adapters.OnCompetitionItemClickListener
import com.example.footballdatainocentemontemayor.models.beans.Competition
import com.example.footballdatainocentemontemayor.models.managers.CompetitionManager
import com.example.footballdatainocentemontemayor.models.managers.OnGetCompetitionsDone

class CompetitionsFragment: Fragment(), OnCompetitionItemClickListener, OnGetCompetitionsDone {

    var competitionsView : RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_competitions,
            container,
            false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        competitionsView = view.findViewById(R.id.competitionsView)

        CompetitionManager.getInstance().getCompetitions(this, requireContext())
    }

    override fun onClick(competition: Competition) {
        val intent = Intent(activity, CompetitionActivity::class.java)
        intent.putExtra("competitionId", competition.id)
        startActivity(intent)
    }

    override fun onSuccess(newCompetitions: ArrayList<Competition>) {
        requireActivity().runOnUiThread {
            val competitionsAdapter = CompetitionsAdapter(newCompetitions, this)
            competitionsView!!.adapter = competitionsAdapter
        }
    }

    override fun onError(msg: String) {
        TODO("Not yet implemented")
    }
}