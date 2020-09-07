package com.example.raiseyourglass.firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.raiseyourglass.activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

object UpdateUserComponent {

    private const val USERID = "userID"
    private const val NAME = "name"

    fun updateUserName(
        userName: String,
        context: Context,
        auth: FirebaseAuth,
        usersRef: CollectionReference
    ) = CoroutineScope(Dispatchers.IO).launch{
            try {
                auth.currentUser?.let {
                    it.updateProfile(
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(userName)
                            .build()
                    ).await()
                }
                val usersQuery = usersRef
                    .whereEqualTo(USERID, auth.currentUser?.uid)
                    .get()
                    .await()
                for(user in usersQuery){
                    Firebase.firestore.runBatch { batch ->
                        val userRef = usersRef.document(user.id)
                        batch.update(userRef, NAME, userName)
                    }.await()
                }
                withContext(Dispatchers.Main){
                    Toast.makeText(context, "Successfully changed Your Name", Toast.LENGTH_SHORT).show()
                }
            } catch(e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
}