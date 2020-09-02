package com.example.raiseyourglass.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.raiseyourglass.R
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment(val finishActivity: () -> Unit) : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etNewName.setText(Firebase.getUserName() ?: "")

        btnLogout.setOnClickListener{
            Firebase.logout()
            finishActivity()
        }

        btnSaveChangedName.setOnClickListener{
            val newName = etNewName.text.toString()
            Firebase.updateUserName(newName)
        }
    }

}