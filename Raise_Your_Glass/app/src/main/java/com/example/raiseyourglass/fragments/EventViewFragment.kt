package com.example.raiseyourglass.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.R
import com.example.raiseyourglass.adapters.EventParticipantsListAdapter
import com.example.raiseyourglass.adapters.OrderDrinksAdapter
import com.example.raiseyourglass.dataclasses.Event
import com.example.raiseyourglass.dataclasses.Order
import com.example.raiseyourglass.firebase.Firebase
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_make_order.view.*
import kotlinx.android.synthetic.main.fragment_event_view.*

class EventViewFragment(
    val event: Event,
    val setCurrentFragment: (fragment: Fragment) -> Unit
) : Fragment(R.layout.fragment_event_view) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Firebase.setContext(view.context)
        setEventDetails()
        setRecycleViewParticipant()
        setInitialButtonForOrders()
    }

    private fun setRecycleViewParticipant() {
        val userID = Firebase.getUserId()
        val filterNotFunction = {elem:String -> elem == userID}
        val adapter = EventParticipantsListAdapter(event.participants.filterNot(filterNotFunction),
            event.invited.filterNot(filterNotFunction))
        rvEventParticipants.adapter = adapter
        rvEventParticipants.layoutManager = LinearLayoutManager(view?.context)
    }

    private fun setEventDetails() {
        Firebase.setUserToTextView(event.ownerID, tvEventHostName)
        tvEventIsPrivate.setText(if (event.isPrivate) "Private" else "Public")
        val localDate = event.dateToLocalDate()
        tvEventDate.setText("${localDate.year}-${localDate.month.value}-${localDate.dayOfMonth}")
        tvEventPlace.setText(event.place)

        if (event.ownerID == Firebase.getUserId()) {
            switchInvitation.visibility = View.GONE
        } else {
            val userID = Firebase.getUserId()
            switchInvitation.isChecked = event.participants.contains(userID)
            switchInvitation.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked && userID != null)Firebase.participate(event)
                else if (!isChecked && userID != null)Firebase.participate(event)
                btnMakeOrder.isEnabled = isChecked
            }
        }
    }

    private fun setInitialButtonForOrders(){
        val currentUserID = Firebase.getUserId()
        if(event.ownerID == currentUserID){
            btnMakeOrder.text = "Show Orders"
            setButtonMakeOrderForOwner()
        }
        else{
            btnMakeOrder.isEnabled = event.participants.contains(currentUserID)
            setButtonMakeOrderForParticipants()
        }
    }

    private fun setButtonMakeOrderForOwner(){
        btnMakeOrder.setOnClickListener {
            val fragment = OrdersDetailsFragment(event, setCurrentFragment)
            setCurrentFragment(fragment)
        }
    }

    private fun setButtonMakeOrderForParticipants(){
        btnMakeOrder.setOnClickListener {
            val adapter = OrderDrinksAdapter(event)
            val bottomSheetDialog = BottomSheetDialog(activity!!)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_make_order, null)
            Firebase.setCommentFromOrderToTextView(event, view.etComment)
            view.rvMakeOrder.apply {
                this.adapter = adapter
                this.layoutManager = LinearLayoutManager(view?.context)
            }
            view.btnMakeOrderSubmit.setOnClickListener {
                val orderedDrinks =
                    adapter.drinksOrders.filter { (_, boolean) -> boolean }.map{ (drink, _) -> drink }.toMutableList()
                val comment = view.etComment.text.toString()
                val order = Order(
                    Firebase.getUserId()!!,
                    orderedDrinks.map { drink -> drink.documentID!! },
                    comment,
                    event.documentID!!.id
                )
                Firebase.deleteOrder(order)
                Firebase.addOrder(order)
                bottomSheetDialog.dismiss()
            }
            view.svMakeOrder.apply{
                setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(drinkName: String?): Boolean {
                        adapter.filterByName(drinkName ?: "")
                        return true
                    }
                })
            }
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }
    }

}