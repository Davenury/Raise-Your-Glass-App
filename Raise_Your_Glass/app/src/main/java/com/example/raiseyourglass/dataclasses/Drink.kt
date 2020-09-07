package com.example.raiseyourglass.dataclasses

data class Drink(
    var name: String = "",
    var type: String = "",
    var owner: String = "",
    var ingredients: MutableList<Ingredient> = mutableListOf(),
    var steps: MutableList<Step> = mutableListOf()
){
    fun toMap():Map<String,Any> {
        val drinkMap = HashMap<String, Any>()
        drinkMap["name"] = name
        drinkMap["type"] = type
        drinkMap["owner"] = owner
        drinkMap["ingredients"] = ingredients.map{ ing -> HashMap<String, Any>().apply{
            this["name"] = ing.name
            this["quantity"] = ing.quantity
            this["measurement"] = ing.measurement
        } }
        drinkMap["steps"] = steps.map{ elem -> elem.name }
        return drinkMap
    }
}