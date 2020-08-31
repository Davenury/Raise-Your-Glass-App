package com.example.raiseyourglass.dataclasses

data class Favorites(
    var userID: String = "",
    var favorites: List<Drink> = mutableListOf()
)