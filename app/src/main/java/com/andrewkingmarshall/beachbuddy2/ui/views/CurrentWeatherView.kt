package com.andrewkingmarshall.beachbuddy2.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.andrewkingmarshall.beachbuddy2.databinding.CompoundViewCurrentWeatherBinding
import com.andrewkingmarshall.beachbuddy2.ui.domainmodels.WeatherDM
import com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels.CurrentWeatherViewModel

class CurrentWeatherView : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var binding: CompoundViewCurrentWeatherBinding =
        CompoundViewCurrentWeatherBinding.inflate(LayoutInflater.from(context), this)

    fun setWeather(weather: WeatherDM) {

        val viewModel = CurrentWeatherViewModel(context, weather)

        // Set all the Text
        binding.cityNameTextView.text = viewModel.getCityName()
        binding.currentWeatherDescription.text = viewModel.getWeatherDescription()
        binding.currentTempTextView.text = viewModel.getFeelsLikeTemp()

        // Set the Card color
        binding.cardView.setCardBackgroundColor(
            ContextCompat.getColor(context, viewModel.getCardBackgroundColor())
        )

        // Set Text Color
        binding.cityNameTextView.setTextColor(ContextCompat.getColor(context, viewModel.getTextColor()))
        binding.currentWeatherDescription.setTextColor(
            ContextCompat.getColor(
                context,
                viewModel.getSecondaryTextColor()
            )
        )
        binding.currentTempTextView.setTextColor(ContextCompat.getColor(context, viewModel.getTextColor()))

        loadImage(context, viewModel.getIconUrl(), binding.currentWeatherIconImageView)
    }

}