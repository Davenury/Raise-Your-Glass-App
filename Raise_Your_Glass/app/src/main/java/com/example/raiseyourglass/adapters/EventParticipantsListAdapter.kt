package com.example.raiseyourglass.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.User
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.item_participants_list.view.*

class EventParticipantsListAdapter(val participantsList:List<String>, val invitedList:List<String>)
    : RecyclerView.Adapter<EventParticipantsListAdapter.EventParticipantsListHolder>() {

    var userDataList: HashMap<String, User> = HashMap()

    init{
        Firebase.getInvitedUsersData(this)
    }

    inner class EventParticipantsListHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventParticipantsListHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_participants_list, parent, false)
        return EventParticipantsListHolder(view)
    }

    override fun onBindViewHolder(holder: EventParticipantsListHolder, position: Int) {
        holder.itemView.apply{
            val currentUser = userDataList[invitedList[position]]
            this.tvEventParticipant.text = if(currentUser?.name == "") currentUser.email else currentUser?.name
            val isParticipant = if(participantsList.contains(invitedList[position])) "Accepted" else "Not Accepted"
            tvEventIsParticipant.text = isParticipant
        }
    }

    override fun getItemCount(): Int = invitedList.size
}