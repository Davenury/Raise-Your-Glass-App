package com.example.raiseyourglass.dataclasses

import com.google.firebase.firestore.DocumentReference

data class Drink(
    var name: String = "",
    var type: String = "",
    var owner: String = "",
    var ingredients: MutableList<Ingredient> = mutableListOf(),
    var steps: MutableList<Step> = mutableListOf(),
    val documentID: DocumentReference? = null
){

    fun getImagePath(): String{
        return name + owner
    }

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

    companion object{
        fun fromMap(map: Map<String, Any>,documentID: DocumentReference): Drink{
            val name = map["name"] as String
            val type = map["type"] as String
            val owner = map["owner"] as String
            val ingredientsMaps = map["ingredients"] as List<HashMap<String, Any>>
            val ingredients = ingredientsMaps.map{ ingMap ->
                val ingQuantity = ingMap["quantity"]
                Ingredient(
                    ingMap["name"] as String,
                     if(ingQuantity is Long)ingQuantity.toDouble() else ingQuantity as Double,
                    ingMap["measurement"] as String
                )
            } as MutableList<Ingredient>
            val steps = map["steps"] as MutableList<String>
            return Drink(
                name,
                type,
                owner,
                ingredients,
                steps.map{ step -> Step(step)} as MutableList<Step>,
                documentID
            )
        }
    }
}