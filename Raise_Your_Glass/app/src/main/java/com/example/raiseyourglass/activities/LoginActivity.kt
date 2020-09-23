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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.centerCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.example.raiseyourglass.R
import com.example.raiseyourglass.firebase.Firebase
import com.example.raiseyourglass.other_useful_things.ImageBlurer
import com.example.raiseyourglass.other_useful_things.ThemeChanger
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

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

}