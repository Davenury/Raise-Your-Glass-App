package com.example.raiseyourglass.other_useful_things

import android.app.Activity
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.raiseyourglass.activities.LoginActivity
import com.example.raiseyourglass.activities.StartActivity

object ThemeChanger {
    fun changeTheme(source: Any) {
        val resources: Any
        resources = if(source is Activity){
            source.resources
        } else{
            val newSource = source as Fragment
            newSource.resources
        }
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Configuration.UI_MODE_NIGHT_NO ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    fun changeThemeByBoolean(boolean: Boolean){
        if(boolean){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}