package com.andrewkingmarshall.beachbuddy2.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.andrewkingmarshall.beachbuddy2.databinding.CompoundViewDailyWeatherItemBinding
import com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels.DailyWeatherItemViewModel

class DailyWeatherItemView : ConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var binding: CompoundViewDailyWeatherItemBinding =
        CompoundViewDailyWeatherItemBinding.inflate(LayoutInflater.from(context), this)

    private fun resetView() {
        binding.dayTimeTextView.text = ""
        binding.dailyIconImageView.setImageDrawable(null)
        binding.summaryTextView.text = ""
        binding.feelsLikeTextView.text = ""
        binding.rainAmountTextView.text = ""
    }

    fun setViewModel(viewModel: DailyWeatherItemViewModel) {
        resetView()

        binding.dayTimeTextView.text = viewModel.getTime()
        binding.summaryTextView.text = viewModel.getSummary()
        binding.feelsLikeTextView.text = viewModel.getFeelsLike()
        binding.rainAmountTextView.text = viewModel.getRain()

        loadImage(context, viewModel.getIconUrl(), binding.dailyIconImageView)
    }

}