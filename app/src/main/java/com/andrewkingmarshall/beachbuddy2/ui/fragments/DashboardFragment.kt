package com.andrewkingmarshall.beachbuddy2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.database.model.User
import com.andrewkingmarshall.beachbuddy2.extensions.toast
import com.andrewkingmarshall.beachbuddy2.ui.views.LeaderBoardView
import com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels.CurrentUvViewModel
import com.andrewkingmarshall.beachbuddy2.viewmodels.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dashboard.*
import timber.log.Timber

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    lateinit var viewModel: DashboardViewModel

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(DashboardViewModel::class.java)

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

        viewModel.weatherDomainModel.observe(viewLifecycleOwner, Observer {
            Timber.d("WeatherDM: $it")
        })

        viewModel.showToast.observe(viewLifecycleOwner, { it.toast(requireContext()) })
    }

    private fun setUpLeaderboard() {
        viewModel.usersWithScores.observe(viewLifecycleOwner, Observer {
            Timber.i("setUpLeaderboard: ${it.toString().subSequence(0, 50)}")

            if (it == null) {
                return@Observer
            }

            leaderBoardView.setUsers(it, object : LeaderBoardView.InteractionListener {
                override fun onSettingsClicked() {
                    navController.navigate(R.id.action_dashboardFragment_to_scoreManagementFragment)
                }

                override fun onUserClicked(user: User) {
                    currentUvView.showSafeExposureTimeForSkinType(user.skinType)
                }
            })
        })
    }

    private fun setUpBeachConditions() {
        viewModel.weatherDomainModel.observe(viewLifecycleOwner, Observer {
            Timber.i("setUpBeachConditions: ${it.toString().subSequence(0, 50)}")

            if (it == null) {
                return@Observer
            }

            beachConditionsView.setWeather(it)
        })
    }

    private fun setUpCurrentWeatherView() {
        viewModel.weatherDomainModel.observe(viewLifecycleOwner, Observer {
            Timber.i("setUpCurrentWeatherView: ${it.toString().subSequence(0, 50)}")

            if (it == null) {
                return@Observer
            }

            currentWeatherView.setWeather(it)
        })
    }

    private fun setUpCurrentUvInfo() {
        viewModel.weatherDomainModel.observe(viewLifecycleOwner, Observer {
            Timber.i("setUpCurrentUvInfo: ${it.toString().subSequence(0, 50)}")

            if (it == null) {
                return@Observer
            }

            currentUvView.setViewModel(CurrentUvViewModel(it))
        })
    }

    private fun setUpHourlyWeatherView() {
        viewModel.hourlyWeatherInfo.observe(viewLifecycleOwner, Observer {
            Timber.i("setUpHourlyWeatherView: ${it.toString().subSequence(0, 50)}")

            if (it == null) {
                return@Observer
            }

            hourlyWeatherView.setWeather(it)
        })
    }

    private fun setUpDailyWeatherView() {
        viewModel.dailyWeatherInfo.observe(viewLifecycleOwner, Observer {
            Timber.i("setUpDailyWeatherView: ${it.toString().subSequence(0, 50)}")

            if (it == null) {
                return@Observer
            }

            dailyWeatherView.setWeather(it)
        })
    }

    private fun setUpSunsetView() {
        viewModel.sunsetInfo.observe(viewLifecycleOwner, Observer {
            Timber.i("setUpSunsetView: ${it.toString().subSequence(0, 50)}")

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