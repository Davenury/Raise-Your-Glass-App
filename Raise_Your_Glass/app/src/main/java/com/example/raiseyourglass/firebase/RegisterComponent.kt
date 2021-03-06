package com.example.raiseyourglass.firebase

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.raiseyourglass.activities.StartActivity
import com.example.raiseyourglass.dataclasses.Favorites
import com.example.raiseyourglass.dataclasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

object RegisterComponent {

    private const val EMAIL = "email"

    fun registerUserWithEmailAndPassword(
        email: String,
        password: String,
        context: Context,
        auth: FirebaseAuth,
        favoritesCollectionRef: CollectionReference,
        userCollectionRef: CollectionReference
    )=
        CoroutineScope(Dispatchers.IO).launch{
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                prepareUserToLife(context, auth, favoritesCollectionRef, userCollectionRef)
            }
            catch (e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    fun prepareUserToLife(
        context: Context,
        auth: FirebaseAuth,
        favoritesCollectionRef: CollectionReference,
        userCollectionRef: CollectionReference
    ) = CoroutineScope(Dispatchers.IO).launch{
        auth.currentUser?.let {
            val userQuery = userCollectionRef
                .whereEqualTo(EMAIL, it.email)
                .get()
                .await()
            if (userQuery.isEmpty) {
                val favorites = Favorites(
                    it.uid,
                    mutableListOf()
                )
                try {
                    favoritesCollectionRef.add(favorites).await()
                    if (it.email != null) {
                        val user = User(
                            it.displayName ?: "",
                            it.uid,
                            it.email.toString()
                        )
                        userCollectionRef.add(user)
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Successful registration!", Toast.LENGTH_LONG)
                            .show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
            withContext(Dispatchers.Main){
                Intent(context, StartActivity::class.java).apply {
                    this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(this)
                }
            }
        }
    }
}