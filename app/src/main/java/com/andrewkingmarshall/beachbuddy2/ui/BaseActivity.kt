package com.andrewkingmarshall.beachbuddy2.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding>(val bindingFactory: (LayoutInflater) -> VB) : AppCompatActivity() {

    private var _binding: ViewBinding? = null
//    abstract val bindingInflater: (LayoutInflater) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingFactory(layoutInflater)
        setContentView(requireNotNull(_binding).root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}