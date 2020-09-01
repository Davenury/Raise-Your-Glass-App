package com.example.raiseyourglass.firebase

object Validator {

    fun areValid(email: String, password: String):Boolean{
        return isPasswordValid(password) && isEmailValid(email)
    }

    fun isPasswordValid(password: String): Boolean{
        return password.isNotEmpty() && password.length >= 6
    }

    fun isEmailValid(email: String): Boolean{
        return email.isNotEmpty() && email.contains("@")
    }
}