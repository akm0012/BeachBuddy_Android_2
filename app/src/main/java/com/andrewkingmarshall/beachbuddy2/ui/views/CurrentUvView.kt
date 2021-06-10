package com.andrewkingmarshall.beachbuddy2.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.andrewkingmarshall.beachbuddy2.databinding.CompoundViewCurrentUvBinding
import com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels.CurrentUvViewModel

class CurrentUvView : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var viewModel: CurrentUvViewModel? = null

    private var binding: CompoundViewCurrentUvBinding =
        CompoundViewCurrentUvBinding.inflate(LayoutInflater.from(context), this)

    fun setViewModel(viewModel: CurrentUvViewModel) {

        this.viewModel = viewModel

        binding.sunView.setCurrentTime(viewModel.getSunViewStartTime())

        binding.sunView.apply {
            setStartTime(viewModel.getSunViewStartTime())
            setEndTime(viewModel.getSunViewEndTime())
            setCurrentTime(viewModel.getSunViewCurrentTime())
            setArcSolidColor(ContextCompat.getColor(context, viewModel.getUvColor()))
            binding.sunView.invalidate()
        }

        if (binding.safeExposureTimeTextView.visibility == View.VISIBLE) {
            binding.safeExposureTimeTextView.text = viewModel.getTimeToBurn(null)
        }

        binding.uvIndexTextView.text = viewModel.getUvIndex()
    }

    fun getSafeExposureTimeForSkinType(skinType: Int?): String {
        return viewModel?.getTimeToBurn(skinType) ?: "N/A"
    }

    fun showSafeExposureTimeForSkinType(skinType: Int?) {
        binding.safeExposureTimeTextView.text = viewModel?.getTimeToBurn(skinType)

        binding.safeExposureTitle.visibility = View.VISIBLE
        binding.safeExposureTimeTextView.visibility = View.VISIBLE
    }

    fun clearSafeExposureTime() {
        binding.safeExposureTitle.visibility = View.GONE
        binding.safeExposureTimeTextView.visibility = View.GONE
    }
}