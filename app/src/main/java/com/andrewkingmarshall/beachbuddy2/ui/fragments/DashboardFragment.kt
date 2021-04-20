package com.andrewkingmarshall.beachbuddy2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.database.model.User
import com.andrewkingmarshall.beachbuddy2.extensions.toast
import com.andrewkingmarshall.beachbuddy2.ui.views.LeaderBoardView
import com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels.CurrentUvViewModel
import com.andrewkingmarshall.beachbuddy2.viewmodels.DashboardViewModel
import com.andrewkingmarshall.beachbuddy2.viewmodels.ItemAddedDialogViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.IllegalStateException

const val TIME_TO_SHOW_TIME_TO_BURN_MS: Long = 5 * 1000 // 5 sec

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    lateinit var viewModel: DashboardViewModel

    lateinit var itemViewModel: ItemAddedDialogViewModel

    lateinit var navController: NavController

    private var showSkinTypeJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(DashboardViewModel::class.java)
        itemViewModel =
            ViewModelProvider(requireActivity()).get(ItemAddedDialogViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        setUpLeaderboard()

        setUpBeachConditions()

        setUpCurrentWeatherView()

        setUpCurrentUvInfo()

        setUpHourlyWeatherView()

        setUpDailyWeatherView()

        setUpSunsetView()

        setUpSwipeToRefresh()

        itemViewModel.showNewItemAddedDialogEvent.observe(viewLifecycleOwner, {
            navController.navigate(R.id.action_dashboardFragment_to_itemAddedDialogFragment)
        })

        viewModel.dashboardRefreshErrorEvent.observe(
            viewLifecycleOwner,
            { it.message?.toast(requireContext()) })
        viewModel.showToast.observe(viewLifecycleOwner, { it.toast(requireContext()) })
    }

    private fun setUpLeaderboard() {
        viewModel.usersWithScores.observe(viewLifecycleOwner, Observer {

            if (it == null) {
                return@Observer
            }

            leaderBoardView.setUsers(it, object : LeaderBoardView.InteractionListener {
                override fun onSettingsClicked() {
                    navController.navigate(R.id.action_dashboardFragment_to_scoreManagementFragment)
                }

                override fun onDarkModeToggleClicked() {

                    when (AppCompatDelegate.getDefaultNightMode()) {

                        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
                        AppCompatDelegate.MODE_NIGHT_NO,
                        AppCompatDelegate.MODE_NIGHT_UNSPECIFIED -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            "Dark Mode On".toast(requireContext())
                        }

                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY,
                        AppCompatDelegate.MODE_NIGHT_YES -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            "Dark Mode Off".toast(requireContext())
                        }

                        else -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            "Dark Mode Off".toast(requireContext())
                        }
                    }
                }

                override fun onUserClicked(user: User) {
                    showSkinTypeJob?.cancel()
                    showSkinTypeJob = lifecycleScope.launch {
                        currentUvView.showSafeExposureTimeForSkinType(user.skinType)
                        delay(TIME_TO_SHOW_TIME_TO_BURN_MS)
                        currentUvView.clearSafeExposureTime()
                    }
                }
            })
        })
    }

    private fun setUpBeachConditions() {
        viewModel.weatherDomainModel.observe(viewLifecycleOwner, Observer {

            if (it == null) {
                return@Observer
            }

            beachConditionsView.setWeather(it)
        })
    }

    private fun setUpCurrentWeatherView() {
        viewModel.weatherDomainModel.observe(viewLifecycleOwner, Observer {

            if (it == null) {
                return@Observer
            }

            currentWeatherView.setWeather(it)
        })
    }

    private fun setUpCurrentUvInfo() {
        viewModel.weatherDomainModel.observe(viewLifecycleOwner, Observer {

            if (it == null) {
                return@Observer
            }

            currentUvView.setViewModel(CurrentUvViewModel(it))
        })
    }

    private fun setUpHourlyWeatherView() {
        viewModel.hourlyWeatherInfo.observe(viewLifecycleOwner, Observer {

            if (it == null) {
                return@Observer
            }

            hourlyWeatherView.setWeather(it)
        })
    }

    private fun setUpDailyWeatherView() {
        viewModel.dailyWeatherInfo.observe(viewLifecycleOwner, Observer {

            if (it == null) {
                return@Observer
            }

            dailyWeatherView.setWeather(it)
        })
    }

    private fun setUpSunsetView() {
        viewModel.sunsetInfo.observe(viewLifecycleOwner, Observer {

            if (it == null) {
                return@Observer
            }

            sunsetTimerView.setSunsetSunriseTimes(
                it.sunrise,
                it.sunset,
                it.sunriseNextDay,
                it.sunsetPrevDay
            )
        })

        viewModel.sunsetViewUpdateTimer.observe(viewLifecycleOwner, {
            sunsetTimerView.updateTimer()
        })
    }

    private fun setUpSwipeToRefresh() {
        dashboardSwipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            ContextCompat.getColor(requireContext(), R.color.colorAccent)
        )
        dashboardSwipeRefreshLayout.setOnRefreshListener { viewModel.onPullToRefresh() }
        viewModel.showLoadingEvent.observe(
            viewLifecycleOwner, { dashboardSwipeRefreshLayout.isRefreshing = it })
    }

}