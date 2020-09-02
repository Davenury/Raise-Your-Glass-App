package com.example.raiseyourglass.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.adapters.DrinksListAdapter
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.observers.DrinksCallback
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.fragment_drinks_list.*


class DrinksListFragment : Fragment(R.layout.fragment_drinks_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val drinksList = mutableListOf(Drink("Moczajto", "Long Drink"),
          //  Drink("Angry dog", "shot"))

        setRecyclerView()
    }

    private fun setRecyclerView(){
        val drinksList: List<Drink> = mutableListOf()
        val adapter = DrinksListAdapter(drinksList)
        Firebase.subscribeToDrinkSnapshotListener(adapter)
        adapter.notifyDataSetChanged()
        rvDrinksList.adapter = adapter
        rvDrinksList.layoutManager = LinearLayoutManager(view?.context)
    }

}