package com.andrewkingmarshall.beachbuddy2.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.database.AppDatabase
import com.andrewkingmarshall.beachbuddy2.database.model.BeachConditions
import com.andrewkingmarshall.beachbuddy2.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        setUpNavigation()
    }

    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        bottom_navigation_view.setupWithNavController(navController)

        // todo
//        viewModel.getNumberOfNotCompletedRequestedItems().observe(this, Observer {
//
//            if (it == null || it == 0) {
//                bottom_navigation_view.removeBadge(R.id.requestedItemsFragment)
//            } else {
//                bottom_navigation_view.getOrCreateBadge(R.id.requestedItemsFragment).number = it
//            }
//        })
    }
}