package com.example.raiseyourglass.search_strategies

import com.example.raiseyourglass.dataclasses.Drink

class CompositeDrinkStrategies(
    private var strategies: MutableList<DrinkSearchStrategy>
) {
    fun isFitting(drink: Drink): Boolean{
        return strategies.all { strategy -> strategy.search(drink) }
    }

    fun addStrategy(strategy: DrinkSearchStrategy){
        this.strategies.add(strategy)
    }

    fun removeNameStrategies(){
        this.strategies = this.strategies.filter { strategy -> nameFilterFunction(strategy) } as MutableList<DrinkSearchStrategy>
    }

    private fun nameFilterFunction(strategy: DrinkSearchStrategy) : Boolean{
        return strategy !is DrinkNameSearchStrategy
    }
}