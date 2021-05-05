package com.burf.favdish.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.burf.favdish.R
import com.burf.favdish.application.FavDishApplication
import com.burf.favdish.databinding.DialogCustomListBinding
import com.burf.favdish.databinding.FragmentAllDishesBinding
import com.burf.favdish.model.entities.FavDish
import com.burf.favdish.utils.Constants
import com.burf.favdish.view.activities.AddUpdateDishActivity
import com.burf.favdish.view.activities.MainActivity
import com.burf.favdish.view.adapters.CustomListItemAdapter
import com.burf.favdish.view.adapters.FavDishAdapter
import com.burf.favdish.viewmodel.FavDishViewModel
import com.burf.favdish.viewmodel.FavDishViewModelFactory
import com.burf.favdish.viewmodel.HomeViewModel

class AllDishesFragment : Fragment() {

    private lateinit var mBinding: FragmentAllDishesBinding
    private lateinit var mFavDishAdapter: FavDishAdapter
    private lateinit var mCustomListDialog : Dialog

    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentAllDishesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        mFavDishAdapter = FavDishAdapter(this@AllDishesFragment)
        mBinding.rvDishesList.adapter = mFavDishAdapter

        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner , Observer {
            dishes ->
                populateRecycleView(dishes)
        })
    }

    fun dishDetails(favDish : FavDish) {
        findNavController().navigate(AllDishesFragmentDirections.actionNavigationAllDishesToNavigationDishDetails(
            favDish
        ))

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.hideBottomNavigationView()
        }
    }

    fun deleteDish(dish : FavDish) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.delete_dish))
        builder.setMessage(getString(R.string.delete_dish_confirm, dish.title))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.yes)){dialogInterface, _ ->
            mFavDishViewModel.delete(dish)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(resources.getString(R.string.no)){dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun filterDishesListDialog(){
        mCustomListDialog = Dialog(requireActivity())
        val binding : DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)

        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = resources.getString(R.string.select_item_to_filter)

        val dishTypes = Constants.dishTypes()
        dishTypes.add(0, Constants.ALL_ITEMS)

        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = CustomListItemAdapter(requireActivity(), this@AllDishesFragment,  dishTypes, Constants.FILTER_SELECTION)
        binding.rvList.adapter = adapter

        mCustomListDialog.show()
    }

    override fun onResume() {
        super.onResume()

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.showBottomNavigationView()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
                return true
            }
            R.id.action_filter_dishes -> {
                filterDishesListDialog()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun filterSelection(filterItemSection : String) {
        mCustomListDialog.dismiss()

        Log.i("TAG", filterItemSection)

        if (filterItemSection == Constants.ALL_ITEMS) {
            mFavDishViewModel.allDishesList.observe(viewLifecycleOwner , Observer {
                    dishes ->
                populateRecycleView(dishes)
            })
        } else {
            mFavDishViewModel.getFilteredList(filterItemSection).observe(viewLifecycleOwner , Observer {
                    dishes ->
                populateRecycleView(dishes)
            })
        }
    }

    private fun populateRecycleView(dishes: List<FavDish>?) {
        dishes.let {
            if (it!!.isNotEmpty()) {
                mBinding.rvDishesList.visibility = View.VISIBLE
                mBinding.tvNoDishesAddedYet.visibility = View.GONE

                mFavDishAdapter.dishesList(it!!)
            } else {
                mBinding.rvDishesList.visibility = View.GONE
                mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
            }
        }
    }
}