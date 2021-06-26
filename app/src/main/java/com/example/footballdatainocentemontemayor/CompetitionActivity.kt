package com.example.footballdatainocentemontemayor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.footballdatainocentemontemayor.fragments.StandingsFragment
import com.example.footballdatainocentemontemayor.fragments.TeamsFragment
import com.example.footballdatainocentemontemayor.models.managers.CompetitionManager
import com.google.android.material.navigation.NavigationView

class CompetitionActivity : AppCompatActivity() {

    var dlaMain : DrawerLayout? = null

    var fragments : ArrayList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_competition)
        // Esta actividad contiene el listado de equipos y de resultados de una competicion
        fragments.add(TeamsFragment())
        fragments.add(StandingsFragment())

        val nviMain = findViewById<NavigationView>(R.id.nviMain)
        dlaMain = findViewById(R.id.dlaMain)

        // Se configuran las acciones del drawer
        nviMain.setNavigationItemSelectedListener { item : MenuItem ->
            item.setChecked(true)
            // Cada opcion abrirá un fragment distinto
            if (item.itemId == R.id.mnuEquipos) {
                openFragmentWithCompetition(fragments[0])
            }else if (item.itemId == R.id.mnuPosiciones) {
                Log.i("aea", "adasdaasddasdas")
                openFragmentWithCompetition(fragments[1])
            }
            dlaMain!!.closeDrawers()
            true
        }
        // Por defecto se muestra el fragment de listado de equipos
        openFragmentWithCompetition(fragments[0])
    }

    // Función que recibe un fragment y lo muestra
    private fun openFragmentWithCompetition(fragment: Fragment) {
        val bun = Bundle()
        bun.putInt("competitionId", intent.getIntExtra("competitionId", 0))
        fragment.arguments = bun

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.competitionContent, fragment)
        ft.commit()
    }
}