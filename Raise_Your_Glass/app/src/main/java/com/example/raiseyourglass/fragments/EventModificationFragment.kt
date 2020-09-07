package com.example.raiseyourglass.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.R
import com.example.raiseyourglass.adapters.InviteUsersAdapter
import com.example.raiseyourglass.adapters.MyEventsAdapter
import com.example.raiseyourglass.dataclasses.Event
import com.example.raiseyourglass.firebase.Firebase
import com.example.raiseyourglass.listeners.InvitedUsersListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.event_item.*
import kotlinx.android.synthetic.main.fragment_event_modification.*
import kotlinx.android.synthetic.main.fragment_event_modification.btnAddEvent
import kotlinx.android.synthetic.main.fragment_event_modification.view.*
import kotlinx.android.synthetic.main.fragment_my_events.*
import kotlinx.android.synthetic.main.invite_users_bottom_sheet.*
import kotlinx.android.synthetic.main.invite_users_bottom_sheet.view.*
import kotlinx.android.synthetic.main.invite_users_bottom_sheet.view.rvInviteUsers
import java.util.*

class EventModificationFragment : Fragment(R.layout.fragment_event_modification) {

    var invited = mutableListOf<String>()
    var date = Date(1000, 0, 1)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners(){
        setAddEventListener()
        setInviteUsersListener()
        setDatePickerListener()
    }

    private fun setDatePickerListener() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        ivDatePicker.setOnClickListener {
            val dialog = DatePickerDialog(activity!!,
                DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    date = calendar.time
                },
                year, month, day
            )
            dialog.show()
        }
    }

    private fun setInviteUsersListener() {
        btnInviteUsers.setOnClickListener {
            val adapter = InviteUsersAdapter(object: InvitedUsersListener{
                override fun onCheckItem(userID: String) {
                    invited.add(userID)
                }

                override fun onUncheckItem(userID: String) {
                    invited.remove(userID)
                }
            })
            val bottomSheetDialog = BottomSheetDialog(activity!!)
            val view = layoutInflater.inflate(R.layout.invite_users_bottom_sheet, null)
            view.rvInviteUsers.apply{
                this.adapter = adapter
                this.layoutManager = LinearLayoutManager(view?.context)
            }
            view.btnAddInvitedUsers.setOnClickListener {
                tvAddedNFriends.text = "Added ${invited.size} Friends"
                bottomSheetDialog.dismiss()
            }
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }
    }

    private fun setAddEventListener() {
        btnAddEvent.setOnClickListener {
            val place = etEventPlace.text.toString()
            val isPrivate = cbIsEventPrivate.isChecked
            val event = Event(
                Date(date.year, date.month, date.day),
                place,
                isPrivate,
                Firebase.getUserId()!!,
                mutableListOf(),
                mutableListOf(),
                invited
            )
            Firebase.addEvent(event)
        }
    }
}