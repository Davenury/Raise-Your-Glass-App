package com.example.raiseyourglass.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.R
import com.example.raiseyourglass.adapters.IngredientsListAdapter
import com.example.raiseyourglass.adapters.StepsListAdapter
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.dataclasses.Ingredient
import com.example.raiseyourglass.dataclasses.Step
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.fragment_drink_modification.*
import kotlinx.android.synthetic.main.fragment_drink_modification.view.*

class DrinkModificationFragment(val drink: Drink) : Fragment(R.layout.fragment_drink_modification) {

    lateinit var drinkTypeArray:Array<String>

    private val isNewDrink = drink == Drink(owner = Firebase.getUserId().toString())

    val previousVersionDrink: Drink = drink

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drinkTypeArray = view.context.resources.getStringArray(R.array.drink_types)

        setValues()


        setRecycleViewIngredients()
        setRecycleViewSteps()

        btnAddIngredient.setOnClickListener {
            setChangedValuesRecyclerViews()
            drink.ingredients.add(Ingredient())
            setRecycleViewIngredients()
        }

        btnAddStep.setOnClickListener {
            setChangedValuesRecyclerViews()
            drink.steps.add(Step())
            setRecycleViewSteps()
        }

        btnSaveDrinkModification.setOnClickListener{
            drink.name = etDrinkName.text.toString()
            drink.type = spinnerType.selectedItem.toString()
            setChangedValuesRecyclerViews()
            Log.e("Drink",drink.toString())
            val drinkMap = makeDrinkMap()
            if(isNewDrink) Firebase.addDrink(drink)
            else Firebase.updateDrink(previousVersionDrink, drinkMap)
        }

    }

    private fun makeDrinkMap(): HashMap<String, Any> {
        val drinkMap = HashMap<String, Any>()
        drinkMap["name"] = drink.name
        drinkMap["type"] = drink.type
        drinkMap["owner"] = drink.owner
        drinkMap["ingredients"] = drink.ingredients.map{ ing -> HashMap<String, Any>().apply{
            this["name"] = ing.name
            this["quantity"] = ing.quantity
            this["measurement"] = ing.measurement
        } }
        drinkMap["steps"] = drink.steps.map{ elem -> elem.name }
        return drinkMap
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

    private fun setChangedValuesRecyclerViews(){
        drink.ingredients = (rvIngredientsList.adapter as IngredientsListAdapter).changedData
        drink.steps = (rvStepsList.adapter as StepsListAdapter).changedData
    }

    private fun setValues(){
        etDrinkName.setText(drink.name)
        val drinkTypePosition = drinkTypeArray.indexOf(drink.type)
        spinnerType.setSelection(drinkTypePosition)
    }

}