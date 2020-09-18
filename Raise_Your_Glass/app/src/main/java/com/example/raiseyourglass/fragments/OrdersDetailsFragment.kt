package com.example.raiseyourglass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.R
import com.example.raiseyourglass.adapters.OrderDrinksAdapter
import com.example.raiseyourglass.adapters.OwnerOrdersDetailsAdapter
import com.example.raiseyourglass.dataclasses.Event
import com.example.raiseyourglass.firebase.Firebase
import kotlinx.android.synthetic.main.fragment_orders_details.*

class OrdersDetailsFragment(
    var event: Event,
    var setCurrentFragment: (fragment: Fragment) -> Unit
) : Fragment(R.layout.fragment_orders_details) 

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Firebase.setContext(view.context)

        setRecyclerView()
        setShoppingListButton()
    }

    private fun setRecyclerView(){
        val adapter = OwnerOrdersDetailsAdapter(event)
        rvOrdersDetails.adapter = adapter
        rvOrdersDetails.layoutManager = LinearLayoutManager(view?.context)
    }

    private fun setShoppingListButton(){
        btnShoppingList.setOnClickListener {
            Toast.makeText(context, "HAHAHAHAHAHAH", Toast.LENGTH_SHORT).show()
        }
    }
}