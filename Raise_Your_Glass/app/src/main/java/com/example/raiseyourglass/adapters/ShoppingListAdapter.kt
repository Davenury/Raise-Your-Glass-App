package com.example.raiseyourglass.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Event
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.item_shopping_list.view.*

class ShoppingListAdapter(event: Event)
    : RecyclerView.Adapter<ShoppingListAdapter.ShoppingListHolder>(){

    var shoppingList: MutableList<String> = mutableListOf()

    init{
        Firebase.setAllAlcoholsToList(event, this)
    }

    inner class ShoppingListHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shopping_list, parent, false)
        return ShoppingListHolder(view)
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }

    override fun onBindViewHolder(holder: ShoppingListHolder, position: Int) {
        holder.itemView.apply{
            tvShoppingListName.text = shoppingList[position]
            cbIsAlreadyBought.setOnClickListener{
                if(cbIsAlreadyBought.isChecked){
                    tvShoppingListName.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.secondary_text_color
                        )
                    )
                    tvShoppingListName.paintFlags = tvShoppingListName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                else{
                    tvShoppingListName.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.text_color
                        )
                    )
                    tvShoppingListName.paintFlags = tvShoppingListName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
        }
    }
}