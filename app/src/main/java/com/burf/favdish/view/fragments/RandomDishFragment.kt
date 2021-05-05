package com.burf.favdish.view.fragments

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.burf.favdish.R
import com.burf.favdish.application.FavDishApplication
import com.burf.favdish.databinding.FragmentRandomDishBinding
import com.burf.favdish.model.entities.FavDish
import com.burf.favdish.model.entities.RandomDish
import com.burf.favdish.utils.Constants
import com.burf.favdish.utils.Helper
import com.burf.favdish.viewmodel.FavDishViewModel
import com.burf.favdish.viewmodel.FavDishViewModelFactory
import com.burf.favdish.viewmodel.NotificationsViewModel
import com.burf.favdish.viewmodel.RandomDishViewModel

class RandomDishFragment : Fragment() {

    private var mBinding: FragmentRandomDishBinding? = null
    private lateinit var mRandomDishViewModel: RandomDishViewModel
    private var mProgressDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentRandomDishBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRandomDishViewModel = ViewModelProvider(this).get(RandomDishViewModel::class.java)
        mRandomDishViewModel.getRandomRecipeFromApi()
        randomDishViewModelObserver()

        // pull to refresh
        mBinding!!.srlRandomDish.setOnRefreshListener {
            mRandomDishViewModel.getRandomRecipeFromApi()
        }

    }

    private fun showCustomProgressDialog() {
        mProgressDialog = Dialog(requireActivity())
        mProgressDialog?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
        }
    }

    private fun hideCustomProgressDialog() {
        mProgressDialog?.let {
            it.dismiss()
        }
    }

    private fun randomDishViewModelObserver() {
        mRandomDishViewModel.randomDishResponse.observe(viewLifecycleOwner,
            { randomDishResponse ->
                randomDishResponse?.let {
                    Log.i("API", "$randomDishResponse.recipes[0]")
                    setRandomDishResponseInUI(randomDishResponse.recipes[0])

                    if (mBinding!!.srlRandomDish.isRefreshing) {
                        mBinding!!.srlRandomDish.isRefreshing = false
                    }
                }
            }
        )
        mRandomDishViewModel.randomDishLoadingError.observe(viewLifecycleOwner, { dataError ->
            dataError?.let {
                Log.i("API", "ERROR $dataError")

                if (mBinding!!.srlRandomDish.isRefreshing) {
                    mBinding!!.srlRandomDish.isRefreshing = false
                }

            }
        }
        )
        mRandomDishViewModel.loadRandomDish.observe(viewLifecycleOwner, { loadRandomDish ->
            loadRandomDish?.let {
                Log.i("API", "loadRandomDish $loadRandomDish")

                if (loadRandomDish && !mBinding!!.srlRandomDish.isRefreshing) {
                    showCustomProgressDialog()
                } else {
                    hideCustomProgressDialog()
                }
            }
        }
        )

    }

    private fun setRandomDishResponseInUI(recipe: RandomDish.Recipe) {

        // Load the dish image in the ImageView.
        Glide.with(requireActivity())
            .load(recipe.image)
            .centerCrop()
            .into(mBinding!!.ivDishImage)

        mBinding!!.tvTitle.text = recipe.title

        // Default Dish Type
        var dishType: String = "other"

        if (recipe.dishTypes.isNotEmpty()) {
            dishType = recipe.dishTypes[0]
            mBinding!!.tvType.text = dishType
        }

        // There is not category params present in the response so we will define it as Other.
        mBinding!!.tvCategory.text = "Other"

        var ingredients = ""
        for (value in recipe.extendedIngredients) {

            if (ingredients.isEmpty()) {
                ingredients = value.original
            } else {
                ingredients = ingredients + ", \n" + value.original
            }
        }

        mBinding!!.tvIngredients.text = ingredients

        // The instruction or you can say the Cooking direction text is in the HTML format so we will you the fromHtml to populate it in the TextView.
        mBinding!!.tvCookingDirection.text = Helper.stripHtml(recipe.instructions)

        mBinding!!.ivFavoriteDish.setImageDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.ic_favorite_unselected
            )
        )

        var addedToFavorites = false

        mBinding!!.tvCookingTime.text =
            resources.getString(
                R.string.lbl_estimate_cooking_time,
                recipe.readyInMinutes.toString()
            )

        // save the recipe in to DB
        mBinding!!.ivFavoriteDish.setOnClickListener {

            if (addedToFavorites) {
                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_already_added_to_favorites),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val randomDishDetails = FavDish(
                    recipe.image,
                    Constants.DISH_IMAGE_SOURCE_ONLINE,
                    recipe.title,
                    dishType,
                    "Other",
                    ingredients,
                    recipe.readyInMinutes.toString(),
                    recipe.instructions,
                    true
                )

                val mFavDishViewModel: FavDishViewModel by viewModels {
                    FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
                }

                mFavDishViewModel.insert(randomDishDetails)
                addedToFavorites = true

                mBinding!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                    )
                )


                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_added_to_favorites),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}