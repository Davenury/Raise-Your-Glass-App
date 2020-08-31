package com.example.raiseyourglass.firebase

object Firebase {

    /**LOGIN SECTION*/
    fun loginWithEmailAndPassword(email: String, password: String){
        LoginComponent.loginWithEmailAndPassword(email, password)
    }

    fun logout(){
        LoginComponent.logout()
    }


    /**REGISTER SECTION*/
    fun registerWithEmailAndPassword(email: String, password: String){
        RegisterComponent.registerUserWithEmailAndPassword(email, password)
    }


    /**PROFILE SECTION*/
    fun updateUserName(userName: String){
        UpdateUserComponent.updateUserName(userName)
    }

}