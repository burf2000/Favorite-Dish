package com.burf.favdish.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.burf.favdish.databinding.ItemCustomListBinding
import com.burf.favdish.view.activities.AddUpdateDishActivity
import com.burf.favdish.view.fragments.AllDishesFragment

class CustomListItemAdapter (private val activity: Activity,
                             private val fragment: Fragment?,
                             private val listItems: List<String>,
                             private val selction: String)
    : RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>(){

    class ViewHolder(view: ItemCustomListBinding): RecyclerView.ViewHolder(view.root) {
        val tvText= view.tvText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // View Binding
        val binding: ItemCustomListBinding = ItemCustomListBinding.inflate(LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listItems.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.tvText.text = item

        // setup the onlClickListener
        holder.itemView.setOnClickListener() {
            if (activity is AddUpdateDishActivity) {
                activity.selectedListItem(item, selction)
            }
            if(fragment is AllDishesFragment) {
                // this is why we have the fragment
                fragment.filterSelection(item)
            }
        }
    }

}