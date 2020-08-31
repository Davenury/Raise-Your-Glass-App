package com.example.raiseyourglass.dataclasses

data class Order(
    var userID: String = "",
    var orders: List<Drink> = mutableListOf(),
    var comments: List<String> = mutableListOf()
)