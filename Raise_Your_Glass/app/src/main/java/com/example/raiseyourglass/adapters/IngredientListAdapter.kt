package com.example.raiseyourglass.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.dataclasses.Ingredient
import kotlinx.android.synthetic.main.item_ingredients_list_modification.view.*

class IngredientListAdapter(
    var ingredientsList: MutableList<Ingredient>
)
    : RecyclerView.Adapter<IngredientListAdapter.IngredientListHolder>(){
    inner class IngredientListHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientListHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_ingredients_list_modification, parent, false)
        return IngredientListHolder(view)
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    override fun onBindViewHolder(holder: IngredientListHolder, position: Int) {
        val currentIngredient = ingredientsList[position]
        holder.itemView.apply{
            etIngredientListName.setText(currentIngredient.name)
            etIngredientListQuantity.setText("${currentIngredient.quantity}")
            etIngredientListMeasurement.setText(currentIngredient.measurement)
        }
    }

    fun deleteIngredientAt(position: Int){
        ingredientsList.removeAt(position)
        notifyDataSetChanged()
    }

    fun addIngredient(ingredient: Ingredient){
        ingredientsList.add(ingredient)
        notifyDataSetChanged()
    }
}