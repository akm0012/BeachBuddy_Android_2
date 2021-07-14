package com.andrewkingmarshall.beachbuddy2.ui.fragments

import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.lifecycle.ViewModelProvider
import com.andrewkingmarshall.beachbuddy2.databinding.FragmentDialogItemAddedBinding
import com.andrewkingmarshall.beachbuddy2.viewmodels.ItemAddedDialogViewModel
import dagger.hilt.android.AndroidEntryPoint

const val DIALOG_COOL_DOWN_MILLIS: Long = 10 * 60 * 1000 // 10 min

@AndroidEntryPoint
class ItemAddedDialogFragment : BaseDialogFragment<FragmentDialogItemAddedBinding>(FragmentDialogItemAddedBinding::inflate) {

    lateinit var itemViewModel: ItemAddedDialogViewModel

    var progressBarAnimation = ObjectAnimator()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemViewModel =
            ViewModelProvider(requireActivity()).get(ItemAddedDialogViewModel::class.java)
    }

    override fun setup(view: View) {
        itemViewModel.onDialogShown()

        // Listen for title
        itemViewModel.titleString.observe(viewLifecycleOwner, {title ->
            binding.itemAddedTextView.text = title
        })

        // Listen for main text
        itemViewModel.itemsAddedString.observe(viewLifecycleOwner, { itemsAdded ->
            binding.itemAddedToListTextView.text = itemsAdded
        })


        progressBarAnimation = ObjectAnimator.ofInt(binding.progressBar, "progress", 100).apply {
            duration = DIALOG_COOL_DOWN_MILLIS
            interpolator = LinearInterpolator()
            doOnEnd { dialog?.dismiss() }
            start()
        }

        itemViewModel.restartTimerEvent.observe(viewLifecycleOwner, {

            // Remove the listeners so the dialog won't close
            progressBarAnimation.removeAllListeners()

            // Restart the progress
            binding.progressBar.progress = 0

            // Add the listener back
            progressBarAnimation.doOnEnd {
                dialog?.dismiss()
            }

            progressBarAnimation.start()
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        itemViewModel.onDialogClosed()
    }

}