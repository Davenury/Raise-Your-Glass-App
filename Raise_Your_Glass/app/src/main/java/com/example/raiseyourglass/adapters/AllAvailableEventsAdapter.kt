package com.example.raiseyourglass.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Event
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.event_item.view.*
import java.time.ZoneId
import java.util.*

class AllAvailableEventsAdapter(
    var eventsList: MutableList<Event>
) : RecyclerView.Adapter<AllAvailableEventsAdapter.AllAvailableEventsHolder>(){
    inner class AllAvailableEventsHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    init{
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
        Log.d("Kurwa", event.place)
        val status: String
        status = if(!event.isPrivate) "Public"
        else if(event.ownerID == Firebase.getUserId()) "Owner"
        else if(event.invited.contains(Firebase.getUserId())) "Private, you are invited"
        else if(event.participants.contains(Firebase.getUserId())) "Private, you participate"
        else ""

        holder.itemView.apply{
            this.tvEventStatus.text = status
            this.tvEventPlace.text = event.place
            val localDate = event.dateToLocalDate()
            this.tvEventDate.text = "${localDate.year}-${localDate.month.value}-${localDate.dayOfMonth}"
        }
    }
}