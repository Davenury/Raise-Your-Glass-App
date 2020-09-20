package com.example.raiseyourglass.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.raiseyourglass.R
import com.example.raiseyourglass.firebase.Firebase
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment(
    val bottomNavigationView: BottomNavigationView,
    val finishActivity: () -> Unit
) : Fragment(R.layout.fragment_settings) {

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
            tvUserName.setTextColor(
                ContextCompat.getColor(
                    context!!,
                    R.color.text_color
                )
            )
            bottomNavigationView.removeBadge(R.id.miSettings)
        }

        btnDisplayUID.setOnClickListener {
            tvUID.text = Firebase.getUserId()
        }

        if(Firebase.getUserName() == null || Firebase.getUserName() == ""){
            tvUserName.setTextColor(
                ContextCompat.getColor(
                    context!!,
                    R.color.bad
                )
            )
        }
    }

}