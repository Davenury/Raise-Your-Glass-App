package com.example.raiseyourglass.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Ingredient
import com.example.raiseyourglass.dataclasses.Step
import kotlinx.android.synthetic.main.item_steps_list_modification.view.*

class StepListAdapter(
    var stepsList: MutableList<Step>
)
    : RecyclerView.Adapter<StepListAdapter.StepListHolder>() {
    inner class StepListHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepListHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_steps_list_modification, parent, false)
        return StepListHolder(view)
    }

    override fun getItemCount(): Int {
        return stepsList.size
    }

    override fun onBindViewHolder(holder: StepListHolder, position: Int) {
        holder.itemView.apply{
            etStepListStep.setText(stepsList[position].name)
        }
    }

    fun deleteStepAt(position: Int){
        stepsList.removeAt(position)
        notifyDataSetChanged()
    }

    fun addStep(step: Step){
        stepsList.add(step)
        notifyDataSetChanged()
    }
}