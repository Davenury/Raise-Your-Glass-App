package com.example.raiseyourglass.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Event
import com.example.raiseyourglass.firebase.Firebase
import com.example.raiseyourglass.fragments.EventViewFragment
import kotlinx.android.synthetic.main.event_item.view.*

class AllAvailableEventsAdapter(val setCurrentFragment: (fragment: Fragment) -> Unit
) : RecyclerView.Adapter<AllAvailableEventsAdapter.AllAvailableEventsHolder>() {
    inner class AllAvailableEventsHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var eventsList: MutableList<Event> = mutableListOf()
    var constantEventsList: MutableList<Event> = mutableListOf()

    init {
        Firebase.subscribeToAllEventsListener(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllAvailableEventsHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return AllAvailableEventsHolder(view)
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    override fun onBindViewHolder(holder: AllAvailableEventsHolder, position: Int) {
        val event = eventsList[position]
        val status: String
        status = when {
            !event.isPrivate -> "Public"
            event.ownerID == Firebase.getUserId() -> "Owner"
            event.participants.contains(Firebase.getUserId()) -> "Private, you participate"
            event.invited.contains(Firebase.getUserId()) -> "Private, you are invited"
            else -> ""
        }

        holder.itemView.apply {
            this.tvEventStatus.text = status
            this.tvEventPlace.text = event.place
            Firebase.setDrinkOwner(event.ownerID, this.tvEventOwner)
            val localDate = event.dateToLocalDate()
            this.tvEventDate.text =
                "${localDate.year}-${localDate.month.value}-${localDate.dayOfMonth}"
        }

        val eventFragment = EventViewFragment(eventsList[position], setCurrentFragment)

        holder.itemView.setOnClickListener {
            setCurrentFragment(eventFragment)
        }
    }

    fun changeType(type: String){
        eventsList = constantEventsList
        eventsList = eventsList.filter { event -> filterFunction(event, type) } as MutableList<Event>
        this.notifyDataSetChanged()
    }

    private fun filterFunction(event: Event, type: String): Boolean{
        if(type == "All") return true
        if(type == "Public"){
            return !event.isPrivate
        }
        if(type == "Invited"){
            return event.invited.contains(Firebase.getUserId())
        }
        if(type == "Participant"){
            return event.participants.contains(Firebase.getUserId())
        }
        if(type == "Owner"){
            return event.ownerID == Firebase.getUserId()
        }
        return false
    }
}
