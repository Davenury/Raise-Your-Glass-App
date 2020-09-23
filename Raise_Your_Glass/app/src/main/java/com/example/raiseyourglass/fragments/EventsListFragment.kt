package com.example.raiseyourglass.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.R
import com.example.raiseyourglass.activities.StartActivity
import com.example.raiseyourglass.adapters.AllAvailableEventsAdapter
import com.example.raiseyourglass.adapters.DrinksListAdapter
import com.example.raiseyourglass.listeners.SpinnerListener
import kotlinx.android.synthetic.main.fragment_drinks_list.*
import kotlinx.android.synthetic.main.fragment_events_list.*


class EventsListFragment : Fragment(R.layout.fragment_events_list) {

    private var setCurrentFragment: (fragment: Fragment) -> Unit = { _ -> null }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as StartActivity
        setCurrentFragment = activity.lambdaSetCurrentFragment

        setRecyclerView()
    }

    private fun setRecyclerView() {
        val adapter = AllAvailableEventsAdapter(setCurrentFragment)
        rvEvents.adapter = adapter
        rvEvents.layoutManager = LinearLayoutManager(view?.context)
        setSpinner(object : SpinnerListener {
            override fun onItemChanged(type: String) {
                adapter.changeType(type)
            }
        })
    }

    private fun setSpinner(listener: SpinnerListener){
        spEventType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    /**Rather impossible*/
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int, id: Long
                ) {
                    val type = adapterView?.getItemAtPosition(position).toString()
                    listener.onItemChanged(type)
                }
            }
    }

}