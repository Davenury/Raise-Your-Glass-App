package com.example.raiseyourglass.firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.dataclasses.Order
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object OrdersCRUD {

    private const val USERID = "userID"
    private const val EVENTID = "eventID"
    private const val DRINKS = "drinksOrders"
    private const val COMMENTS = "comments"

    fun addOrder(
        context: Context,
        ordersCollection: CollectionReference,
        order: Order
    ) = CoroutineScope(Dispatchers.IO).launch{
        try{
            ordersCollection.add(order)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Successfully added your order!", Toast.LENGTH_SHORT).show()
            }
        } catch(e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun addDrink(
        context: Context,
        ordersCollection: CollectionReference,
        order: Order,
        drink: Drink
    ) = CoroutineScope(Dispatchers.IO).launch{
        try{
            val ordersQuery = ordersCollection
                .whereEqualTo(USERID, Firebase.getUserId())
                .whereEqualTo(EVENTID, order.eventID)
                .get()
                .await()
            for(doc in ordersQuery){
                ordersCollection.document(doc.id).update(
                    DRINKS, FieldValue.arrayUnion(drink.documentID)
                )
            }
        } catch(e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun deleteDrink(
        context: Context,
        ordersCollection: CollectionReference,
        order: Order,
        drink: Drink
    ) = CoroutineScope(Dispatchers.IO).launch{
        try{
            val ordersQuery = ordersCollection
                .whereEqualTo(USERID, Firebase.getUserId())
                .whereEqualTo(EVENTID, order.eventID)
                .get()
                .await()
            for(doc in ordersQuery){
                ordersCollection.document(doc.id).update(
                    DRINKS, FieldValue.arrayRemove(drink.documentID)
                )
            }
        } catch(e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun addComment(
        context: Context,
        ordersCollection: CollectionReference,
        order: Order,
        comment: String
    ) = CoroutineScope(Dispatchers.IO).launch{
        try{
            val ordersQuery = ordersCollection
                .whereEqualTo(USERID, Firebase.getUserId())
                .whereEqualTo(EVENTID, order.eventID)
                .get()
                .await()
            for(doc in ordersQuery){
                ordersCollection.document(doc.id).update(
                    COMMENTS, FieldValue.arrayUnion(comment)
                )
            }
        } catch(e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun deleteComment(
        context: Context,
        ordersCollection: CollectionReference,
        order: Order,
        comment: String
    ) = CoroutineScope(Dispatchers.IO).launch{
        try{
            val ordersQuery = ordersCollection
                .whereEqualTo(USERID, Firebase.getUserId())
                .whereEqualTo(EVENTID, order.eventID)
                .get()
                .await()
            for(doc in ordersQuery){
                ordersCollection.document(doc.id).update(
                    COMMENTS, FieldValue.arrayRemove(comment)
                )
            }
        } catch(e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun getAllOrdersFromEvent(
        context: Context,
        ordersCollection: CollectionReference,
        eventID: String     //in Firebase Object this will need to be a event.documentID.id or something like that
    ){
        ordersCollection
            .whereEqualTo(EVENTID, eventID)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                val drinkList: MutableList<Drink> = mutableListOf()

                querySnapshot?.let{
                    CoroutineScope(Dispatchers.Default).launch {
                        for (document in it) {
                            val drinksList =
                                document.get(DRINKS) as MutableList<DocumentReference>
                            for (drinkMap in drinksList) {

                                val drinkDocument = drinkMap.get().await()

                                val drink = Drink.fromMap(
                                    drinkDocument.data as Map<String, Any>,
                                    drinkDocument.reference
                                )
                                Log.e("drink:", drink.toString())
                                drinkList.add(drink)
                            }
                            //adapter.drinksList = drinkList
                            Log.e("drinkList: ", drinkList.toString())
                        }
                        withContext(Dispatchers.Main) {
                            //adapter.drinksList = drinkList
                            //Log.e("List length", adapter.drinksList.toString())
                            //adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
    }
}