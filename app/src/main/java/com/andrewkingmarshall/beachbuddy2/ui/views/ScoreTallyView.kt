package com.andrewkingmarshall.beachbuddy2.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.andrewkingmarshall.beachbuddy2.databinding.CompoundViewScoreTallyItemBinding

class ScoreTallyView : ConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var binding: CompoundViewScoreTallyItemBinding =
        CompoundViewScoreTallyItemBinding.inflate(LayoutInflater.from(context), this)

    var listener: InteractionListener? = null

    interface InteractionListener {
        fun onDecrement()

        fun onIncrement()
    }

    private fun resetView() {
        binding.apply {
            gameNameTextView.text = ""
            scoreTextView.text = ""
        }
    }

    fun setScore(gameName: String, score: Int = 0, listener: InteractionListener) {

        resetView()

        binding.apply {
            gameNameTextView.text = gameName
            scoreTextView.text = score.toString()

            decrementButton.setOnClickListener { listener.onDecrement() }
            incrementButton.setOnClickListener { listener.onIncrement() }
        }
    }
}