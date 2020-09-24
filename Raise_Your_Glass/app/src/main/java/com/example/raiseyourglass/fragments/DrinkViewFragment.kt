package com.example.raiseyourglass.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.fragment_drink_view.*
import kotlinx.android.synthetic.main.ingredient_item.view.*
import kotlinx.android.synthetic.main.item_step_details.view.*

class DrinkViewFragment(val drink: Drink) : Fragment(R.layout.fragment_drink_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDrinkDetails()
    }

    private fun setDrinkDetails(){
        tvName.text = drink.name
        tvDrinkType.text = drink.type
        setDrinkPhoto()
        Firebase.setDrinkOwner(drink.owner, tvDrinkOwner)
        setIngredients()
        setSteps()
        setFavorite()
    }

    private fun setDrinkPhoto(){
        Firebase.setImageToView(drink.getImagePath(), ivDrinkPhoto)
    }

    private fun setIngredients(){
        val tvIngredients = TextView(activity).apply{
            this.text = "Ingredients"
            this.textSize = 24F
            this.setTextColor(
                ContextCompat.getColor(
                activity!!.applicationContext,
                R.color.text_color
                )
            )
        }
        llDetails.addView(tvIngredients)

        for (ingredient in drink.ingredients){
            val ingView = LayoutInflater.from(activity).inflate(R.layout.ingredient_item, llDetails, false)
            ingView.tvIngName.text = ingredient.name
            ingView.tvIngMeasurement.text = ingredient.measurement
            ingView.tvIngQuantity.text = "${ingredient.quantity}"

            llDetails.addView(ingView)
        }
    }

    private fun setSteps(){
        val tvSteps = TextView(activity).apply{
            this.text = "Instructions"
            this.textSize = 24F
            this.setTextColor(
                ContextCompat.getColor(
                    activity!!.applicationContext,
                    R.color.text_color
                )
            )
        }
        llDetails.addView(tvSteps)

        for ((idx, step) in drink.steps.withIndex()){
            val stepView = LayoutInflater.from(activity).inflate(R.layout.item_step_details, llDetails, false)
            stepView.tvStepNumber.text = "${idx + 1}"
            stepView.tvStepInstruction.text = step.name

            llDetails.addView(stepView)
        }
    }

    private fun setFavorite(){
        val fullHeart = resources.getDrawable(R.drawable.ic_added_favorite)
        val border = resources.getDrawable(R.drawable.ic_favorite_border)
        Firebase.setFavoriteHeart(drink, ivFavoriteAdder, border, fullHeart)

        ivFavoriteAdder.setOnClickListener {
            if(ivFavoriteAdder.drawable.constantState == fullHeart.constantState){
                Firebase.deleteDrinkFromFavorites(drink)
            }else{
                Firebase.addDrinkToFavorites(drink)
            }
            Firebase.setFavoriteHeart(drink, ivFavoriteAdder, border, fullHeart)
        }
    }

}