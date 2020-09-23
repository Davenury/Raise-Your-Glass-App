package com.example.raiseyourglass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.adapters.DrinksListAdapter
import com.example.raiseyourglass.R
import com.example.raiseyourglass.activities.StartActivity
import com.example.raiseyourglass.listeners.SpinnerListener
import kotlinx.android.synthetic.main.fragment_drinks_list.*


class DrinksListFragment : Fragment(R.layout.fragment_drinks_list) {

    private var setCurrentFragment: (fragment: Fragment) -> Unit = { _ -> null }

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

}