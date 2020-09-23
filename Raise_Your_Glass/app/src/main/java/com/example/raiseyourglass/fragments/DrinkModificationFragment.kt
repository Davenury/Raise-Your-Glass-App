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
import com.example.raiseyourglass.adapters.IngredientListAdapter
import com.example.raiseyourglass.adapters.StepListAdapter
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.dataclasses.Ingredient
import com.example.raiseyourglass.dataclasses.Step
import com.example.raiseyourglass.firebase.Firebase
import com.example.raiseyourglass.firebase.Validator
import com.example.raiseyourglass.other_useful_things.IngredientItemTouchHelper
import com.example.raiseyourglass.other_useful_things.StepsItemTouchHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_add_ingredient.*
import kotlinx.android.synthetic.main.bottom_sheet_add_ingredient.view.*
import kotlinx.android.synthetic.main.bottom_sheet_add_ingredient.view.etAddIngredientMeasurement
import kotlinx.android.synthetic.main.bottom_sheet_add_ingredient.view.etAddIngredientName
import kotlinx.android.synthetic.main.bottom_sheet_add_ingredient.view.etAddIngredientQuantity
import kotlinx.android.synthetic.main.bottom_sheet_add_step.view.*
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
        setRecyclerViews()
        setOnClickListeners()
    }

    private fun setRecyclerViews(){
        setIngredientsRV()
        setStepsRV()
    }

    private fun setIngredientsRV(){
        val adapter = IngredientListAdapter(drink.ingredients)
        rvIngredientsList.adapter = adapter
        rvIngredientsList.layoutManager = LinearLayoutManager(view?.context)
        IngredientItemTouchHelper.setAdapter(adapter)
        IngredientItemTouchHelper.attachToRV(rvIngredientsList)
    }
    private fun setStepsRV(){
        val adapter = StepListAdapter(drink.steps)
        rvStepsList.adapter = adapter
        rvStepsList.layoutManager = LinearLayoutManager(view?.context)
        StepsItemTouchHelper.setAdapter(adapter)
        StepsItemTouchHelper.attachToRV(rvStepsList)
    }

    private fun setOnClickListeners(){

        btnAddIngredient.setOnClickListener {
            addIngredient()
        }

        btnAddStep.setOnClickListener {
            addStep()
        }

        btnSaveDrinkModification.setOnClickListener{
            drink.name = etDrinkName.text.toString()
            drink.type = spinnerType.selectedItem.toString()
            drink.ingredients = (rvIngredientsList.adapter as IngredientListAdapter).ingredientsList
            drink.steps = (rvStepsList.adapter as StepListAdapter).stepsList
            Log.e("Drink",drink.toString())
            if(isNewDrink){
                if(currentFile != null) {
                    Firebase.addDrink(drink)
                    Firebase.uploadImageToStorage(drink.getImagePath(), currentFile!!)
                    activity!!.onBackPressed()
                }
                else{
                    Toast.makeText(context, "Please, select an image by clicking on this grey field!", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Firebase.updateDrink(previousVersionDrink, drink.toMap())
                activity!!.onBackPressed()
            }
        }
    }

    private fun addIngredient(){
        val bottomSheetDialog = BottomSheetDialog(activity!!)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_add_ingredient, null)
        var ingredient: Ingredient
        view.btnAddIngredientFromBottomSheet.setOnClickListener {
            val name = view.etAddIngredientName.text.toString()
            val quantity = view.etAddIngredientQuantity.text.toString()
            val measurement = view.etAddIngredientMeasurement.text.toString()
            if(Validator.addIngredientValidator(name, quantity, measurement)){
                ingredient = Ingredient(name, quantity.toDouble(), measurement)
                val adapter = rvIngredientsList.adapter as IngredientListAdapter
                adapter.addIngredient(ingredient)
                bottomSheetDialog.dismiss()
            }
            else{
                Toast.makeText(activity, "Please, enter every field!", Toast.LENGTH_SHORT).show()
            }
        }
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    private fun addStep(){
        val bottomSheetDialog = BottomSheetDialog(activity!!)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_add_step, null)
        var step: Step
        view.btnAddStepFromBottomSheet.setOnClickListener {
            val name = view.etAddStepName.text.toString()
            if(name.isNotEmpty()){
                step = Step(name)
                val adapter = rvStepsList.adapter as StepListAdapter
                adapter.addStep(step)
                bottomSheetDialog.dismiss()
            }
            else{
                Toast.makeText(activity, "Please, don't leave the entry empty!", Toast.LENGTH_SHORT).show()
            }
        }
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
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
        Firebase.setImageToView(drink.getImagePath(), imDrinkPhoto)
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