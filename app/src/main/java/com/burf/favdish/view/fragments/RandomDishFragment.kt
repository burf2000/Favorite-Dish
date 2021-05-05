package com.burf.favdish.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.burf.favdish.R
import com.burf.favdish.databinding.FragmentRandomDishBinding
import com.burf.favdish.viewmodel.NotificationsViewModel

class RandomDishFragment : Fragment() {

    private var mBinding: FragmentRandomDishBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentRandomDishBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}