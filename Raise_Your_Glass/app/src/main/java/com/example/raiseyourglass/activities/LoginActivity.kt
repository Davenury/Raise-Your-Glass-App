package com.example.raiseyourglass.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.centerCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.example.raiseyourglass.R
import com.example.raiseyourglass.firebase.Firebase
import com.example.raiseyourglass.firebase.LoginComponent
import com.example.raiseyourglass.other_useful_things.ImageBlurer
import com.example.raiseyourglass.other_useful_things.ThemeChanger
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

const val REQUEST_CODE_SIGN_IN = 0

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPref = getSharedPreferences("darkMode", Context.MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("isDarkMode", false)

        ThemeChanger.changeThemeByBoolean(isDarkMode)
        setListeners()
    }

    override fun onStart() {
        super.onStart()
        Firebase.setContext(this)
        if(Firebase.isUserLogged()){
            Intent(this, StartActivity::class.java).apply{
                startActivity(this)
            }
        }
        ImageBlurer.setImageToBlur(ivLoginBackground, R.drawable.drinks_background, this)
    }

    private fun setListeners() {
        setRegistrationListener()
        setPasswordToggleListener()
        setLoginListener()
        setGoogleListener()
    }

    private fun setRegistrationListener(){
        btnRegisterFromLogin.setOnClickListener {
            Intent(this, RegisterActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun setPasswordToggleListener(){
        val text = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
        val password = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        ivLoginPasswordToggle.setOnClickListener {
            if (etLoginPassword.inputType == password) {
                etLoginPassword.inputType = text
                etLoginPassword.setSelection(etLoginPassword.text.length)
            } else {
                etLoginPassword.inputType = password
                etLoginPassword.setSelection(etLoginPassword.text.length)
            }
        }
    }

    private fun setLoginListener(){
        btnLogin.setOnClickListener {
            val email = etLoginEmail.text.toString()
            val password = etLoginPassword.text.toString()
            if(Firebase.loginWithEmailAndPassword(email, password) && Firebase.isUserLogged()){
                Toast.makeText(applicationContext, "Successful login!", Toast.LENGTH_SHORT).show()
                Intent(this, StartActivity::class.java).apply{
                    startActivity(this)
                }
            }
        }
    }

    /**Google authentication*/

    private fun setGoogleListener(){
        ivGoogleButton.setOnClickListener {
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.webclient_id))
                .requestEmail()
                .build()
            val signInClient = GoogleSignIn.getClient(this, options)
            signInClient.signInIntent.also {
                startActivityForResult(it, REQUEST_CODE_SIGN_IN)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_SIGN_IN) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
                googleAuthForFirebase(it)
            }
        }
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount){
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Firebase.auth.signInWithCredential(credentials).await()
                Firebase.prepareUserToLife()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Successfully logged in", Toast.LENGTH_LONG).show()
                }
            } catch(e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}