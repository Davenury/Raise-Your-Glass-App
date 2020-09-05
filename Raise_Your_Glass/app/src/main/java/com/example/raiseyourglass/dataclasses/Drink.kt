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

    fun changeDrinkForm(): DrinkToAdd{
        val stringSteps = mutableListOf<String>()
        this.steps.forEach {
            stringSteps.add(it.name)
        }
        return DrinkToAdd(
            this.name,
            this.type,
            this.owner,
            this.ingredients,
            stringSteps
        )
    }
}