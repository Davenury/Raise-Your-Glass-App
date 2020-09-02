package com.example.raiseyourglass.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.R
import com.example.raiseyourglass.adapters.DrinksListAdapter
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.fragment_my_drinks.*


class MyDrinksFragment(private val setCurrentFragment: (fragment:Fragment) -> Unit) : Fragment(R.layout.fragment_my_drinks) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddDrink.setOnClickListener{
            Toast.makeText(view.context,"New drink add",Toast.LENGTH_LONG).show()
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