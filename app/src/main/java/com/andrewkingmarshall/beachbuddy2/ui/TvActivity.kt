package com.andrewkingmarshall.beachbuddy2.ui

import android.os.Bundle
import android.view.WindowManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.databinding.ActivityMainBinding
import com.andrewkingmarshall.beachbuddy2.databinding.ActivityTvBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvActivity : BaseActivity<ActivityTvBinding>(ActivityTvBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}