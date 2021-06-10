package com.andrewkingmarshall.beachbuddy2.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrewkingmarshall.beachbuddy2.database.model.HourlyWeatherInfo
import com.andrewkingmarshall.beachbuddy2.databinding.CompoundViewHourlyWeatherBinding
import com.andrewkingmarshall.beachbuddy2.ui.flexible.HourlyWeatherFlexibleItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class HourlyWeatherView : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var adapter: FlexibleAdapter<IFlexible<*>>? = null

    private var binding: CompoundViewHourlyWeatherBinding =
        CompoundViewHourlyWeatherBinding.inflate(LayoutInflater.from(context), this)

    fun setWeather(hourlyWeatherInfoList: List<HourlyWeatherInfo>) {

        val flexibleItemList = ArrayList<IFlexible<*>>()

        hourlyWeatherInfoList.forEach {
            flexibleItemList.add(HourlyWeatherFlexibleItem(it))
        }

        if (adapter == null) {

            adapter = FlexibleAdapter(flexibleItemList)

            binding.hourlyWeatherRecyclerView.adapter = adapter
            binding.hourlyWeatherRecyclerView.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        } else {
            adapter?.updateDataSet(flexibleItemList, true)
            if (flexibleItemList.isNotEmpty()) {
                binding.hourlyWeatherRecyclerView?.smoothScrollToPosition(0)
            }
        }
    }
}