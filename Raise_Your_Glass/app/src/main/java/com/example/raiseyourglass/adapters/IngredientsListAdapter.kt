package com.example.raiseyourglass.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Ingredient
import kotlinx.android.synthetic.main.item_ingredients_list_modification.view.*

class IngredientsListAdapter(val ingredientsList:List<Ingredient>,
                             val removeIngredient: (position:Int) -> Unit
                             ) : RecyclerView.Adapter<IngredientsListAdapter.IngredientsListViewHolder>() {


    inner class IngredientsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_ingredients_list_modification, parent, false)
        return IngredientsListViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientsListViewHolder, position: Int) {
        holder.itemView.apply {
            etIngredientName.setText(ingredientsList[position].name)
            etMeasurement.setText(ingredientsList[position].measurement)
            etQuantity.setText(ingredientsList[position].quantity.toString())


        }

        holder.itemView.fabtnDeleteIngrediente.setOnClickListener{
            removeIngredient(position)
        }

    }

    override fun getItemCount(): Int = ingredientsList.size
}