package com.example.raiseyourglass.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.raiseyourglass.R
import com.example.raiseyourglass.activities.StartActivity
import com.example.raiseyourglass.firebase.Firebase
import com.example.raiseyourglass.other_useful_things.ThemeChanger
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = activity?.getSharedPreferences("darkMode", Context.MODE_PRIVATE)
        switchDarkMode.isChecked = sharedPref!!.getBoolean("isDarkMode", false)

        etNewName.setText(Firebase.getUserName() ?: "")

        btnLogout.setOnClickListener{
            Firebase.logout()
            activity?.finish()
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
            val activity = activity as StartActivity
            activity.bottomNavView?.removeBadge(R.id.miSettings)
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
        else{
            tvUserName.setTextColor(
                ContextCompat.getColor(
                    context!!,
                    R.color.secondary_text_color
                )
            )
        }

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPref.edit()
            editor.apply{
                putBoolean("isDarkMode", isChecked)
                apply()
            }
            ThemeChanger.changeThemeByBoolean(isChecked)
        }
    }

}