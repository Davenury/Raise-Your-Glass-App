package com.example.raiseyourglass.firebase

import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.dataclasses.Event
import java.util.*

object Validator {

    fun areValid(email: String, password: String):Boolean{
        return isPasswordValid(password) && isEmailValid(email)
    }

    fun isPasswordValid(password: String): Boolean{
        return password.isNotEmpty() && password.length >= 6
    }

    fun isEmailValid(email: String): Boolean{
        return email.isNotEmpty() && email.contains("@")
    }

    fun addDrinkValidator(drink: Drink): Boolean{
        if(drink.name == "") return false
        if(drink.type == "") return false
        return true
    }

    fun addEventValidator(event: Event): Boolean{
        if(event.date == Date(1000, 0, 1)) return false
        if(event.place.isEmpty()) return false
        return true
    }

    fun addIngredientValidator(name: String, quantity: String, measurement: String): Boolean{
        if(name.isNotEmpty() && quantity.isNotEmpty() && measurement.isNotEmpty()) return true
        return false
    }
}