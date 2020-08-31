package com.example.raiseyourglass.firebase

import android.content.Context
import android.widget.Toast
import com.example.raiseyourglass.activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

object RegisterComponent {

    val auth = FirebaseAuth.getInstance()

    /**
     * Can throw multiple exceptions, catch in LoginActivity, so
     * it can be displayed as Toast or something*/
    fun registerUserWithEmailAndPassword(email: String, password: String){
        CoroutineScope(Dispatchers.IO).launch{
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                withContext(Dispatchers.Main){
                    Toast.makeText(LoginActivity.getApplicationContext(), "Successful registration!", Toast.LENGTH_LONG).show()
                }
            }
            catch (e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(LoginActivity.getApplicationContext(), e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}