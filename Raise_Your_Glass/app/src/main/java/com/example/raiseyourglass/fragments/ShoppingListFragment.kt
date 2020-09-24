package com.example.raiseyourglass.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raiseyourglass.R
import com.example.raiseyourglass.adapters.ShoppingListAdapter
import com.example.raiseyourglass.dataclasses.Event
import kotlinx.android.synthetic.main.fragment_shopping_list.*

class ShoppingListFragment : Fragment(R.layout.fragment_shopping_list) {

    private lateinit var event: Event

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
    }

    fun setEvent(event: Event){
        this.event = event
    }

    private fun setRecyclerView(){
        val adapter = ShoppingListAdapter(event)
        rvShoppingList.adapter = adapter
        rvShoppingList.layoutManager = LinearLayoutManager(view?.context)
    }

}