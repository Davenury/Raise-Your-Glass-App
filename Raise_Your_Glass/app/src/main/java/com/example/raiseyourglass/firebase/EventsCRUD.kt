package com.example.raiseyourglass.firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.raiseyourglass.adapters.AllAvailableEventsAdapter
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.dataclasses.Event
import com.example.raiseyourglass.dataclasses.Order
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.sql.Timestamp
import java.util.*
import java.util.function.ToDoubleBiFunction

object EventsCRUD {

    fun addEvent(
        event: Event,
        context: Context,
        eventsRef: CollectionReference
    ) = CoroutineScope(Dispatchers.IO).launch{
        try{
            eventsRef.add(event)
            withContext(Dispatchers.Main){
                Toast.makeText(context, "Successfully added an event!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun updateEvent(
        oldEvent: Event,
        newEventMap: Map<String, Any>,
        context: Context,
        eventsRef: CollectionReference
    ) = CoroutineScope(Dispatchers.IO).launch{
        val eventQuery = eventsRef
            .whereEqualTo("place", oldEvent.place)
            .whereEqualTo("ownerID", oldEvent.ownerID)
            .whereEqualTo("isPrivate", oldEvent.isPrivate)
            .whereEqualTo("date", oldEvent.date)
            .get()
            .await()
        for(event in eventQuery) {
            try {
                eventsRef.document(event.id).set(
                    newEventMap,
                    SetOptions.merge()
                ).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Successfully edited this event!", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun deleteEvent(
        event: Event,
        context: Context,
        eventsRef: CollectionReference
    ) = CoroutineScope(Dispatchers.IO).launch {
        val eventQuery = eventsRef
            .whereEqualTo("place", event.place)
            .whereEqualTo("ownerID", event.ownerID)
            .whereEqualTo("isPrivate", event.isPrivate)
            .whereEqualTo("date", event.date)
            .get()
            .await()
        for (eventDoc in eventQuery) {
            try {
                eventsRef.document(eventDoc.id).delete().await()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun allEventsSnapshotListener(
        context: Context,
        eventsRef: CollectionReference,
        userUID: String?,
        adapter: AllAvailableEventsAdapter
    ){
        val events = adapter.eventsList
        /**if there are any public events*/
        eventsRef
            .whereEqualTo("private", false)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                querySnapshot?.let {
                    for (document in it) {
                        val event = makeEventOutOfDocument(document)
                        events.add(event)
                    }
                    adapter.eventsList = events
                    adapter.notifyDataSetChanged()
                }
            }
        /**if user is invited to any events*/
        Log.d("Kurwa", userUID!!)
        if(userUID != null) {
            eventsRef
                .whereArrayContains("invited", userUID)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    firebaseFirestoreException?.let {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                        return@addSnapshotListener
                    }

                    querySnapshot?.let {
                        for (document in it) {
                            val event = makeEventOutOfDocument(document)
                            Log.d("Kurwa", event.toString())
                            events.add(event)
                        }
                        adapter.eventsList = events
                        adapter.notifyDataSetChanged()
                    }
                }
            /**if user is an owner of event*/
            eventsRef
                .whereEqualTo("ownerID", userUID)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    firebaseFirestoreException?.let {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                        return@addSnapshotListener
                    }

                    querySnapshot?.let {
                        for (document in it) {
                            val event = makeEventOutOfDocument(document)
                            Log.d("Kurwa", event.toString())
                            events.add(event)
                        }
                        adapter.eventsList = events
                        adapter.notifyDataSetChanged()
                    }
                }
        }
    }

    private fun makeEventOutOfDocument(document: QueryDocumentSnapshot): Event {
        val timestamp = document["date"] as com.google.firebase.Timestamp
        val date = timestamp.toDate()
        val ordersMap = document.data["orders"] as MutableList<HashMap<String, Any>>
        val orders = ordersMap.map{elem ->
            Order(
                elem["userID"] as String,
                elem["orders"] as MutableList<Drink>,   //zakład, że jebnie? - jak trzeba będzie pierdolić się z kolejną mapą, to ja pierdolnę...
                elem["comments"] as List<String>
            )
        }
        return Event(
            date,
            document["place"] as String,
            document["private"] as Boolean,
            document["ownerID"] as String,
            orders,
            document["participants"] as List<String>,
            document["invited"] as List<String>
        )
    }

    fun changeEventType(
        event: Event,
        context: Context,
        eventsRef: CollectionReference,
        toBePrivate: Boolean
    ) = CoroutineScope(Dispatchers.IO).launch {
        val eventQuery = eventsRef
            .whereEqualTo("place", event.place)
            .whereEqualTo("ownerID", event.ownerID)
            .whereEqualTo("isPrivate", event.isPrivate)
            .whereEqualTo("date", event.date)
            .get()
            .await()
        for(eventDoc in eventQuery) {
            try {
                Firebase.firestore.runBatch { batch ->
                    val eventRef = eventsRef.document(eventDoc.id)
                    batch.update(eventRef, "isPrivate", toBePrivate)
                }.await()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**Adds user to invited list*/
    fun inviteUser(
        event: Event,
        context: Context,
        eventsRef: CollectionReference,
        userID: String
    ) = CoroutineScope(Dispatchers.IO).launch {
        val eventQuery = eventsRef
            .whereEqualTo("place", event.place)
            .whereEqualTo("ownerID", event.ownerID)
            .whereEqualTo("isPrivate", event.isPrivate)
            .whereEqualTo("date", event.date)
            .get()
            .await()
        for(eventDoc in eventQuery) {
            try {
                eventsRef.document(eventDoc.id).update(
                    "invited", FieldValue.arrayUnion(userID)
                )
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**It only adds user to participants list, not deleting him from invited*/
    fun thisUserWillParticipate(
        event: Event,
        context: Context,
        eventsRef: CollectionReference,
        userID: String
    ) = CoroutineScope(Dispatchers.IO).launch {
        val eventQuery = eventsRef
            .whereEqualTo("place", event.place)
            .whereEqualTo("ownerID", event.ownerID)
            .whereEqualTo("isPrivate", event.isPrivate)
            .whereEqualTo("date", event.date)
            .whereArrayContains("invited", userID)
            .get()
            .await()
        for(eventDoc in eventQuery) {
            try {
                eventsRef.document(eventDoc.id).update(
                    "participants", FieldValue.arrayUnion(userID)
                )
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**Delete User is to delete him from invited array*/
    fun uninviteUser(
        event: Event,
        context: Context,
        eventsRef: CollectionReference,
        userID: String
    )= CoroutineScope(Dispatchers.IO).launch {
        val eventQuery = eventsRef
            .whereEqualTo("place", event.place)
            .whereEqualTo("ownerID", event.ownerID)
            .whereEqualTo("isPrivate", event.isPrivate)
            .whereEqualTo("date", event.date)
            .get()
            .await()
        for(eventDoc in eventQuery) {
            try {
                eventsRef.document(eventDoc.id).update(
                    "invited", FieldValue.arrayRemove(userID)
                )
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}