package com.example.raiseyourglass.firebase

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object LoginComponent {

    val auth = FirebaseAuth.getInstance()

    fun logout(){
        auth.signOut()
    }

    //can throw multiple exceptions, catch this in login activity,
    //so it can display Toast or something...
    fun loginWithEmailAndPassword(
        email: String,
        password: String,
        context: Context,
        auth: FirebaseAuth
    ){
        CoroutineScope(Dispatchers.IO).launch{
            try {
                auth.signInWithEmailAndPassword(email, password).await()
            } catch(e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}