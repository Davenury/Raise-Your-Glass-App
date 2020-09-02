package com.example.raiseyourglass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Drink
import kotlinx.android.synthetic.main.fragment_drink_view.*

class DrinkViewFragment(val drink:Drink) : Fragment(R.layout.fragment_drink_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvName.text = drink.name
    }

}