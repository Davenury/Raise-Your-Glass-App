package com.example.raiseyourglass.firebase

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.example.raiseyourglass.dataclasses.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object UsersComponent {

    fun getUserByUID(
        uid: String,
        context: Context,
        collectionRef: CollectionReference,
        view: TextView
    ) = CoroutineScope(Dispatchers.Default).launch{
        collectionRef
            .whereEqualTo("userID", uid)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let{
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                querySnapshot?.let{
                    var user = User("", uid, "Without data")
                    for (document in it){
                        user = document.toObject()
                    }
                    if(user.name == "") view.text = user.email
                    else view.text = user.name
                }
            }
    }
}