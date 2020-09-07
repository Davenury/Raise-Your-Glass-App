package com.example.raiseyourglass.adapters

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.User
import com.example.raiseyourglass.firebase.Firebase
import com.example.raiseyourglass.listeners.InvitedUsersListener
import kotlinx.android.synthetic.main.item_invite_user.view.*

class InviteUsersAdapter(
    var invitedUsersListener: InvitedUsersListener
) : RecyclerView.Adapter<InviteUsersAdapter.InviteUserHolder>() {

    var users = mutableListOf<User>()

    inner class InviteUserHolder(itemView: View) :  RecyclerView.ViewHolder(itemView)

    init{
        Firebase.getAllUsers(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteUserHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_invite_user, parent, false)
        return InviteUserHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: InviteUserHolder, position: Int) {
        val currentUser = users[position]
        holder.itemView.apply{
            this.tvInvitedUserName.text = if(currentUser.name == "") currentUser.email else currentUser.name
            this.cbIsInvited.setOnClickListener{
                if(this.cbIsInvited.isChecked){
                    invitedUsersListener.onCheckItem(currentUser.userID)
                }else{
                    invitedUsersListener.onUncheckItem(currentUser.userID)
                }
            }
            if(currentUser.userID == Firebase.getUserId()) this.visibility = View.GONE
        }
    }
}