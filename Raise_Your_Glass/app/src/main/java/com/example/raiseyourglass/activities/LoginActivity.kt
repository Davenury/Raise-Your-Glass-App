package com.example.raiseyourglass.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import com.example.raiseyourglass.R
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Firebase.setContext(applicationContext)

        setListeners()
    }

    override fun onStart() {
        super.onStart()
        if(Firebase.isUserLogged()){
            Intent(this, StartActivity::class.java).apply{
                startActivity(this)
            }
        }
    }

    private fun setListeners() {
        setRegistrationListener()
        setPasswordToggleListener()
        setLoginListener()
    }

    private fun setRegistrationListener(){
        tvDontHaveAccount.setOnClickListener {
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