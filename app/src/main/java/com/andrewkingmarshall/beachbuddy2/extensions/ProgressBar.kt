package com.andrewkingmarshall.beachbuddy2.extensions

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar

fun ProgressBar.smoothProgress(timeToFillSec: Long): ObjectAnimator {

    val millisToFill = timeToFillSec * 1000

    val animation = ObjectAnimator.ofInt(this, "progress", 100)
    animation.duration = millisToFill
    animation.interpolator = LinearInterpolator()
    animation.start()

    return animation
}
