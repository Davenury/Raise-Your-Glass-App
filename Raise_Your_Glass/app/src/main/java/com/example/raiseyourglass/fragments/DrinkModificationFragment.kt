package com.example.raiseyourglass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.R
import com.example.raiseyourglass.adapters.IngredientsListAdapter
import com.example.raiseyourglass.adapters.StepsListAdapter
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.dataclasses.Ingredient
import com.example.raiseyourglass.dataclasses.Step
import kotlinx.android.synthetic.main.fragment_drink_modification.*

class DrinkModificationFragment(val drink:Drink) : Fragment(R.layout.fragment_drink_modification) {

    lateinit var drinkTypeArray:Array<String>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drinkTypeArray = view.context.resources.getStringArray(R.array.drink_types)

        setValues()

        setRecycleViewIngredients()
        setRecycleViewSteps()

        btnAddIngredient.setOnClickListener {
            drink.ingredients.add(Ingredient())
            setRecycleViewIngredients()
        }

        btnAddStep.setOnClickListener {
            drink.steps.add(Step())
            setRecycleViewSteps()
        }

        btnSaveDrinkModification.setOnClickListener{
            rvIngredientsList.adapter
        }

    }


    private fun setRecycleViewIngredients(){
        val adapter = IngredientsListAdapter(drink.ingredients){position -> drink.ingredients.removeAt(position);  setRecycleViewIngredients()}
        rvIngredientsList.adapter = adapter
        rvIngredientsList.layoutManager = LinearLayoutManager(view?.context)
    }

    private fun setRecycleViewSteps(){
        val adapter = StepsListAdapter(drink.steps){position -> drink.steps.removeAt(position);  setRecycleViewSteps()}
        rvStepsList.adapter = adapter
        rvStepsList.layoutManager = LinearLayoutManager(view?.context)
    }

    private fun setValues(){
        etDrinkName.setText(drink.name)
        val drinkTypePosition = drinkTypeArray.indexOf(drink.type)
        spinnerType.setSelection(drinkTypePosition)
    }

}