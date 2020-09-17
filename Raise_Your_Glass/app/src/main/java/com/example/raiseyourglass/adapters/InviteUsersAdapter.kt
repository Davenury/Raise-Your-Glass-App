package com.example.raiseyourglass.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.User
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.item_invite_user.view.*

class InviteUsersAdapter(val invited: MutableList<String> = mutableListOf()) : RecyclerView.Adapter<InviteUsersAdapter.InviteUserHolder>() {

    var users:MutableList<Pair<User,Boolean>> = mutableListOf()
    inner class InviteUserHolder(itemView: View) :  RecyclerView.ViewHolder(itemView)

    init{
        Firebase.getAllUsers(this)
        Log.e("users", this.users.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteUserHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_invite_user, parent, false)
        return InviteUserHolder(view)
    }

    override fun getItemCount(): Int {
        return this.users.size
    }

    override fun onBindViewHolder(holder: InviteUserHolder, position: Int) {
        val currentUser = this.users[position]
        holder.itemView.apply{
            this.tvInvitedUserName.text = if(currentUser.first.name == "") currentUser.first.email else currentUser.first.name
            this.cbIsInvited.isChecked = users[position].second
            this.cbIsInvited.setOnClickListener{
                if(this.cbIsInvited.isChecked){
                    users[position] = Pair(currentUser.first,true)
                }else{
                    users[position] = Pair(currentUser.first,false)
                }
            }
            if(currentUser.first.userID == Firebase.getUserId()) this.visibility = View.GONE
        }
    }


}