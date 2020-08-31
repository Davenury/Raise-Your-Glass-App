package com.example.raiseyourglass.dataclasses

data class Drink(
    var name: String = "",
    var type: String = "",
    var ingredients: List<Ingredient> = mutableListOf(),
    var steps: List<Step> = mutableListOf()
)