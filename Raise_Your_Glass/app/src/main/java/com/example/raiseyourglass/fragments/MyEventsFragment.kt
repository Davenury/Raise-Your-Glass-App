package com.example.raiseyourglass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.R
import com.example.raiseyourglass.adapters.MyEventsAdapter
import com.example.raiseyourglass.dataclasses.Event
import com.example.raiseyourglass.firebase.Firebase
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_drinks_list.*
import kotlinx.android.synthetic.main.fragment_my_events.*
import java.time.LocalDate
import java.util.*

class MyEventsFragment(private val setCurrentFragment: (fragment:Fragment) -> Unit) : Fragment(R.layout.fragment_my_events) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddEvent.setOnClickListener {
            val modificationFragment = EventModificationFragment()
            setCurrentFragment(modificationFragment)
        }

        setRecyclerView()
    }

    private fun setRecyclerView(){
        val adapter = MyEventsAdapter(setCurrentFragment)
        rvMyEventsList.adapter = adapter
        rvMyEventsList.layoutManager = LinearLayoutManager(view?.context)

    }
}