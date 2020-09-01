package com.example.raiseyourglass.firebase

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Firebase {

    private lateinit var context: Context
    private val auth = FirebaseAuth.getInstance()

    private val drinksCollectionRef = Firebase.firestore.collection("drinks")
    private val favoritesCollectionRef = Firebase.firestore.collection("favorites")
    private val eventsCollectionRef = Firebase.firestore.collection("events")

    fun setContext(context: Context){
        this.context = context
    }

    /**LOGIN SECTION*/
    fun loginWithEmailAndPassword(email: String, password: String){
        LoginComponent.loginWithEmailAndPassword(email, password, context, auth)
    }

    fun logout(){
        LoginComponent.logout()
    }


    /**REGISTER SECTION*/
    fun registerWithEmailAndPassword(email: String, password: String): Boolean{
        if(Validator.areValid(email, password)) {
            RegisterComponent.registerUserWithEmailAndPassword(
                email,
                password,
                context,
                auth,
                favoritesCollectionRef
            )
            return true
        }
        else{
            Toast.makeText(context, "Your email or password isn't valid!", Toast.LENGTH_SHORT).show()
            return false
        }
    }


    /**PROFILE SECTION*/
    fun updateUserName(userName: String){
        UpdateUserComponent.updateUserName(userName, context, auth)
    }

    fun isUserLogged(): Boolean{
        val auth = LoginComponent.auth
        return auth.currentUser != null
    }

}