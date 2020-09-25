package com.example.raiseyourglass.firebase

import android.content.Context
import android.provider.Settings.Global.getString
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.raiseyourglass.R
import com.example.raiseyourglass.activities.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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