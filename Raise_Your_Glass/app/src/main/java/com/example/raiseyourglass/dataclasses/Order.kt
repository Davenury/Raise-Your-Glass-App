package com.example.raiseyourglass.dataclasses

import com.google.firebase.firestore.DocumentReference

data class Order(
    var userID: String = "",
    var drinksOrders: List<DocumentReference> = mutableListOf(),
    var comment: String = "",
    var eventID: String = ""
)