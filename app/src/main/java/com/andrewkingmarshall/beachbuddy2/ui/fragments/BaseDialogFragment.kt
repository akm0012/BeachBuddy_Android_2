package com.andrewkingmarshall.beachbuddy2.ui.fragments

import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

open class BaseDialogFragment : DialogFragment() {

    open var _binding: ViewBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}