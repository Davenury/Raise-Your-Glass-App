package com.example.raiseyourglass.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.raiseyourglass.R
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.btnRegister
import kotlinx.android.synthetic.main.activity_register.etEmail
import kotlinx.android.synthetic.main.activity_register.etPassword

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        Firebase.setContext(applicationContext)
        btnRegister.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            println("$email, $password")
            Firebase.registerWithEmailAndPassword(email, password)
            checkIfLogged()
        }
    }

    private fun checkIfLogged() {
        if (Firebase.isUserLogged()) {
            Intent(this, StartActivity::class.java).also {
                startActivity(it)
            }
        } else {
//            Toast.makeText(this.applicationContext, "Registration failed", Toast.LENGTH_LONG).show()
            Log.d("register", "We are fucked up")
        }
    }
}