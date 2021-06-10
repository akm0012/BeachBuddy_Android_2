package com.andrewkingmarshall.beachbuddy2.ui.views

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.databinding.CompoundViewRequestedItemBinding
import com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels.RequestedItemViewModel

class RequestedItemView : FrameLayout {

    var interactionListener:InteractionListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var binding: CompoundViewRequestedItemBinding =
        CompoundViewRequestedItemBinding.inflate(LayoutInflater.from(context), this)

    interface InteractionListener {
        fun onCheckboxClicked()
    }

    init {
        binding.checkboxImageView.setOnClickListener {
            binding.checkboxImageView.setImageResource(R.drawable.ic_check_circle_grey_24dp)
            binding.checkboxImageView.isEnabled = false
            interactionListener?.onCheckboxClicked()
        }
    }

    private fun resetView() {
        binding.apply {
            checkboxImageView.setImageResource(R.drawable.ic_empty_check_24dp)
            checkboxImageView.isEnabled = true

            itemTitleTextView.text = ""

            subtitleTextView.text = ""

            profileImageView.setImageDrawable(null)

            itemTitleTextView.paintFlags =
                itemTitleTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            subtitleTextView.paintFlags =
                subtitleTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun setViewModel(viewModel: RequestedItemViewModel) {

        resetView()

        binding.itemTitleTextView.text = viewModel.getTitle()

        binding.subtitleTextView.text = viewModel.getSubTitle()

        if (viewModel.isChecked()) {

            binding.apply {
                checkboxImageView.isEnabled = false
                checkboxImageView.setImageResource(R.drawable.ic_check_circle_grey_24dp)

                // Strike through the TextViews
                itemTitleTextView.paintFlags =
                    itemTitleTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                subtitleTextView.paintFlags =
                    subtitleTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            loadCircleProfilePhoto(context, viewModel.getProfilePhotoUrl(), binding.profileImageView, true)

        } else {
            loadCircleProfilePhoto(context, viewModel.getProfilePhotoUrl(), binding.profileImageView)
        }
    }

}