package com.example.raiseyourglass.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.dataclasses.Event
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.item_order_drinks.view.*

class OrderDrinksAdapter(event: Event)
    : RecyclerView.Adapter<OrderDrinksAdapter.OrderDrinksHolder>() {
    inner class OrderDrinksHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var drinksList: MutableList<Drink> = mutableListOf()
    var drinksOrdersAlreadyMade: MutableList<Drink> = mutableListOf()
    var drinksOrders: MutableList<Pair<Drink, Boolean>> = mutableListOf()
    var constantList: MutableList<Drink> = mutableListOf()

    init{
        Firebase.subscribeToDrinkSnapshotListener(this, null)
        Firebase.getAllDrinksFromOrder(event, this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDrinksHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_drinks, parent, false)
        return OrderDrinksHolder(view)
    }

    override fun getItemCount(): Int {
        return drinksList.size
    }

    override fun onBindViewHolder(holder: OrderDrinksHolder, position: Int) {
        val currentDrink = drinksList[position]
        holder.itemView.apply{
            tvOrderDrinkName.text = currentDrink.name
            if(currentDrink in drinksOrdersAlreadyMade){
                cbOrderDrink.isChecked = true
                drinksOrders.add(Pair(currentDrink, true))
            }
            this.cbOrderDrink.setOnClickListener{
                if(this.cbOrderDrink.isChecked){
                    drinksOrders.add(Pair(currentDrink, true))
                }
                else{
                    drinksOrders.add(Pair(currentDrink, false))
                }
            }
        }
    }

    fun filterByName(name: String){
        drinksList = constantList
        drinksList = drinksList.filter { drink -> filterFunction(drink, name) } as MutableList<Drink>
    }

    private fun filterFunction(drink: Drink, name: String) : Boolean{
        return drink.name.contains(name, true)
    }
}