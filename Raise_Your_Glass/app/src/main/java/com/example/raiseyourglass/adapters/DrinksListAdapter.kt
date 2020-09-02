package com.example.raiseyourglass.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.firebase.Firebase
import com.example.raiseyourglass.fragments.DrinkViewFragment
import com.example.raiseyourglass.fragments.OwnDrinkFragment
import kotlinx.android.synthetic.main.item_drinks_list.view.*

class DrinksListAdapter(val setCurrentFragment: (fragment: Fragment) -> Unit, val userFilter: String? = null) :
    RecyclerView.Adapter<DrinksListAdapter.DrinksListViewHolder>() {

    var drinksList: List<Drink> = listOf()

    init {
        Firebase.subscribeToDrinkSnapshotListener(this, userFilter)
    }


    inner class DrinksListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinksListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_drinks_list, parent, false)
        return DrinksListViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrinksListViewHolder, position: Int) {
        holder.itemView.apply {
            tvDrinkName.text = drinksList[position].name
//          if change to enum  tvDrinkType.text = drinksList[position].type.name
            tvDrinkType.text = drinksList[position].type
        }

        val drinkFragment =
            if (userFilter != null) OwnDrinkFragment(drinksList[position]) else DrinkViewFragment(
                drinksList[position]
            )

        holder.itemView.setOnClickListener {
            setCurrentFragment(drinkFragment)
        }

    }

    override fun getItemCount(): Int {
        return drinksList.size
    }


}