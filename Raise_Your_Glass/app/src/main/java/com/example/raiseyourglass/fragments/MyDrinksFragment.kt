package com.example.raiseyourglass.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.R
import com.example.raiseyourglass.activities.StartActivity
import com.example.raiseyourglass.adapters.DrinksListAdapter
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.fragment_my_drinks.*


class MyDrinksFragment : Fragment(R.layout.fragment_my_drinks) {

    private var setCurrentFragment: (fragment: Fragment) -> Unit = { _ -> null }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as StartActivity
        setCurrentFragment = activity.lambdaSetCurrentFragment

        val fragment = DrinkModificationFragment(Drink(owner = Firebase.getUserId().toString()))

        btnAddDrink.setOnClickListener{
            setCurrentFragment(fragment)
        }
    }

    override fun onStart() {
        super.onStart()
        setRecyclerView()
    }

    private fun setRecyclerView(){
        val adapter = DrinksListAdapter(setCurrentFragment,Firebase.getUserId())
        rvMyDrinksList.adapter = adapter
        rvMyDrinksList.layoutManager = LinearLayoutManager(view?.context)
    }

}