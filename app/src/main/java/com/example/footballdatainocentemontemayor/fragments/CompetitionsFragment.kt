package com.example.footballdatainocentemontemayor.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.footballdatainocentemontemayor.MainActivity
import com.example.footballdatainocentemontemayor.R
import com.example.footballdatainocentemontemayor.adapters.CompetitionsAdapter
import com.example.footballdatainocentemontemayor.adapters.OnCompetitionItemClickListener
import com.example.footballdatainocentemontemayor.models.beans.Competition

class CompetitionsFragment: Fragment(), OnCompetitionItemClickListener {

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

        val competitionsAdapter = CompetitionsAdapter((activity as MainActivity).competitions, this, requireActivity())
        competitionsView!!.adapter = competitionsAdapter
    }

    override fun onClick(product: Competition) {
        TODO("Not yet implemented")
    }
}