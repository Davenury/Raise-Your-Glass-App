package com.example.raiseyourglass.firebase

import android.content.Context
import android.view.ContextMenu
import android.widget.Toast
import com.example.raiseyourglass.dataclasses.Drink
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
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
        drinkCollectionRef: CollectionReference
    ): List<Drink>{
        val drinks = mutableListOf<Drink>()
        CoroutineScope(Dispatchers.IO).launch {
        try {
            val querySnapshot = drinkCollectionRef.get().await()
            for (document in querySnapshot.documents) {
                val drink = document.toObject<Drink>()
                if (drink != null)
                    drinks.add(drink)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        return drinks
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
}