package com.example.raiseyourglass.dataclasses

data class Drink(
    var name: String = "",
    var type: String = "",
    var owner: String = "",
    var ingredients: MutableList<Ingredient> = mutableListOf(),
    var steps: MutableList<Step> = mutableListOf()
){
    fun toMap():Map<String,Any> {
        return mapOf<String,Any>(
            "name" to name,
            "owner" to owner,
            "type" to type,
            "ingredients" to ingredients.map {elem -> elem.toMap()},
            "steps" to steps.map { elem -> elem.toString()}
        )
    }
}