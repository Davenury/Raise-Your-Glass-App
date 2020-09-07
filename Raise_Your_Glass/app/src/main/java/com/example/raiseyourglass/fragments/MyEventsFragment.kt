package com.example.raiseyourglass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Event
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.fragment_my_events.*
import java.time.LocalDate
import java.util.*


class MyEventsFragment : Fragment(R.layout.fragment_my_events) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date = LocalDate.of(2020,12,5)


        btnAddEvent.setOnClickListener {
            val event = Event.apply(
                date,
                "Twoja Stara",
                true,
                Firebase.getUserId()!!,
                listOf(),
                listOf(),
                listOf("f9F10BPhfUSK6Db6qpuCBJACqhA2")
            )
            Firebase.addEvent(event)
        }
    }
}