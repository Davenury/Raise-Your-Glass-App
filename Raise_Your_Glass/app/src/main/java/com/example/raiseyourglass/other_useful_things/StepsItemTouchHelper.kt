package com.example.raiseyourglass.other_useful_things

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.adapters.IngredientListAdapter
import com.example.raiseyourglass.adapters.StepListAdapter

object StepsItemTouchHelper {

    private lateinit var adapter: StepListAdapter

    fun setAdapter(adapter: StepListAdapter){
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
            adapter.deleteStepAt(viewHolder.adapterPosition)
        }
    }

    val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)

    fun attachToRV(rv: RecyclerView){
        itemTouchHelper.attachToRecyclerView(rv)
    }
}