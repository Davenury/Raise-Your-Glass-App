package com.example.raiseyourglass.search_strategies

import com.example.raiseyourglass.dataclasses.Drink

interface DrinkSearchStrategy {
    fun search(drink : Drink): Boolean
}