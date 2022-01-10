package com.andrewkingmarshall.beachbuddy2.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.andrewkingmarshall.beachbuddy2.extensions.toast
import com.andrewkingmarshall.beachbuddy2.viewmodels.DarkModeViewModel
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseActivity<VB : ViewBinding>(val bindingFactory: (LayoutInflater) -> VB) : AppCompatActivity() {

    lateinit var darkModeViewModel: DarkModeViewModel

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingFactory(layoutInflater)
        setContentView(requireNotNull(_binding).root)

        darkModeViewModel = ViewModelProvider(this).get(DarkModeViewModel::class.java)

        darkModeViewModel.showToast.observe(this, { it.toast(this) })

        darkModeViewModel.isDarkModeActive.observe(this, {isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}