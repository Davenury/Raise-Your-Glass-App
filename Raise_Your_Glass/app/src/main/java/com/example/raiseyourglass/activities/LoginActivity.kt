package com.example.raiseyourglass.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.raiseyourglass.R
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Firebase.setContext(applicationContext)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            Firebase.loginWithEmailAndPassword(email, password)
            checkLogged()
        }

        btnRegister.setOnClickListener{
            Intent(this,RegisterActivity::class.java).also{
                startActivity(it)
            }
        }
    }

    private fun checkLogged(){
        if(Firebase.isUserLogged()){
            tvLoggedIn.text = "Logged in"
            Intent(this,StartActivity::class.java).also {
                startActivity(it)
            }
        }
        else{
            tvLoggedIn.text = "Not logged in"
        }
    }
}