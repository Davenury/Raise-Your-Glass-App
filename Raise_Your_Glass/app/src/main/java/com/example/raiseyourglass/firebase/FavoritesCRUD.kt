package com.example.raiseyourglass.firebase

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.raiseyourglass.dataclasses.Drink
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object FavoritesCRUD {

    private const val USERID = "userID"
    private const val FAVORITES = "favorites"

    fun addDrinkToFavorites(
        userID: String,
        drink: Drink,
        context: Context,
        favoritesColRef: CollectionReference
    ) = CoroutineScope(Dispatchers.IO).launch{
        val favoritesQuery = favoritesColRef
            .whereEqualTo(USERID, userID)
            .get()
            .await()
        for(doc in favoritesQuery){
            try {
                favoritesColRef.document(doc.id).update(
                    FAVORITES, FieldValue.arrayUnion(drink.toMap())
                )
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun deleteDrinkFromFavorites(
        userID: String,
        drink: Drink,
        context: Context,
        favoritesColRef: CollectionReference
    ) = CoroutineScope(Dispatchers.IO).launch{
        val favoritesQuery = favoritesColRef
            .whereEqualTo(USERID, userID)
            .get()
            .await()
        for(doc in favoritesQuery){
            try {
                favoritesColRef.document(doc.id).update(
                    FAVORITES, FieldValue.arrayRemove(drink.toMap())
                )
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun setFavoriteHeart(
        userID: String,
        drink: Drink,
        context: Context,
        favoritesColRef: CollectionReference,
        imageView: ImageView,
        border: Drawable,
        fullHeart: Drawable
    ) = CoroutineScope(Dispatchers.IO).launch{
        favoritesColRef
            .whereEqualTo(USERID, userID)
            .whereArrayContains(FAVORITES, drink.toMap())
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                Log.d("Kurwa", querySnapshot.toString())
                Log.d("Kurwa", drink.toString())
                if(querySnapshot?.isEmpty!!){
                    Log.d("Kurwa", "delete")
                    imageView.setImageDrawable(border)
                }
                else{
                    Log.d("Kurwa", "add")
                    imageView.setImageDrawable(fullHeart)
                }
            }
    }
}