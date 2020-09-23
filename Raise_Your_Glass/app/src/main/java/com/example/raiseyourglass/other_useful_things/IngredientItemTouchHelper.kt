package com.example.raiseyourglass.other_useful_things

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.adapters.IngredientListAdapter

object IngredientItemTouchHelper {

    private lateinit var adapter: IngredientListAdapter

    fun setAdapter(adapter: IngredientListAdapter){
        this.adapter = adapter
    }

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            adapter.deleteIngredientAt(viewHolder.adapterPosition)
        }
    }

    val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)

    fun attachToRV(rv: RecyclerView){
        itemTouchHelper.attachToRecyclerView(rv)
    }
}