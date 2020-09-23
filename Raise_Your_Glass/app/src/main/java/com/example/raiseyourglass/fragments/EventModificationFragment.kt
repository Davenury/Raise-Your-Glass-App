package com.example.raiseyourglass.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.R
import com.example.raiseyourglass.activities.StartActivity
import com.example.raiseyourglass.adapters.InviteUsersAdapter
import com.example.raiseyourglass.dataclasses.Event
import com.example.raiseyourglass.firebase.Firebase
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_event_modification.*
import kotlinx.android.synthetic.main.fragment_event_modification.btnAddEvent
import kotlinx.android.synthetic.main.invite_users_bottom_sheet.view.*
import kotlinx.android.synthetic.main.invite_users_bottom_sheet.view.rvInviteUsers
import java.time.ZoneId
import java.util.*

class EventModificationFragment(val event: Event = Event(ownerID = Firebase.getUserId()?:"")) :
    Fragment(R.layout.fragment_event_modification) {


    val startVersionOfEvent = event.copy()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        if(event.place!="")setValues()
    }

    private fun setListeners() {
        setAddEventListener()
        setInviteUsersListener()
        setDatePickerListener()
    }

    private fun setDatePickerListener() {
        val calendar = Calendar.getInstance()
        calendar.time = event.date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        ivDatePicker.setOnClickListener {
            val dialog = DatePickerDialog(
                activity!!,
                DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    val now = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    if (calendar.before(now.get(Calendar.DATE))) {
                        Toast.makeText(
                            activity,
                            "Please, select today or further days",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@OnDateSetListener
                    }
                    event.date = calendar.time
                },
                year, month, day
            )
            dialog.datePicker.minDate = System.currentTimeMillis() - 1000
            dialog.show()

            val localDate = event.dateToLocalDate()
            tvDatePicked.text =
                "You've picked ${localDate.dayOfMonth}-${localDate.month.value}-${localDate.year}"
        }
    }

    private fun setInviteUsersListener() {
        btnInviteUsers.setOnClickListener {
            val adapter = InviteUsersAdapter(event.invited)
            val bottomSheetDialog = BottomSheetDialog(activity!!)
            val view = layoutInflater.inflate(R.layout.invite_users_bottom_sheet, null)
            view.rvInviteUsers.apply {
                this.adapter = adapter
                this.layoutManager = LinearLayoutManager(view?.context)
            }
            view.btnAddInvitedUsers.setOnClickListener {
                val invited =
                    adapter.users.filter { (_, b) -> b }.map { (a, _) -> a.userID }.toMutableList()
                tvAddedNFriends.text = "Added ${invited.size} Friends"
                event.invited = invited
                bottomSheetDialog.dismiss()
            }
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }
    }

    private fun setAddEventListener() {
        btnAddEvent.setOnClickListener {
            event.place = etEventPlace.text.toString()
            event.isPrivate = cbIsEventPrivate.isChecked
            if (startVersionOfEvent.place=="") Firebase.addEvent(event)
            else Firebase.updateEvent(startVersionOfEvent, event.toMap())
            activity!!.onBackPressed()
        }
    }

    private fun setValues() {
        etEventPlace.setText(event.place)
        cbIsEventPrivate.isChecked = event.isPrivate
        tvAddedNFriends.text = "Added ${event.invited.size} Friends"
        val localDate = event.dateToLocalDate()
        tvDatePicked.text =
            "You've picked ${localDate.dayOfMonth}-${localDate.month.value}-${localDate.year}"
        btnAddEvent.setText("Update event")
    }
}