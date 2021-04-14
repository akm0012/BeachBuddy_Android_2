package com.andrewkingmarshall.beachbuddy2.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels.SunsetTimerViewModel
import kotlinx.android.synthetic.main.compound_view_sunset_timer.view.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class SunsetTimerView : FrameLayout {

    private var viewModel: SunsetTimerViewModel? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        View.inflate(context, R.layout.compound_view_sunset_timer, this)
    }

    fun setSunsetSunriseTimes(
        sunrise: Long,
        sunset: Long,
        sunriseNextDay: Long,
        sunsetPrevDay: Long
    ) {
        viewModel = SunsetTimerViewModel(sunrise, sunset, sunriseNextDay, sunsetPrevDay)
        updateTimer()
    }

    fun updateTimer() {
        val currentTime = DateTime().withZone(DateTimeZone.getDefault()).millis

        sunsetCountDownTextView.text = viewModel?.getTimerText(currentTime)
        sunsetTimeTextView.text = viewModel?.getSubtitleTime(currentTime)
        val progressInt = viewModel?.getProgressInt(currentTime) ?: 0
        arcProgressView.progress = progressInt
        arcProgressView.bottomText = viewModel?.getBottomLabel(currentTime)
    }

    companion object {
        val TIMER_DELAY = 1000L
    }
}


