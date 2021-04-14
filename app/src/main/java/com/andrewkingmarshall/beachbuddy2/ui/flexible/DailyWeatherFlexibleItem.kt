package com.andrewkingmarshall.beachbuddy2.ui.flexible

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.database.model.DailyWeatherInfo
import com.andrewkingmarshall.beachbuddy2.ui.views.DailyWeatherItemView
import com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels.DailyWeatherItemViewModel
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

class DailyWeatherFlexibleItem(
    private var dailyWeather: DailyWeatherInfo
) :
    AbstractFlexibleItem<DailyWeatherFlexibleItem.BeachConditionItemViewHolder>() {

    override fun equals(other: Any?): Boolean {
        return if (other is DailyWeatherFlexibleItem) {
            this.dailyWeather.id == other.dailyWeather.id
        } else false
    }

    override fun hashCode(): Int {
        return dailyWeather.hashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.container_daily_weather_item
    }

    override fun createViewHolder(
        view: View?, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ): BeachConditionItemViewHolder {
        return BeachConditionItemViewHolder(view, adapter as FlexibleAdapter<*>)

    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
        holder: BeachConditionItemViewHolder?,
        position: Int,
        callbacks: MutableList<Any>?
    ) {

        holder?.dailyWeatherItemView?.setViewModel(
            DailyWeatherItemViewModel(
                holder.dailyWeatherItemView.context,
                dailyWeather
            )
        )
    }

    inner class BeachConditionItemViewHolder(view: View?, adapter: FlexibleAdapter<*>) :
        FlexibleViewHolder(view, adapter) {

        var dailyWeatherItemView: DailyWeatherItemView = view as DailyWeatherItemView
    }
}