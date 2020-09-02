package com.example.raiseyourglass.observers

import com.example.raiseyourglass.dataclasses.Drink

interface DrinksCallback {
    fun onCallback(drinks: List<Drink>)
}