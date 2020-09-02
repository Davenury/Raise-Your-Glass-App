package com.example.raiseyourglass.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.item_drinks_list.view.*

class DrinksListAdapter(userFilter:String ?= null) :
    RecyclerView.Adapter<DrinksListAdapter.DrinksListViewHolder>() {

    var drinksList: List<Drink> = listOf()

    init {
        Firebase.subscribeToDrinkSnapshotListener(this,userFilter)
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
    }

    override fun getItemCount(): Int {
        return drinksList.size
    }


}