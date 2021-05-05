package com.burf.favdish.view.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.burf.favdish.R
import com.burf.favdish.databinding.ItemDishLayoutBinding
import com.burf.favdish.model.entities.FavDish
import com.burf.favdish.utils.Constants
import com.burf.favdish.view.activities.AddUpdateDishActivity
import com.burf.favdish.view.fragments.AllDishesFragment
import com.burf.favdish.view.fragments.FavoriteDishesFragment

class FavDishAdapter(private val fragment: Fragment) : RecyclerView.Adapter<FavDishAdapter.ViewHolder>() {

    private var dishes : List<FavDish> = listOf()

    class ViewHolder(view: ItemDishLayoutBinding): RecyclerView.ViewHolder(view.root) {
        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
        val ibMore = view.ibMore
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDishLayoutBinding = ItemDishLayoutBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishes[position]

        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDishImage)

        holder.tvTitle.text = dish.title

        holder.itemView.setOnClickListener{
            if (fragment is AllDishesFragment ) {
                fragment.dishDetails(dish)
            } else if (fragment is FavoriteDishesFragment) {
                fragment.dishDetails(dish)
            }
        }

        // menu on button
        holder.ibMore.setOnClickListener {
            val popup = PopupMenu(fragment.context, holder.ibMore )
            popup.menuInflater.inflate(R.menu.menu_adapter, popup.menu)

            popup.setOnMenuItemClickListener {
                if (it.itemId == R.id.action_edit_dish) {

                    // launch activity from fragment
                    val intent = Intent(fragment.requireActivity(), AddUpdateDishActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DISH_DETAILS, dish)
                    fragment.requireActivity().startActivity(intent)

                } else if (it.itemId == R.id.action_delete_dish) {
                    if (fragment is AllDishesFragment) {
                        fragment.deleteDish(dish)
                    }

                }
                true
            }

            popup.show()
        }

        if (fragment is AllDishesFragment) {
            holder.ibMore.visibility = View.VISIBLE
        } else {
            holder.ibMore.visibility = View.INVISIBLE
        }
    }

    fun dishesList(list: List<FavDish>) {
        dishes = list
        notifyDataSetChanged()
    }
}