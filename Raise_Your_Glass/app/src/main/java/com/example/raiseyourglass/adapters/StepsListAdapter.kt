package com.example.raiseyourglass.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Step
import kotlinx.android.synthetic.main.item_ingredients_list_modification.view.*
import kotlinx.android.synthetic.main.item_steps_list_modification.view.*

class StepsListAdapter(val stepsList: MutableList<Step>, val removeStep: (position:Int) -> Unit) :
    RecyclerView.Adapter<StepsListAdapter.StepsListViewHolder>() {

    inner class StepsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepsListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_steps_list_modification, parent, false)
        return StepsListViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepsListViewHolder, position: Int) {
        holder.itemView.apply {
            etStep.setText(stepsList[position].name)
        }

        holder.itemView.fabtnDeleteStep.setOnClickListener {
            removeStep(position)
        }
    }

    override fun getItemCount(): Int = stepsList.size

}