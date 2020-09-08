package com.example.raiseyourglass.firebase

import android.content.Context
import android.widget.Toast
import com.example.raiseyourglass.adapters.DrinksListAdapter
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.dataclasses.Ingredient
import com.example.raiseyourglass.dataclasses.Step
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

object DrinkCRUD {

    private const val NAME = "name"
    private const val TYPE = "type"
    private const val OWNER = "owner"
    private const val STEPS = "steps"
    private const val INGREDIENTS = "ingredients"
    private const val QUANTITY = "quantity"
    private const val MEASUREMENT = "measurement"
    private const val USERID = "userID"
    private const val FAVORITES = "favorites"

    fun addDrink(
        drink: Drink,
        context: Context,
        drinkCollectionRef: CollectionReference
    ) = CoroutineScope(Dispatchers.IO).launch {
        try {

            drinkCollectionRef.add(drink.toMap()).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Successfully added your drink!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateDrink(
        drink: Drink,
        newDrinkMap: Map<String, Any>,
        context: Context,
        drinkCollectionRef: CollectionReference
    ) = CoroutineScope(Dispatchers.IO).launch {
        val drinkQuery = drinkCollectionRef
            .whereEqualTo(NAME, drink.name)
            .whereEqualTo(TYPE, drink.type)
            .whereEqualTo(OWNER, drink.owner)
            .get()
            .await()
        for (document in drinkQuery) {
            try {
                drinkCollectionRef.document(document.id).set(
                    newDrinkMap,
                    SetOptions.merge()
                ).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Successfully edited this drink!", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun deleteDrink(
        drink: Drink,
        context: Context,
        drinkCollectionRef: CollectionReference
    ) = CoroutineScope(Dispatchers.IO).launch {
        val drinkQuery = drinkCollectionRef
            .whereEqualTo(NAME, drink.name)
            .whereEqualTo(TYPE, drink.type)
            .whereEqualTo(OWNER, drink.owner)
            .get()
            .await()
        for (document in drinkQuery) {
            try {
                drinkCollectionRef.document(document.id).delete().await()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun subscribeToDrinkSnapshotListener(
        context: Context,
        drinkCollectionRef: CollectionReference,
        adapter: DrinksListAdapter,
        userFilter: String?
    ) {
        adapter.drinksList = mutableListOf()
        drinkCollectionRef
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                querySnapshot?.let {
                    val drinks = mutableListOf<Drink>()
                    for (document in it) {
                        val drink = makeDrinkOutOfDocument(document)
                        if (userFilter == null || drink.owner == userFilter) drinks.add(drink)
                    }
                    adapter.drinksList = drinks
                    adapter.notifyDataSetChanged()
                }
            }
    }

    private fun makeDrinkOutOfDocument(document: DocumentSnapshot): Drink {
        val name = document.data?.get(NAME) as String
        val type = document.data?.get(TYPE) as String
        val owner = document.data?.get(OWNER) as String
        val stepsStrings = document.data?.get(STEPS) as MutableList<String>
        val steps = stepsStrings.map { elem -> Step(elem) } as MutableList<Step>
        val ingredientsMap = document.data?.get(INGREDIENTS) as MutableList<HashMap<String, Any>>
        val ingredients = ingredientsMap.map { elem ->
            val quantity = elem[QUANTITY]
            Ingredient(
                elem.getOrDefault(NAME, "") as String,
                if(quantity is Long) quantity.toDouble() else quantity as Double,
                elem.getOrDefault(MEASUREMENT, "") as String
            )
        } as MutableList<Ingredient>
        return Drink(name, type, owner, ingredients,steps)
    }

    fun subscribeToFavoriteDrinkSnapshotListener(
        context: Context,
        adapter: DrinksListAdapter,
        userFilter: String?
    ){
        if(userFilter != null){
            adapter.drinksList = mutableListOf()
            Firebase.favoritesCollectionRef
                .whereEqualTo(USERID, userFilter)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    firebaseFirestoreException?.let {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                        return@addSnapshotListener
                    }

                    querySnapshot?.let {
                        for (document in it) {
                            val drinksList = document.get(FAVORITES) as MutableList<HashMap<String, Any>>
                            for (drinkMap in drinksList){
                                val drink = Drink.fromMap(drinkMap)
                                if(!adapter.drinksList.contains(drink)) adapter.drinksList.add(drink)
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
        }
    }
}