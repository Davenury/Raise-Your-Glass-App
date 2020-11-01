package com.example.raiseyourglass.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.adapters.DrinksListAdapter
import com.example.raiseyourglass.R
import com.example.raiseyourglass.activities.StartActivity
import com.example.raiseyourglass.listeners.SpinnerListener
import com.example.raiseyourglass.search_strategies.CompositeDrinkStrategies
import com.example.raiseyourglass.search_strategies.DrinkNameSearchStrategy
import com.example.raiseyourglass.search_strategies.DrinkSearchStrategy
import kotlinx.android.synthetic.main.fragment_drinks_list.*


class DrinksListFragment : Fragment(R.layout.fragment_drinks_list) {

    private var setCurrentFragment: (fragment: Fragment) -> Unit = { _ -> null }
    private val compositeDrinkStrategies = CompositeDrinkStrategies(mutableListOf())


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as StartActivity
        setCurrentFragment = activity.lambdaSetCurrentFragment

        val adapter = DrinksListAdapter(setCurrentFragment)

        setRecyclerView(adapter)
        setSpinner(object : SpinnerListener{
            override fun onItemChanged(type: String) {
                adapter.changeType(type)
            }
        })
        setSearchBar(adapter)
    }

    private fun setRecyclerView(adapter: DrinksListAdapter){
        rvDrinksList.adapter = adapter
        rvDrinksList.layoutManager = LinearLayoutManager(view?.context)
    }
    
    private fun setSpinner(listener: SpinnerListener) {
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

    private fun setSearchBar(adapter: DrinksListAdapter){
        svSearchDrinkByName.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(drinkName: String?): Boolean {
                compositeDrinkStrategies.removeNameStrategies()
                val drinkNameSearchStrategy = DrinkNameSearchStrategy(drinkName ?: "")
                compositeDrinkStrategies.addStrategy(drinkNameSearchStrategy)
                adapter.filter(compositeDrinkStrategies)
                return true
            }
        })
    }
}