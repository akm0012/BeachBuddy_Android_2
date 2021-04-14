package com.andrewkingmarshall.beachbuddy2.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.ui.domainmodels.WeatherDM
import com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels.CurrentWeatherViewModel
import kotlinx.android.synthetic.main.compound_view_current_weather.view.*

class CurrentWeatherView : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        View.inflate(context, R.layout.compound_view_current_weather, this)
    }

    fun setWeather(weather: WeatherDM) {

        val viewModel = CurrentWeatherViewModel(context, weather)

        // Set all the Text
        cityNameTextView.text = viewModel.getCityName()
        currentWeatherDescription.text = viewModel.getWeatherDescription()
        currentTempTextView.text = viewModel.getFeelsLikeTemp()

        // Set the Card color
        cardView.setCardBackgroundColor(
            ContextCompat.getColor(context, viewModel.getCardBackgroundColor())
        )

        // Set Text Color
        cityNameTextView.setTextColor(ContextCompat.getColor(context, viewModel.getTextColor()))
        currentWeatherDescription.setTextColor(
            ContextCompat.getColor(
                context,
                viewModel.getSecondaryTextColor()
            )
        )
        currentTempTextView.setTextColor(ContextCompat.getColor(context, viewModel.getTextColor()))

        loadImage(context, viewModel.getIconUrl(), currentWeatherIconImageView)
    }

}