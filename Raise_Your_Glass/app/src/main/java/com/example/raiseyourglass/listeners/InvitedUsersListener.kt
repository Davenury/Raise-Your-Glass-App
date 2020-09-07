package com.example.raiseyourglass.listeners

interface InvitedUsersListener {
    fun onCheckItem(userID: String)
    fun onUncheckItem(userID: String)
}