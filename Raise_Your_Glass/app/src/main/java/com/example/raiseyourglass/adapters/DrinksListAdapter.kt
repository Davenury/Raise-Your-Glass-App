package com.example.raiseyourglass.adapters

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.raiseyourglass.R
import com.example.raiseyourglass.dataclasses.Drink
import com.example.raiseyourglass.firebase.Firebase
import com.example.raiseyourglass.fragments.DrinkViewFragment
import com.example.raiseyourglass.fragments.OwnDrinkFragment
import kotlinx.android.synthetic.main.item_drinks_list.view.*
import kotlinx.android.synthetic.main.item_drinks_list.view.tvDrinkName
import kotlinx.android.synthetic.main.item_drinks_list.view.tvDrinkType
import kotlinx.android.synthetic.main.item_drinks_list_with_deletion.view.*

class DrinksListAdapter(
    val setCurrentFragment: (fragment: Fragment) -> Unit,
    val userFilter: String? = null
) : RecyclerView.Adapter<DrinksListAdapter.DrinksListViewHolder>() {

    var drinksList: MutableList<Drink> = mutableListOf()

    init {
        Firebase.subscribeToDrinkSnapshotListener(this, userFilter)
    }


    inner class DrinksListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinksListViewHolder {
        val layout = if(userFilter == null) R.layout.item_drinks_list else R.layout.item_drinks_list_with_deletion
        val view =
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return DrinksListViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrinksListViewHolder, position: Int) {
        holder.itemView.apply {
            tvDrinkName.text = drinksList[position].name
            tvDrinkType.text = drinksList[position].type
            if(userFilter == null)  // means that it's item_drinks_list
                Firebase.setImageToView(drinksList[position].getImagePath(), ivDrinkItemDrinkImage)
            else
                Firebase.setImageToView(drinksList[position].getImagePath(), ivDrinkItemDrinkImageDelete)
        }

        val drinkFragment =
            if (userFilter != null) OwnDrinkFragment(drinksList[position]) else DrinkViewFragment(
                drinksList[position]
            )


        holder.itemView.setOnClickListener {
            setCurrentFragment(drinkFragment)
        }

        if(userFilter != null){
            holder.itemView.ivDeleteDrink.setOnClickListener {
                AlertDialog.Builder(Firebase.getContext())
                    .setTitle("Drink Deletion")
                    .setIcon(R.drawable.ic_deletion_warning)
                    .setMessage("Are you sure to delete this drink?")
                    .setPositiveButton("Yes, delete this drink"
                    ) { _, _ ->
                        Firebase.deleteDrink(drinksList[position])
                    }
                    .setNegativeButton("No, don't delete it") { _, _ ->
                        Toast.makeText(Firebase.getContext(), "You decided not to delete this drink", Toast.LENGTH_SHORT).show()
                    }
                    .create()
                    .show()
            }
        }

    }

    override fun getItemCount(): Int {
        return drinksList.size
    }

    fun changeType(type: String){
        when(type){
            "Favorites" -> Firebase.subscribeToFavoriteDrinkSnapshotListener(this)
            "All" -> Firebase.subscribeToDrinkSnapshotListener(this, userFilter)
        }
    }

}