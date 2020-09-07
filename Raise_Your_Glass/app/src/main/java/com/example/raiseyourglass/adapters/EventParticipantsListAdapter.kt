package com.example.raiseyourglass.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.item_participants_list.view.*

class EventParticipantsListAdapter(val participantsList:List<String>,val invitedList:List<String>)
    : RecyclerView.Adapter<EventParticipantsListAdapter.EventParticipantsListHolder>() {

    inner class EventParticipantsListHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventParticipantsListHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_participants_list, parent, false)
        return EventParticipantsListHolder(view)
    }

    override fun onBindViewHolder(holder: EventParticipantsListHolder, position: Int) {
        holder.itemView.apply{
            Firebase.setUserToTextView(invitedList[position],tvEventParticipant)
            val isParticipant = if(participantsList.contains(invitedList[position])) "Accepted" else "Not Accepted"
            tvEventIsParticipant.setText(isParticipant)
        }
    }

    override fun getItemCount(): Int = invitedList.size
}