package com.example.raiseyourglass.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.raiseyourglass.R
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        btnEvents.setOnClickListener{
            Intent(this,EventsActivity::class.java).also {
                startActivity(it)
            }
        }

        btnDrinks.setOnClickListener{
            Intent(this,DrinksActivity::class.java).also {
                startActivity(it)
            }
        }

        btnSettings.setOnClickListener{
            Intent(this,SettingsActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}