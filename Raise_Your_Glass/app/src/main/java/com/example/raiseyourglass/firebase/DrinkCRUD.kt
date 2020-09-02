package com.example.raiseyourglass.firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.raiseyourglass.adapters.DrinksListAdapter
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.dataclasses.Ingredient
import com.example.raiseyourglass.dataclasses.Step
import com.example.raiseyourglass.observers.DrinksCallback
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

object DrinkCRUD {

    fun addDrink(
        drink: Drink,
        context: Context,
        drinkCollectionRef: CollectionReference
        ) = CoroutineScope(Dispatchers.IO).launch{
        try{
            drinkCollectionRef.add(drink).await()
            withContext(Dispatchers.Main){
                Toast.makeText(context, "Successfully added your drink!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getDrinksListWithoutRestrictions(
        context:Context,
        drinkCollectionRef: CollectionReference,
        drinksCallback: DrinksCallback
    ){
        val drinks = mutableListOf<Drink>()
        CoroutineScope(Dispatchers.IO).launch {
        try {
            val querySnapshot = drinkCollectionRef.get().await()
            for (document in querySnapshot.documents) {
                val drink = makeDrinkOutOfDocument(document)
                drinks.add(drink)
            }
            drinksCallback.onCallback(drinks)
        } catch (e: Exception) {
            withContext(Dispatchers.Main){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun makeDrinkOutOfDocument(document: DocumentSnapshot): Drink{
        //Log.d("Kurwa", document.data?.get("name").toString())
        val name = document.data?.get("name") as String
        val type = document.data?.get("type") as String
        val owner = document.data?.get("owner") as String
        val steps = document.data?.get("steps") as List<Step>
        val ingredients = document.data?.get("ingredients") as List<Ingredient>
        Log.d("Kurwa", "$name, $type, $owner")
        return Drink(name, type, owner, ingredients, steps)
    }

    fun updateDrink(
        drink: Drink,
        newDrinkMap: Map<String, Any>,
        context: Context,
        drinkCollectionRef: CollectionReference
    )
        = CoroutineScope(Dispatchers.IO).launch{
        val drinkQuery = drinkCollectionRef
            .whereEqualTo("name", drink.name)
            .whereEqualTo("type", drink.type)
            .whereEqualTo("owner", drink.owner)
            .get()
            .await()
        for(document in drinkQuery){
            try{
                drinkCollectionRef.document(document.id).set(
                    newDrinkMap,
                    SetOptions.merge()
                ).await()
            } catch (e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun deleteDrink(
        drink: Drink,
        context: Context,
        drinkCollectionRef: CollectionReference
    ) = CoroutineScope(Dispatchers.IO).launch{
        val drinkQuery = drinkCollectionRef
            .whereEqualTo("name", drink.name)
            .whereEqualTo("type", drink.type)
            .whereEqualTo("owner", drink.owner)
            .get()
            .await()
        for(document in drinkQuery){
            try{
                drinkCollectionRef.document(document.id).delete().await()
            } catch(e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun subscribeToDrinkSnapshotListener(
        context:Context,
        drinkCollectionRef: CollectionReference,
        adapter: DrinksListAdapter
    ){
        drinkCollectionRef
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let{
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                querySnapshot?.let{
                    val drinks = mutableListOf<Drink>()
                    for (document in it){
                        val drink = makeDrinkOutOfDocument(document)
                        drinks.add(drink)
                    }
                    adapter.drinksList = drinks
                    adapter.notifyDataSetChanged()
                }
            }
    }
}