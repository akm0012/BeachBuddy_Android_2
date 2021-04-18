package com.andrewkingmarshall.beachbuddy2.ui.fragments

import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.viewmodels.ItemAddedDialogViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dialog_item_added.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class ItemAddedDialogFragment : DialogFragment() {

    lateinit var itemViewModel: ItemAddedDialogViewModel

    var progressBarAnimation = ObjectAnimator()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemViewModel =
            ViewModelProvider(requireActivity()).get(ItemAddedDialogViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_item_added, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemViewModel.onDialogShown()

        progressBarAnimation = ObjectAnimator.ofInt(progressBar, "progress", 100).apply {
            duration = 4000
            interpolator = LinearInterpolator()

        }

        progressBarAnimation.doOnEnd {
            dialog?.dismiss()
        }

        progressBarAnimation.start()

        itemAddedImage.setOnClickListener {
            progressBarAnimation.removeAllListeners()

            progressBar.progress = 0
            progressBarAnimation.doOnEnd {
                dialog?.dismiss()
            }
            progressBarAnimation.start()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        itemViewModel.onDialogClosed()
    }

}