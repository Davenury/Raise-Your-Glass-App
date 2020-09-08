package com.example.raiseyourglass.adapters

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Event
import com.example.raiseyourglass.firebase.Firebase
import com.example.raiseyourglass.fragments.EventModificationFragment
import kotlinx.android.synthetic.main.event_item.view.*
import kotlinx.android.synthetic.main.event_item.view.tvEventDate
import kotlinx.android.synthetic.main.event_item.view.tvEventPlace
import kotlinx.android.synthetic.main.item_my_event.view.*

class MyEventsAdapter(val setCurrentFragment: (fragment:Fragment) -> Unit) : RecyclerView.Adapter<MyEventsAdapter.MyEventsListViewHolder>() {

    var myEvents: MutableList<Event> = mutableListOf()

    inner class MyEventsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    init{
        Firebase.myEventsSnapshotListener(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyEventsListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_event, parent, false)
        return MyEventsListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myEvents.size
    }

    override fun onBindViewHolder(holder: MyEventsListViewHolder, position: Int) {
        val event = myEvents[position]

        holder.itemView.apply{
            this.tvEventPlace.text = event.place
            val localDate = event.dateToLocalDate()
            this.tvEventDate.text = "${localDate.dayOfMonth}-${localDate.month.value}-${localDate.year}"
            this.imDeleteEvent.setOnClickListener{
                createAlertDialog(position).show()
            }
        }

        holder.itemView.setOnClickListener {
            val fragment = EventModificationFragment(event)
            setCurrentFragment(fragment)
        }


    }


    private fun createAlertDialog(position: Int):AlertDialog{
        return AlertDialog.Builder(Firebase.getContext())
            .setTitle("Event Deletion")
            .setIcon(R.drawable.ic_deletion_warning)
            .setMessage("Are you sure to delete this event?")
            .setPositiveButton("Yes, delete this event"
            ) { _, _ ->
                Firebase.deleteEvent(myEvents[position])
            }
            .setNegativeButton("No, don't delete it") { _, _ ->
                Toast.makeText(Firebase.getContext(), "You decided not to delete this event", Toast.LENGTH_SHORT).show()
            }
            .create()
    }
}