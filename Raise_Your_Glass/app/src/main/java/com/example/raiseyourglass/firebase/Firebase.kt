package com.example.raiseyourglass.firebase

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.example.raiseyourglass.adapters.AllAvailableEventsAdapter
import com.example.raiseyourglass.adapters.DrinksListAdapter
import com.example.raiseyourglass.adapters.InviteUsersAdapter
import com.example.raiseyourglass.adapters.MyEventsAdapter
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.dataclasses.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Firebase {

    private lateinit var context: Context
    private val auth = FirebaseAuth.getInstance()


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

    /**User Section*/

    fun getUserId() = auth.currentUser?.uid
    fun getUserName() = auth.currentUser?.displayName

    fun logout(){
        LoginComponent.logout()
    }

    fun getAllUsers(adapter: InviteUsersAdapter){
        UsersComponent.getAllUsers(context, userCollectionRef, adapter)
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

    fun setDrinkOwner(owner: String, view: TextView) {
        UsersComponent.getUserByUID(owner, context, userCollectionRef, view)
    }


    /**Events CRUD Section*/
    fun addEvent(event: Event){
        EventsCRUD.addEvent(event, context, eventsCollectionRef)
    }

    fun updateEvent(event: Event, newEventMap: Map<String, Any>){
        EventsCRUD.updateEvent(event, newEventMap, context, eventsCollectionRef)
    }

    fun deleteEvent(event: Event){
        EventsCRUD.deleteEvent(event, context, eventsCollectionRef)
    }

    fun subscribeToAllEventsListener(adapter: AllAvailableEventsAdapter){
        EventsCRUD.allEventsSnapshotListener(context, eventsCollectionRef, this.getUserId(), adapter)
    }

    fun myEventsSnapshotListener(adapter: MyEventsAdapter){
        EventsCRUD.allMyEventsSnapshotListener(context, eventsCollectionRef, this.getUserId(), adapter)
    }

    fun changeEventType(event: Event, toBePrivate: Boolean){
        EventsCRUD.changeEventType(event, context, eventsCollectionRef, toBePrivate)
    }

    fun inviteUser(event: Event, userID: String){
        EventsCRUD.inviteUser(event, context, eventsCollectionRef, userID)
    }

    fun participate(event: Event){
        EventsCRUD.changeUserParticipation(event, context, eventsCollectionRef, this.getUserId()!!)
    }

    fun uninviteUser(event: Event, userID: String){
        EventsCRUD.uninviteUser(event, context, eventsCollectionRef, userID)
    }

    fun setUserToTextView(owner: String, view: TextView) {
        UsersComponent.getUserByUID(owner, context, eventsCollectionRef, view)
    }



}