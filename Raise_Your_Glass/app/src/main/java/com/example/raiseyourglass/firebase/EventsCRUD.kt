package com.example.raiseyourglass.firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.raiseyourglass.adapters.AllAvailableEventsAdapter
import com.example.raiseyourglass.adapters.MyEventsAdapter
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

    private const val PLACE = "place"
    private const val OWNERID = "ownerID"
    private const val ISPRIVATE = "private"
    private const val DATE = "date"
    private const val INVITED = "invited"
    private const val ORDERS = "orders"
    private const val USERID = "userID"
    private const val PARTICIPANTS = "participants"
    private const val COMMENTS = "comments"

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
            .whereEqualTo(PLACE, oldEvent.place)
            .whereEqualTo(OWNERID, oldEvent.ownerID)
            .whereEqualTo(ISPRIVATE, oldEvent.isPrivate)
            .whereEqualTo(DATE, oldEvent.date)
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
            .whereEqualTo(PLACE, event.place)
            .whereEqualTo(OWNERID, event.ownerID)
            .whereEqualTo(ISPRIVATE, event.isPrivate)
            .whereEqualTo(DATE, event.date)
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

    fun allMyEventsSnapshotListener(
        context: Context,
        eventsRef: CollectionReference,
        userUID: String?,
        adapter: MyEventsAdapter
    ){
        val events = adapter.myEvents
        eventsRef
            .whereEqualTo(OWNERID, userUID)
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
                    adapter.myEvents = events
                    adapter.notifyDataSetChanged()
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
            .whereEqualTo(ISPRIVATE, false)
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
        if(userUID != null) {
            eventsRef
                .whereArrayContains(INVITED, userUID)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    firebaseFirestoreException?.let {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                        return@addSnapshotListener
                    }

                    querySnapshot?.let {
                        for (document in it) {
                            val event = makeEventOutOfDocument(document)
                            if(!events.contains(event)) events.add(event)
                        }
                        adapter.eventsList = events
                        adapter.notifyDataSetChanged()
                    }
                }
            /**if user is an owner of event*/
            eventsRef
                .whereEqualTo(OWNERID, userUID)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    firebaseFirestoreException?.let {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                        return@addSnapshotListener
                    }

                    querySnapshot?.let {
                        for (document in it) {
                            val event = makeEventOutOfDocument(document)
                            if(!events.contains(event)) events.add(event)
                        }
                        adapter.eventsList = events
                        adapter.notifyDataSetChanged()
                    }
                }
        }
    }

    private fun makeEventOutOfDocument(document: QueryDocumentSnapshot): Event {
        val timestamp = document[DATE] as com.google.firebase.Timestamp
        val date = timestamp.toDate()
        val ordersMap = document.data[ORDERS] as MutableList<HashMap<String, Any>>
        val orders = ordersMap.map{elem ->
            Order(
                elem[USERID] as String,
                elem[ORDERS] as MutableList<Drink>,   //zakład, że jebnie? - jak trzeba będzie pierdolić się z kolejną mapą, to ja pierdolnę...
                elem[COMMENTS] as List<String>
            )
        }.toMutableList()
        return Event(
            date,
            document[PLACE] as String,
            document[ISPRIVATE] as Boolean,
            document[OWNERID] as String,
            orders,
            document[PARTICIPANTS] as MutableList<String>,
            document[INVITED] as MutableList<String>
        )
    }

    fun changeEventType(
        event: Event,
        context: Context,
        eventsRef: CollectionReference,
        toBePrivate: Boolean
    ) = CoroutineScope(Dispatchers.IO).launch {
        val eventQuery = eventsRef
            .whereEqualTo(PLACE, event.place)
            .whereEqualTo(OWNERID, event.ownerID)
            .whereEqualTo(ISPRIVATE, event.isPrivate)
            .whereEqualTo(DATE, event.date)
            .get()
            .await()
        for(eventDoc in eventQuery) {
            try {
                Firebase.firestore.runBatch { batch ->
                    val eventRef = eventsRef.document(eventDoc.id)
                    batch.update(eventRef, ISPRIVATE, toBePrivate)
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
            .whereEqualTo(PLACE, event.place)
            .whereEqualTo(OWNERID, event.ownerID)
            .whereEqualTo(ISPRIVATE, event.isPrivate)
            .whereEqualTo(DATE, event.date)
            .get()
            .await()
        for(eventDoc in eventQuery) {
            try {
                eventsRef.document(eventDoc.id).update(
                    INVITED, FieldValue.arrayUnion(userID)
                )
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**It only adds user to participants list, not deleting him from invited*/
    fun changeUserParticipation(
        event: Event,
        context: Context,
        eventsRef: CollectionReference,
        userID: String
    ) = CoroutineScope(Dispatchers.IO).launch {
        val eventQuery = eventsRef
            .whereEqualTo(PLACE, event.place)
            .whereEqualTo(OWNERID, event.ownerID)
            .whereEqualTo(ISPRIVATE, event.isPrivate)
            .whereEqualTo(DATE, event.date)
            .whereArrayContains(INVITED, userID)
            .get()
            .await()
        Log.e("Participation",eventQuery.size().toString())
        for(eventDoc in eventQuery) {
            try {
                Log.e("Participation","$userID  ${event.participants.contains(userID)}")
                if(!event.participants.contains(userID)){
                    eventsRef.document(eventDoc.id).update(PARTICIPANTS, FieldValue.arrayUnion(userID))
                }else{
                    eventsRef.document(eventDoc.id).update(PARTICIPANTS,FieldValue.arrayRemove(userID))
                }
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
            .whereEqualTo(PLACE, event.place)
            .whereEqualTo(OWNERID, event.ownerID)
            .whereEqualTo(ISPRIVATE, event.isPrivate)
            .whereEqualTo(DATE, event.date)
            .get()
            .await()
        for(eventDoc in eventQuery) {
            try {
                eventsRef.document(eventDoc.id).update(
                    INVITED, FieldValue.arrayRemove(userID)
                )
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}