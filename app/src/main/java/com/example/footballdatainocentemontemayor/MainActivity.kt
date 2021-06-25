package com.example.footballdatainocentemontemayor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Window
import androidx.fragment.app.Fragment
import com.example.footballdatainocentemontemayor.fragments.CompetitionsFragment
import com.example.footballdatainocentemontemayor.fragments.OnSync
import com.example.footballdatainocentemontemayor.fragments.SyncFragment
import com.example.footballdatainocentemontemayor.models.beans.Competition
import com.example.footballdatainocentemontemayor.models.managers.CompetitionManager
import com.example.footballdatainocentemontemayor.models.managers.OnGetCompetitionsDone
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), OnGetCompetitionsDone, OnSync {
    var fragments : ArrayList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragments.add(SyncFragment())
        fragments.add(CompetitionsFragment())

        val ft = supportFragmentManager.beginTransaction()

        if (CompetitionManager.getInstance().hasLocalCompetitions(this)) {
            ft.replace(R.id.contentFrame, fragments[1])
        } else {
            ft.replace(R.id.contentFrame, fragments[0])
        }

        ft.addToBackStack(null)
        ft.commit()
    }

    override fun onSuccess(newCompetitions: ArrayList<Competition>) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.contentFrame, fragments[1])
        ft.addToBackStack(null)
        ft.commit()
    }

    override fun onError(msg: String) {
        Log.i("awa", msg)
    }

    override fun syncData() {
        CompetitionManager.getInstance().getCompetitions(this, this)
    }
}