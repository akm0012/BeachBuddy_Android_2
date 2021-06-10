package com.andrewkingmarshall.beachbuddy2.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.databinding.CompoundViewLeaderBoardItemBinding
import com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels.LeaderBoardItemViewModel

class LeaderBoardItemView : ConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var binding: CompoundViewLeaderBoardItemBinding =
        CompoundViewLeaderBoardItemBinding.inflate(LayoutInflater.from(context), this)

    init {
        setBackgroundResource(R.drawable.transparent_gray_ripple_square)
    }

    private fun resetView() {
        binding.apply {
            nameTextView.text = ""
            totalScoreTextView.text = ""
            subtitleTextView.text = ""
            profileImageView.setImageDrawable(null)
        }
    }

    fun setViewModel(viewModel: LeaderBoardItemViewModel) {

        resetView()

        binding.apply {
            nameTextView.text = viewModel.getName()
            totalScoreTextView.text = viewModel.getScore()
            subtitleTextView.text = viewModel.getSubtitle()
        }

        loadCircleProfilePhoto(context, viewModel.getProfilePhotoUrl(), binding.profileImageView)
    }
}