package com.example.raiseyourglass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.adapters.DrinksListAdapter
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Drink
import kotlinx.android.synthetic.main.fragment_drinks_list.*


class DrinksListFragment : Fragment(R.layout.fragment_drinks_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drinksList = mutableListOf(Drink("Moczajto", "Long Drink"),
            Drink("Angry dog", "shot"))

        val adapter = DrinksListAdapter(drinksList)
        rvDrinksList.adapter = adapter
        rvDrinksList.layoutManager = LinearLayoutManager(view.context)


    }

}