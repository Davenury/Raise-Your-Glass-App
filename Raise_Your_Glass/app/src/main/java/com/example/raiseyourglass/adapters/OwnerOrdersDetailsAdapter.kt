package com.example.raiseyourglass.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.dataclasses.Event
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.item_order_details.view.*

class OwnerOrdersDetailsAdapter(event: Event)
    : RecyclerView.Adapter<OwnerOrdersDetailsAdapter.OwnerOrdersDetailsHolder>(){
    inner class OwnerOrdersDetailsHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    var pairList: MutableList<Pair<Drink, Int>> = mutableListOf()

    init{
        Firebase.setAllDrinksFromEventToPairs(event, this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnerOrdersDetailsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_details, parent, false)
        return OwnerOrdersDetailsHolder(view)
    }

    override fun getItemCount(): Int {
        return pairList.size
    }

    override fun onBindViewHolder(holder: OwnerOrdersDetailsHolder, position: Int) {
        val currentDrink = pairList[position]
        holder.itemView.apply{
            this.tvOrdersDetailsDrinkName.text = currentDrink.first.name
            this.tvOrdersDetailsDrinksQuantity.text = "${currentDrink.second}"
        }
    }
}