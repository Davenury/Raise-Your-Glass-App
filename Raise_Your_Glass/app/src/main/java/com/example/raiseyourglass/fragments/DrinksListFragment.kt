package com.example.raiseyourglass.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.adapters.DrinksListAdapter
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.firebase.Firebase
import com.example.raiseyourglass.listeners.FavoritesSpinnerListener
import kotlinx.android.synthetic.main.fragment_drinks_list.*


class DrinksListFragment(private val setCurrentFragment: (fragment:Fragment) -> Unit) : Fragment(R.layout.fragment_drinks_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DrinksListAdapter(setCurrentFragment)

        setRecyclerView(adapter)
        setSpinner(object : FavoritesSpinnerListener{
            override fun onItemChanged(type: String) {
                adapter.changeType(type)
            }
        })
    }

    private fun setRecyclerView(adapter: DrinksListAdapter){
        rvDrinksList.adapter = adapter
        rvDrinksList.layoutManager = LinearLayoutManager(view?.context)
    }
    
    private fun setSpinner(listener: FavoritesSpinnerListener) {
        spChooseDrinksFavorites.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    /**Rather impossible*/
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int, id: Long
                ) {
                    val type = adapterView?.getItemAtPosition(position).toString()
                    listener.onItemChanged(type)
                }
            }
    }

}