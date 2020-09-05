package com.example.raiseyourglass.firebase

import android.content.Context
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.raiseyourglass.adapters.DrinksListAdapter
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.dataclasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Firebase {

    private lateinit var context: Context
    private val auth = FirebaseAuth.getInstance()

    fun getUserId() = auth.currentUser?.uid
    fun getUserName() = auth.currentUser?.displayName

    private val drinksCollectionRef = Firebase.firestore.collection("drinks")
    private val favoritesCollectionRef = Firebase.firestore.collection("favorites")
    private val eventsCollectionRef = Firebase.firestore.collection("events")
    private val userCollectionRef = Firebase.firestore.collection("customUsers")

    fun setContext(context: Context){
        this.context = context
    }

    fun getContext(): Context{
        return this.context
    }

    /**LOGIN SECTION*/
    fun loginWithEmailAndPassword(email: String, password: String): Boolean {
        return if(Validator.areValid(email, password)){
            LoginComponent.loginWithEmailAndPassword(
                email,
                password,
                context,
                auth
            )
            true
        } else{
            Toast.makeText(context, "Your email or password isn't valid!", Toast.LENGTH_SHORT).show()
            false
        }
    }

    fun logout(){
        LoginComponent.logout()
    }


    /**REGISTER SECTION*/
    fun registerWithEmailAndPassword(email: String, password: String){
        if(Validator.areValid(email, password)) {
            RegisterComponent.registerUserWithEmailAndPassword(
                email,
                password,
                context,
                auth,
                favoritesCollectionRef,
                userCollectionRef)
        }
        else{
            Toast.makeText(context, "Your email or password isn't valid!", Toast.LENGTH_SHORT).show()
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



    /**DRINKS CRUD SECTION*/
    fun addDrink(drink: Drink){
        DrinkCRUD.addDrink(drink, context, drinksCollectionRef)
    }

    fun updateDrink(drink: Drink, newDrinkMap: Map<String, Any>){
        DrinkCRUD.updateDrink(drink, newDrinkMap, context, drinksCollectionRef)
    }

    fun deleteDrink(drink: Drink){
        DrinkCRUD.deleteDrink(drink, context, drinksCollectionRef)
    }

    fun subscribeToDrinkSnapshotListener(adapter: DrinksListAdapter, userFilter:String?){
        DrinkCRUD.subscribeToDrinkSnapshotListener(context, drinksCollectionRef, adapter, userFilter)
    }

    fun setOwner(owner: String, view: TextView) {
        Log.d("Kurwa", "get Owner")
        Log.d("Kurwa", owner)
        var currentUser = User("", owner, "This User didn't pass his data")
        UsersComponent.getUserByUID(owner, context, userCollectionRef, view)
    }

}