package com.example.raiseyourglass.firebase

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.raiseyourglass.adapters.*
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.dataclasses.Event
import com.example.raiseyourglass.dataclasses.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Firebase {

    private lateinit var context: Context
    private val auth = FirebaseAuth.getInstance()


    private val drinksCollectionRef = Firebase.firestore.collection("drinks")
    val favoritesCollectionRef = Firebase.firestore.collection("favorites")
    private val eventsCollectionRef = Firebase.firestore.collection("events")
    private val userCollectionRef = Firebase.firestore.collection("customUsers")
    private val ordersCollectionRef = Firebase.firestore.collection("orders")

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


    fun getInvitedUsersData(adapter: EventParticipantsListAdapter) {
        UsersComponent.getInvitedUserData(context, userCollectionRef, adapter)
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
        UpdateUserComponent.updateUserName(userName, context, auth, userCollectionRef)
    }

    fun isUserLogged(): Boolean{
        val auth = LoginComponent.auth
        return auth.currentUser != null
    }



    /**DRINKS CRUD SECTION*/
    fun addDrink(drink: Drink){
        if(Validator.addDrinkValidator(drink))
            DrinkCRUD.addDrink(drink, context, drinksCollectionRef)
        else{
            Toast.makeText(context, "Drink wasn't added! Check for Drink name and Type!", Toast.LENGTH_LONG).show()
        }
    }

    fun updateDrink(drink: Drink, newDrinkMap: Map<String, Any>){
        DrinkCRUD.updateDrink(drink, newDrinkMap, context, drinksCollectionRef)
    }

    fun deleteDrink(drink: Drink){
        DrinkCRUD.deleteDrink(drink, context, drinksCollectionRef)
    }

    fun subscribeToDrinkSnapshotListener(adapter: Any, userFilter:String?){
        DrinkCRUD.subscribeToDrinkSnapshotListener(context, drinksCollectionRef, adapter, userFilter)
    }

    fun setDrinkOwner(owner: String, view: TextView) {
        UsersComponent.getUserByUID(owner, context, userCollectionRef, view)
    }

    /**Favorites CRUD Section*/
    fun subscribeToFavoriteDrinkSnapshotListener(adapter: DrinksListAdapter){
        DrinkCRUD.subscribeToFavoriteDrinkSnapshotListener(context, adapter, this.getUserId())
    }

    fun addDrinkToFavorites(drink: Drink){
        FavoritesCRUD.addDrinkToFavorites(this.getUserId()!!, drink, context, favoritesCollectionRef)
    }

    fun deleteDrinkFromFavorites(drink: Drink){
        FavoritesCRUD.deleteDrinkFromFavorites(this.getUserId()!!, drink, context, favoritesCollectionRef)
    }

    fun setFavoriteHeart(drink: Drink, imageView: ImageView, border: Drawable, fullHeart: Drawable){
        FavoritesCRUD.setFavoriteHeart(this.getUserId()!!, drink, context, favoritesCollectionRef, imageView, border, fullHeart)
    }


    /**Events CRUD Section*/
    fun addEvent(event: Event){
        if(Validator.addEventValidator(event))
            EventsCRUD.addEvent(event, context, eventsCollectionRef)
        else{
            Toast.makeText(context, "Event wasn't added! Check for Place of Event and its Date (click on calendar icon to choose date)", Toast.LENGTH_LONG).show()
        }
    }

    fun updateEvent(event: Event, newEventMap: Map<String, Any>){
        EventsCRUD.updateEvent(event, newEventMap, context, eventsCollectionRef)
    }

    fun deleteEvent(event: Event){
        this.deleteAllOrdersFromEvent(event)
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

    /**Orders Section*/
    fun addOrder(order: Order){
        OrdersCRUD.addOrder(context, ordersCollectionRef, order)
    }

    fun deleteOrder(order: Order){
        OrdersCRUD.deleteOrder(context, ordersCollectionRef, order)
    }

    fun addDrinkToOrder(order: Order, drink: Drink){
        OrdersCRUD.addDrink(context, ordersCollectionRef, order, drink)
    }

    fun deleteDrinkFromOrder(order: Order, drink: Drink){
        OrdersCRUD.deleteDrink(context, ordersCollectionRef, order, drink)
    }

    fun addCommentToOrder(order: Order, comment: String){
        OrdersCRUD.addComment(context, ordersCollectionRef, order, comment)
    }

    fun deleteCommentFromOrder(order: Order, comment: String){
        OrdersCRUD.deleteComment(context, ordersCollectionRef, order, comment)
    }

    fun getAllOrdersFromEvent(event: Event){
        OrdersCRUD.getAllOrdersFromEvent(context, ordersCollectionRef, event.documentID!!.id)
    }

    fun getAllDrinksFromOrder(event: Event, adapter: Any){
        OrdersCRUD.getAllDrinksFromOrder(context, ordersCollectionRef, event.documentID!!.id, this.getUserId()!!, adapter)
    }

    fun setCommentFromOrderToTextView(event: Event, textView: EditText){
        OrdersCRUD.setCommentFromOrderToTextView(context, ordersCollectionRef, event.documentID!!.id, this.getUserId()!!, textView)
    }

    fun deleteAllOrdersFromEvent(event: Event){
        OrdersCRUD.deleteAllOrdersFromEvent(context, ordersCollectionRef, event.documentID!!.id)
    }

    fun setAllDrinksFromEventToPairs(event: Event, adapter: Any){
        OrdersCRUD.setAllDrinksFromEventToPairs(context, ordersCollectionRef, event.documentID!!.id, adapter)
    }

    /**Drinks Images Section*/
    fun setImageToView(filename: String, imageView: ImageView){
        DrinksImages.setImageToView(filename, context, imageView)
    }

    fun uploadImageToStorage(filename: String, currentFile: Uri){
        DrinksImages.uploadImageToStorage(filename, currentFile, context)
    }

    fun deleteImageFromStorage(filename: String){
        DrinksImages.deleteImageFromStorage(filename, context)
    }
}