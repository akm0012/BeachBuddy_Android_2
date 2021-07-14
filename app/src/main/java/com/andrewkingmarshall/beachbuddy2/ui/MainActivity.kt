package com.andrewkingmarshall.beachbuddy2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.databinding.ActivityMainBinding
import com.andrewkingmarshall.beachbuddy2.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        setUpNavigation()
    }

    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        viewModel.notCompletedItemCount.observe(this, {

            if (it == null || it == 0) {
                binding.bottomNavigationView.removeBadge(R.id.requestedItemsFragment)
            } else {
                binding.bottomNavigationView.getOrCreateBadge(R.id.requestedItemsFragment).number =
                    it
            }
        })
    }
}