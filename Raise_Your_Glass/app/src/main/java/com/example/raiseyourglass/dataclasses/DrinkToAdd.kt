package com.example.raiseyourglass.dataclasses

data class DrinkToAdd(
    var name: String = "",
    var type: String = "",
    var owner: String = "",
    var ingredients: MutableList<Ingredient> = mutableListOf(),
    var steps: MutableList<String> = mutableListOf()
)