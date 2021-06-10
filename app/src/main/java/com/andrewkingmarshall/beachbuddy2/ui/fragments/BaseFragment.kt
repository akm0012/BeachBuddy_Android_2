package com.andrewkingmarshall.beachbuddy2.ui.fragments

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

open class BaseFragment : Fragment() {

    open var _binding: ViewBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}