package com.example.raiseyourglass.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.R
import com.example.raiseyourglass.adapters.AllAvailableEventsAdapter
import com.example.raiseyourglass.adapters.DrinksListAdapter
import kotlinx.android.synthetic.main.fragment_drinks_list.*
import kotlinx.android.synthetic.main.fragment_events_list.*


class EventsListFragment(private val setCurrentFragment: (fragment:Fragment) -> Unit) : Fragment(R.layout.fragment_events_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
    }

    private fun setRecyclerView() {
        val adapter = AllAvailableEventsAdapter(setCurrentFragment)
        rvEvents.adapter = adapter
        rvEvents.layoutManager = LinearLayoutManager(view?.context)
    }


}