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

        Firebase.setContext(view.context)
        setEventDetails()
        setRecycleViewParticipant()
    }

    private fun setRecycleViewParticipant() {
        val userID = Firebase.getUserId()
        val filterNotFunction = {elem:String -> elem == userID}
        val adapter = EventParticipantsListAdapter(event.participants.filterNot(filterNotFunction),
            event.invited.filterNot(filterNotFunction))
        rvEventParticipants.adapter = adapter
        rvEventParticipants.layoutManager = LinearLayoutManager(view?.context)
    }

    private fun setEventDetails() {
        Firebase.setUserToTextView(event.ownerID, tvEventHostName)
        tvEventIsPrivate.setText(if (event.isPrivate) "Private" else "Public")
        val localDate = event.dateToLocalDate()
        tvEventDate.setText("${localDate.year}-${localDate.month.value}-${localDate.dayOfMonth}")
        tvEventPlace.setText(event.place)

        if (event.ownerID == Firebase.getUserId()) {
            switchInvitation.visibility = View.GONE
        } else {
            val userID = Firebase.getUserId()
            switchInvitation.isChecked = event.participants.contains(userID)
            switchInvitation.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked && userID != null)Firebase.participate(event)
                else if (!isChecked && userID != null)Firebase.participate(event)
            }
        }
    }

}