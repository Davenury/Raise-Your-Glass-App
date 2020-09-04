package com.example.raiseyourglass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.fragment_drink_view.*

class DrinkViewFragment(val drink: Drink) : Fragment(R.layout.fragment_drink_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDrinkDetails()
    }

    private fun setDrinkDetails(){
        tvName.text = drink.name
        tvDrinkType.text = drink.type
         Firebase.setOwner(drink.owner, tvDrinkOwner)
    }

}