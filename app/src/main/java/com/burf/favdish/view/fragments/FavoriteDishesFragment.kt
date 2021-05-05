package com.burf.favdish.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.burf.favdish.R
import com.burf.favdish.application.FavDishApplication
import com.burf.favdish.databinding.FragmentAllDishesBinding
import com.burf.favdish.databinding.FragmentFavoriteDishesBinding
import com.burf.favdish.model.entities.FavDish
import com.burf.favdish.view.activities.MainActivity
import com.burf.favdish.view.adapters.FavDishAdapter
import com.burf.favdish.viewmodel.DashboardViewModel
import com.burf.favdish.viewmodel.FavDishViewModel
import com.burf.favdish.viewmodel.FavDishViewModelFactory

class FavoriteDishesFragment : Fragment() {

    private var mBinding: FragmentFavoriteDishesBinding? = null

    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFavDishViewModel.favoriteDishesList.observe(viewLifecycleOwner , Observer {
                dishes ->
            dishes.let {
                mBinding!!.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
                val favDishAdapter = FavDishAdapter(this)
                mBinding!!.rvDishesList.adapter = favDishAdapter

                if (it.isNotEmpty()) {
                    mBinding!!.rvDishesList.visibility = View.VISIBLE
                    mBinding!!.tvNoDishesAddedYet.visibility = View.GONE

                    favDishAdapter.dishesList(it)
                } else {
                    mBinding!!.rvDishesList.visibility = View.GONE
                    mBinding!!.tvNoDishesAddedYet.visibility = View.VISIBLE
                }
            }
        })
    }

    fun dishDetails(favDish : FavDish) {
        findNavController().navigate(FavoriteDishesFragmentDirections.actionNavigationFavoriteDishesToNavigationDishDetails(
            favDish
        ))

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.hideBottomNavigationView()
        }
    }

    override fun onResume() {
        super.onResume()

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.showBottomNavigationView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mBinding = null
    }
}