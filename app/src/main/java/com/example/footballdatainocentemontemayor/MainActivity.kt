package com.example.footballdatainocentemontemayor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.footballdatainocentemontemayor.fragments.CompetitionsFragment
import com.example.footballdatainocentemontemayor.fragments.OnSync
import com.example.footballdatainocentemontemayor.fragments.SyncFragment
import com.example.footballdatainocentemontemayor.models.beans.Competition
import com.example.footballdatainocentemontemayor.models.managers.CompetitionManager
import com.example.footballdatainocentemontemayor.models.managers.OnGetCompetitionsDone

class MainActivity : AppCompatActivity(), OnGetCompetitionsDone, OnSync {
    var fragments : ArrayList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Esta actividad contiene el fragment para sincronizar y el de listado de competiciones
        fragments.add(SyncFragment())
        fragments.add(CompetitionsFragment())

        val ft = supportFragmentManager.beginTransaction()

        // Al entrar se verifica si hay data local
        if (CompetitionManager.getInstance().hasLocalCompetitions(this)) {
            // Si existe se va de frente al listado de competiciones
            ft.replace(R.id.contentFrame, fragments[1])
        } else {
            // De lo contrario se muestra el boton para sincronizar
            ft.replace(R.id.contentFrame, fragments[0])
        }
        ft.commit()
    }

    // Una vez que llegan las competiciones del api se muestra el fragment que las lista
    override fun onSuccess(newCompetitions: ArrayList<Competition>) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.contentFrame, fragments[1])
        ft.commit()
    }

    override fun onError(msg: String) {}

    // Funcion que trae todas las competiciones de la api
    override fun syncData() {
        CompetitionManager.getInstance().getCompetitions(this, this)
    }
}