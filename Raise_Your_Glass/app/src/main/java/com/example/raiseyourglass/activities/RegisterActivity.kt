package com.example.raiseyourglass.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.raiseyourglass.R
import com.example.raiseyourglass.firebase.Firebase
import com.example.raiseyourglass.firebase.Validator
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setListeners()
    }

    private fun setListeners() {
        btnRegister.setOnClickListener {
            val email = etRegisterEmail.text.toString()
            val password = etRegisterPassword.text.toString()
            if (Firebase.registerWithEmailAndPassword(email, password)) {
                Toast.makeText(this, "Successfully registered!", Toast.LENGTH_LONG).show()
                Intent(this, StartActivity::class.java).also{
                    startActivity(it)
                }
            }
        }

        val text = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
        val password = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        ivRegisterPasswordToggle.setOnClickListener {
            if (etRegisterPassword.inputType == password) {
                etRegisterPassword.inputType = text
                etRegisterPassword.setSelection(etRegisterPassword.text.length)
            } else {
                etRegisterPassword.inputType = password
                etRegisterPassword.setSelection(etRegisterPassword.text.length)
            }
        }

        etRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val currentPassword = etRegisterPassword.text.toString()
                if (Validator.isPasswordValid(currentPassword)) {
                    tvLengthOfPassword.text = resources.getString(R.string.six_characters_ok)
                    tvLengthOfPassword.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.good
                        )
                    )
                } else {
                    tvLengthOfPassword.text = resources.getString(R.string.six_characters_bad)
                    tvLengthOfPassword.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.bad
                        )
                    )
                }
            }
        })


        etRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val currentEmail = etRegisterEmail.text.toString()
                if (Validator.isEmailValid(currentEmail)) {
                    tvEmailHasDot.text = resources.getString(R.string.dot_in_email_good)
                    tvEmailHasDot.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.good
                        )
                    )
                } else {
                    tvEmailHasDot.text = resources.getString(R.string.dot_in_email_bad)
                    tvEmailHasDot.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.bad
                        )
                    )
                }
            }
        })

        tvAlreadyRegistered.setOnClickListener {
            Intent(this, LoginActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}