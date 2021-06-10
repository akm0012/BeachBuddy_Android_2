package com.andrewkingmarshall.beachbuddy2.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.andrewkingmarshall.beachbuddy2.databinding.CompoundViewBeachConditionItemBinding
import com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels.BeachConditionItemViewModel

class BeachConditionItemView : ConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var binding: CompoundViewBeachConditionItemBinding =
        CompoundViewBeachConditionItemBinding.inflate(LayoutInflater.from(context), this)

    private fun resetView() {
        binding.iconImageView.setImageDrawable(null)
        binding.titleTextView.text = ""
        binding.contentTextView.text = ""
        binding.bottomDivider.visibility = View.GONE
    }

    fun setHeight(height: Int) {
        val params = layoutParams
        params.height = height
        layoutParams = params
    }

    fun setViewModel(viewModel: BeachConditionItemViewModel) {
        resetView()
        binding.iconImageView.setImageDrawable(ContextCompat.getDrawable(context, viewModel.getIconDrawable()))
        binding.titleTextView.text = viewModel.getTitle()
        binding.contentTextView.text = viewModel.getContent()

    }

    fun setBottomDividerVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.bottomDivider.visibility = View.VISIBLE
        } else {
            binding.bottomDivider.visibility = View.GONE
        }
    }
}