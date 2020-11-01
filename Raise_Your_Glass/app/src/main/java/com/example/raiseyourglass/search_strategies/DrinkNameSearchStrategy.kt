package com.example.raiseyourglass.search_strategies

import com.example.raiseyourglass.dataclasses.Drink

class DrinkNameSearchStrategy(
    private val nameFragment: String
) : DrinkSearchStrategy {

    override fun search(drink : Drink) : Boolean {
        return drink.name.contains(nameFragment)
    }

}