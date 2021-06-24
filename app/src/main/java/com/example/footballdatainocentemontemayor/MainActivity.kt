package com.example.footballdatainocentemontemayor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import com.example.footballdatainocentemontemayor.models.beans.Competition
import com.example.footballdatainocentemontemayor.models.managers.CompetitionManager
import com.example.footballdatainocentemontemayor.models.managers.OnGetCompetitionsDone

class MainActivity : AppCompatActivity(), OnGetCompetitionsDone {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CompetitionManager.getInstance().getCompetitions(this)
    }

    override fun onSuccess(competitions: ArrayList<Competition>) {
        for (competition in competitions) {
            Log.i("awa", competition.name)
        }
    }

    override fun onError(msg: String) {
        Log.i("awa", msg)
    }
}