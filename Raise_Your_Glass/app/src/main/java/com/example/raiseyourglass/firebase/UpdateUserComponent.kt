package com.example.raiseyourglass.firebase

import android.content.Context
import android.widget.Toast
import com.example.raiseyourglass.activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

object UpdateUserComponent {

    fun updateUserName(userName: String, context: Context, auth: FirebaseAuth){
        CoroutineScope(Dispatchers.IO).launch{
            try {
                auth.currentUser?.let {
                    it.updateProfile(
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(userName)
                            .build()
                    ).await()
                }
            } catch(e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}