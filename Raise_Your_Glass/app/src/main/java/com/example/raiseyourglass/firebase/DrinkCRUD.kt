package com.example.raiseyourglass.firebase

import android.content.Context
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.example.raiseyourglass.adapters.DrinksListAdapter
import com.example.raiseyourglass.dataclasses.Drink
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.model.Document
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
                        val drink = Drink.fromMap(document.data, document.reference)
                        if (userFilter == null || drink.owner == userFilter) drinks.add(drink)
                    }
                    adapter.drinksList = drinks
                    adapter.notifyDataSetChanged()
                }
            }
    }

    fun subscribeToFavoriteDrinkSnapshotListener(
        context: Context,
        adapter: DrinksListAdapter,
        userFilter: String?
    ) {
        if (userFilter != null) {
            adapter.drinksList = mutableListOf()
            Firebase.favoritesCollectionRef
                .whereEqualTo(USERID, userFilter)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    firebaseFirestoreException?.let {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                        return@addSnapshotListener
                    }

                    val drinkList: MutableList<Drink> = mutableListOf()

                    querySnapshot?.let {

                        CoroutineScope(Dispatchers.Default).launch {
                            for (document in it) {
                                val drinksList =
                                    document.get(FAVORITES) as MutableList<DocumentReference>
                                for (drinkMap in drinksList) {

                                    val drinkDocument = drinkMap.get().await()

                                    val drink = Drink.fromMap(
                                        drinkDocument.data as Map<String, Any>,
                                        drinkDocument.reference
                                    )
                                    Log.e("drink:", drink.toString())
                                    drinkList.add(drink)
                                }
                                adapter.drinksList = drinkList
                                Log.e("drinkList: ", drinkList.toString())
                            }
                            withContext(Dispatchers.Main) {
                                adapter.drinksList = drinkList
                                Log.e("List length", adapter.drinksList.toString())
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
        }
    }
}