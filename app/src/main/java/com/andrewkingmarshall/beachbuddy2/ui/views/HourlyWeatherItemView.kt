package com.andrewkingmarshall.beachbuddy2.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.andrewkingmarshall.beachbuddy2.databinding.CompoundViewHourlyWeatherItemBinding
import com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels.HourlyWeatherItemViewModel

class HourlyWeatherItemView : ConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var binding: CompoundViewHourlyWeatherItemBinding =
        CompoundViewHourlyWeatherItemBinding.inflate(LayoutInflater.from(context), this)

    private fun resetView() {
        binding.apply {
            hourTimeTextView.text = ""
            hourIconImageView.setImageDrawable(null)
            summaryTextView.text = ""
            feelsLikeTextView.text = ""
            humidityTextView.text = ""
        }
    }

    fun setViewModel(viewModel: HourlyWeatherItemViewModel) {
        resetView()

        binding.apply {
            hourTimeTextView.text = viewModel.getTime()
            summaryTextView.text = viewModel.getSummary()
            feelsLikeTextView.text = viewModel.getFeelsLike()
            humidityTextView.text = viewModel.getHumidity()
        }

        loadImage(context, viewModel.getIconUrl(), binding.hourIconImageView)
    }

}