package com.example.raiseyourglass.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.raiseyourglass.R
import com.example.raiseyourglass.firebase.Firebase
import com.example.raiseyourglass.fragments.*
import com.example.raiseyourglass.other_useful_things.ThemeChanger
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    var bottomNavView: BottomNavigationView? = null

    val lambdaSetCurrentFragment = { fragment: Fragment -> setCurrentFragment(fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val sharedPref = getSharedPreferences("darkMode", Context.MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("isDarkMode", false)

        ThemeChanger.changeThemeByBoolean(isDarkMode)

        if(!Firebase.isUserLogged()){
            Intent(this, LoginActivity::class.java).apply{
                startActivity(this)
            }
        }

        val drinkListFragment = DrinksListFragment()
        val myDrinkFragment = MyDrinksFragment()
        val eventsListFragment = EventsListFragment()
        val myEvents = MyEventsFragment()
        val settingsFragment = SettingsFragment()
        bottomNavView = bottomNavigationView
        setCurrentFragment(drinkListFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.miDrink -> setCurrentFragment(drinkListFragment)
                R.id.miMyDrinks -> setCurrentFragment(myDrinkFragment)
                R.id.miEvents -> setCurrentFragment(eventsListFragment)
                R.id.miMyEvents -> setCurrentFragment(myEvents)
                R.id.miSettings -> setCurrentFragment(settingsFragment)
            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        Firebase.setContext(this)
        if(Firebase.getUserName() == null || Firebase.getUserName() == ""){
            bottomNavigationView.getOrCreateBadge(R.id.miSettings).apply{
                number = 1
                isVisible = true
            }
        }
    }

    fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.flMainStage, fragment)
            commit()
        }
    }


}