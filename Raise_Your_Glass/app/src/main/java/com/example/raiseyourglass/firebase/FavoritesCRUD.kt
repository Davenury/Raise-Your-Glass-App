package com.example.raiseyourglass.firebase

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.raiseyourglass.dataclasses.Drink
import com.google.firebase.firestore.*
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
                    FAVORITES, FieldValue.arrayUnion(drink.documentID)
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
                    FAVORITES, FieldValue.arrayRemove(drink.documentID)
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
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                querySnapshot?.let {

                    for(doc in it) {
                        val referenceList = doc.get(FAVORITES) as List<DocumentReference>
                        if (!referenceList.contains(drink.documentID)) {
                            Log.d("Kurwa", "delete")
                            imageView.setImageDrawable(border)
                        } else {
                            Log.d("Kurwa", "add")
                            imageView.setImageDrawable(fullHeart)
                        }
                    }
                }
            }
    }
}