package com.example.raiseyourglass.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.R
import com.example.raiseyourglass.adapters.EventParticipantsListAdapter
import com.example.raiseyourglass.dataclasses.Event
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.fragment_event_view.*

class EventViewFragment(val event: Event) : Fragment(R.layout.fragment_event_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setEventDetails()
        setRecycleViewParticipant()
    }

    private fun setRecycleViewParticipant() {
        val adapter = EventParticipantsListAdapter(event.participants,event.invited)
        rvEventParticipants.adapter = adapter
        rvEventParticipants.layoutManager = LinearLayoutManager(view?.context)
    }

    private fun setEventDetails() {
        Log.e("Error","Whata")
        Firebase.setUserToTextView(event.ownerID,tvEventHostName)
        tvEventIsPrivate.setText(if(event.isPrivate)"Private" else "Public")
        val localDate = event.dateToLocalDate()
        tvEventDate.setText("${localDate.year}-${localDate.month.value}-${localDate.dayOfMonth}")
        tvEventPlace.setText(event.place)
    }

}