package com.example.raiseyourglass.firebase
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object DrinksImages {
    private val imageRef = Firebase.storage.reference
    private val pathString = "drinksImages/"

    fun setImageToView(
        filename: String,
        context: Context,
        imageView: ImageView
    ) = CoroutineScope(Dispatchers.IO).launch {
        try{
            val maxDownloadSize = 5L * 1024 * 1024
            val bytes = imageRef.child(pathString + filename)
                .getBytes(maxDownloadSize)
                .await()
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            withContext(Dispatchers.Main){
                imageView.setImageBitmap(bitmap)
            }
        } catch(e: Exception){
            Log.d("Drinks Images", e.message!!)
        }
    }

    fun uploadImageToStorage(
        filename: String,
        currentFile: Uri?,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch{
        try{
            currentFile?.let{
                imageRef.child(pathString + filename).putFile(it).await()
            }
        } catch(e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun deleteImageFromStorage(
        filename: String,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch{
        try{
            imageRef.child(pathString + filename).delete().await()
        } catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}