package com.example.raiseyourglass.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Ingredient
import kotlinx.android.synthetic.main.item_ingredients_list_modification.view.*

class IngredientsListAdapter(val ingredientsList:MutableList<Ingredient>,
                             val removeIngredient: (position:Int) -> Unit
                             ) : RecyclerView.Adapter<IngredientsListAdapter.IngredientsListViewHolder>() {


    inner class IngredientsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    val changedData : MutableList<Ingredient> = ingredientsList

    enum class ingredientParam{ NAME,QUANTITY,MEASURMENT}

    inner class ingredientTextWatcher(val position: Int,val type:ingredientParam): TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun afterTextChanged(s: Editable?) {
            when(type){
                ingredientParam.NAME -> changedData[position].name = s.toString()?:""
                ingredientParam.MEASURMENT -> changedData[position].measurement = s.toString()?:""
                ingredientParam.QUANTITY -> changedData[position].quantity = s.toString().toDouble()
            }
        }
    }

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

        holder.itemView.etIngredientName.addTextChangedListener(ingredientTextWatcher(position,ingredientParam.NAME))
        holder.itemView.etMeasurement.addTextChangedListener(ingredientTextWatcher(position,ingredientParam.MEASURMENT))
        holder.itemView.etQuantity.addTextChangedListener(ingredientTextWatcher(position,ingredientParam.QUANTITY))

        holder.itemView.fabtnDeleteIngrediente.setOnClickListener{
            removeIngredient(position)
        }

    }

    override fun getItemCount(): Int = ingredientsList.size
}