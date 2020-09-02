package com.example.raiseyourglass.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Drink
import kotlinx.android.synthetic.main.fragment_own_drink.*


class OwnDrinkFragment(val drink: Drink) : Fragment(R.layout.fragment_own_drink) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewFragment: Fragment = DrinkViewFragment(drink)
        val modificationFragment: Fragment = DrinkModificationFragment(drink)

        setCurrentFragment(viewFragment)

        switchModifyRecipe.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) setCurrentFragment(modificationFragment)
            else setCurrentFragment(viewFragment)
        }

    }

    private fun setCurrentFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction().apply {
            if (this != null) {
                replace(R.id.flDrinkDescription, fragment)
                commit()
            } else {
                Log.e("OwnDrinkFragment","Fragment Manager problem")
            }
        }
    }

}