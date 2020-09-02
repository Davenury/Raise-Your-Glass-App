package com.example.raiseyourglass.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.raiseyourglass.R
import com.example.raiseyourglass.firebase.Firebase
import com.example.raiseyourglass.fragments.*
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        if(!Firebase.isUserLogged()){
            Intent(this, LoginActivity::class.java).apply{
                startActivity(this)
            }
        }


        val drinkListFragment = DrinksListFragment()
        val myDrinkFragment = MyDrinksFragment()
        val eventsListFragment = EventsListFragment()
        val myEvents = MyEventsFragment()
        val settingsFragment = SettingsFragment { finish() }

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

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.flMainStage, fragment)
            commit()
        }
    }




}