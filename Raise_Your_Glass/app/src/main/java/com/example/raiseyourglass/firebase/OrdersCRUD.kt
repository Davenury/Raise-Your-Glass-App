package com.example.raiseyourglass.firebase

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.raiseyourglass.adapters.OrderDrinksAdapter
import com.example.raiseyourglass.adapters.OwnerOrdersDetailsAdapter
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
    private const val COMMENT = "comment"

    fun addOrder(
        context: Context,
        ordersCollection: CollectionReference,
        order: Order
    ) = CoroutineScope(Dispatchers.IO).launch{
        try{
            Log.d("Orders", "In add Order Function")
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

    fun deleteOrder(
        context: Context,
        ordersCollection: CollectionReference,
        order: Order
    )=  CoroutineScope(Dispatchers.IO).launch{
        try{
            Log.d("Orders", "In delete Order Function")
            val col = ordersCollection
                .whereEqualTo(EVENTID, order.eventID)
                .whereEqualTo(USERID, order.userID)
                .get()
                .await()
            for(doc in col){
                ordersCollection.document(doc.id).delete().await()
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Successfully added your order!", Toast.LENGTH_SHORT).show()
            }
        } catch(e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun deleteAllOrdersFromEvent(
        context: Context,
        ordersCollection: CollectionReference,
        eventID: String
    ) = CoroutineScope(Dispatchers.IO).launch{
        val ordersSnap = ordersCollection
            .whereEqualTo(EVENTID, eventID)
            .get()
            .await()
        for (order in ordersSnap){
            try{
                ordersCollection.document(order.id).delete().await()
            } catch(e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
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
                    COMMENT, FieldValue.arrayUnion(comment)
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
                    COMMENT, FieldValue.arrayRemove(comment)
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
        eventID: String
    ){
        ordersCollection
            .whereEqualTo(EVENTID, eventID)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                querySnapshot?.let{
                    //here just list of orders
                }
            }
    }

    fun getAllDrinksFromOrder(
        context: Context,
        ordersCollection: CollectionReference,
        eventID: String,
        userID: String,
        adapter: Any
    ){
        ordersCollection
            .whereEqualTo(EVENTID, eventID)
            .whereEqualTo(USERID, userID)
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
                                drinkList.add(drink)
                            }
                            if(adapter is OrderDrinksAdapter)
                                adapter.drinksOrdersAlreadyMade = drinkList
                            Log.e("drinkList: ", drinkList.toString())
                        }
                        withContext(Dispatchers.Main) {
                            if(adapter is OrderDrinksAdapter) {
                                adapter.drinksOrdersAlreadyMade = drinkList
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
    }

    fun setCommentFromOrderToTextView(
        context: Context,
        ordersCollection: CollectionReference,
        eventID: String,
        userID: String,
        textView: EditText
    ){
        ordersCollection
            .whereEqualTo(EVENTID, eventID)
            .whereEqualTo(USERID, userID)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                querySnapshot?.let {
                    for(orderDoc in it){
                        val comment = orderDoc.get(COMMENT) as String
                        textView.setText(comment)
                    }
                }
            }
    }

    fun setAllDrinksFromEventToPairs(
        context: Context,
        ordersCollection: CollectionReference,
        eventID: String,
        adapter: Any
    ){
        ordersCollection
            .whereEqualTo(EVENTID, eventID)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                var documentReferences: MutableList<DocumentReference>
                val drinks = mutableListOf<Drink>()
                val pairsList = mutableListOf<Pair<Drink, Int>>()
                if(adapter is OwnerOrdersDetailsAdapter){
                    querySnapshot?.let{
                        CoroutineScope(Dispatchers.IO).launch{
                            for(document in it){
                                documentReferences = document.get(DRINKS) as MutableList<DocumentReference>
                                for (drinkMap in documentReferences) {

                                    val drinkDocument = drinkMap.get().await()

                                    val drink = Drink.fromMap(
                                        drinkDocument.data as Map<String, Any>,
                                        drinkDocument.reference
                                    )
                                    drinks.add(drink)
                                }
                            }
                            for(drink in drinks) {
                                if(pairsList.count { elem -> elem.first.name == drink.name } == 0)
                                    pairsList.add(Pair(
                                        drink,
                                        drinks.count { elem -> elem.name == drink.name }
                                    ))
                            }
                            withContext(Dispatchers.Main) {
                                adapter.pairList = pairsList
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
    }
}