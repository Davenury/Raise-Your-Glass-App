package com.example.raiseyourglass.firebase

import android.content.Context
import android.widget.Toast
import com.example.raiseyourglass.dataclasses.Favorites
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

object RegisterComponent {

    val auth = FirebaseAuth.getInstance()

    /**
     * Can throw multiple exceptions, catch in LoginActivity, so
     * it can be displayed as Toast or something*/
    fun registerUserWithEmailAndPassword(
        email: String,
        password: String,
        context: Context,
        auth: FirebaseAuth,
        favoritesCollectionRef: CollectionReference
    ){
        CoroutineScope(Dispatchers.IO).launch{
            try {
                Firebase.firestore.runTransaction {
                    auth.createUserWithEmailAndPassword(email, password)
                    prepareUserToLife(context, auth, favoritesCollectionRef)
                }.await()
            }
            catch (e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun prepareUserToLife(
        context: Context,
        auth: FirebaseAuth,
        favoritesCollectionRef: CollectionReference
    ) = CoroutineScope(Dispatchers.IO).launch{
        auth.currentUser?.let{
            val favorites = Favorites(
                it.uid,
                mutableListOf()
            )
            try{
                favoritesCollectionRef.add(favorites).await()
                withContext(Dispatchers.Main){
                    Toast.makeText(context, "Successful registration!", Toast.LENGTH_LONG).show()
                }
            } catch(e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}