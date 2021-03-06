package com.example.footballdatainocentemontemayor.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.footballdatainocentemontemayor.R

interface OnSync {
    fun syncData()
}

class SyncFragment: Fragment() {
    // Syncer es la actividad que tendrá este fragment y que implementará OnSync
    lateinit var syncer: OnSync

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_sync,
            container,
            false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Se llama a la funcion que implementa la actividad al hacer clic en el boton
        view.findViewById<Button>(R.id.syncButton).setOnClickListener { syncer.syncData() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        syncer = context as OnSync
    }
}