package com.example.raiseyourglass.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.R
import com.example.raiseyourglass.adapters.IngredientsListAdapter
import com.example.raiseyourglass.adapters.StepsListAdapter
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.dataclasses.Ingredient
import com.example.raiseyourglass.dataclasses.Step
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.fragment_drink_modification.*

class DrinkModificationFragment(val drink: Drink) : Fragment(R.layout.fragment_drink_modification) {

    private val REQUEST_CODE_IMAGE_PICK = 0
    private var currentFile: Uri? = null

    lateinit var drinkTypeArray:Array<String>

    private val isNewDrink = drink == Drink(owner = Firebase.getUserId().toString())

    val previousVersionDrink: Drink = drink

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drinkTypeArray = view.context.resources.getStringArray(R.array.drink_types)

        setValues()
        setRecycleViewIngredients()
        setRecycleViewSteps()
        setOnClickListeners()
    }

    private fun setOnClickListeners(){
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
            if(currentFile != null){
                if(isNewDrink) Firebase.addDrink(drink)
                else Firebase.updateDrink(previousVersionDrink, drink.toMap())
                Firebase.uploadImageToStorage(drink.getImagePath(), currentFile!!)
            }
            else{
                Toast.makeText(context, "Please, select an image by clicking on this grey field!", Toast.LENGTH_SHORT).show()
            }
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

    private fun setChangedValuesRecyclerViews(){
        drink.ingredients = (rvIngredientsList.adapter as IngredientsListAdapter).changedData
        drink.steps = (rvStepsList.adapter as StepsListAdapter).changedData
    }

    private fun setValues(){
        etDrinkName.setText(drink.name)
        val drinkTypePosition = drinkTypeArray.indexOf(drink.type)
        spinnerType.setSelection(drinkTypePosition)
        setImage()
    }

    private fun setImage(){
        imDrinkPhoto.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also{
                it.type = "image/*"
                startActivityForResult(it, REQUEST_CODE_IMAGE_PICK)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK){
            data?.data?.let{
                currentFile = it
                imDrinkPhoto.setImageURI(it)
            }
        }
    }

}