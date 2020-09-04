package com.example.raiseyourglass.dataclasses

data class Ingredient(
    var name: String = "",
    var quantity: Double = 0.0,
    var measurement: String = ""
){
    fun toMap():Map<String,Any> {
        return mapOf<String,Any>(
            "name" to name,
            "quantity" to quantity,
            "measurement" to measurement
        )
    }
}