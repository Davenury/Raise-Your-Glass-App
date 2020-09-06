package com.example.raiseyourglass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Event
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.fragment_my_events.*
import java.util.*

/**Please, remember, that when you make a Date (unless you'll find another method for displaying time, 
 * I'm fed up with this shit), months go from 0 to 11*/
class MyEventsFragment : Fragment(R.layout.fragment_my_events) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date = Date(2020, 11, 5)

        btnAddEvent.setOnClickListener {
            val event = Event(
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