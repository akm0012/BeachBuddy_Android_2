package com.andrewkingmarshall.beachbuddy2.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrewkingmarshall.beachbuddy2.database.model.DailyWeatherInfo
import com.andrewkingmarshall.beachbuddy2.databinding.CompoundViewDailyWeatherBinding
import com.andrewkingmarshall.beachbuddy2.ui.flexible.DailyWeatherFlexibleItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class DailyWeatherView : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var binding: CompoundViewDailyWeatherBinding =
        CompoundViewDailyWeatherBinding.inflate(LayoutInflater.from(context), this)

    private var adapter: FlexibleAdapter<IFlexible<*>>? = null

    fun setWeather(dailyWeatherInfoList: List<DailyWeatherInfo>) {

        val flexibleItemList = ArrayList<IFlexible<*>>()

        dailyWeatherInfoList.forEach {
            flexibleItemList.add(DailyWeatherFlexibleItem(it))
        }

        if (adapter == null) {

            adapter = FlexibleAdapter(flexibleItemList)

            binding.dailyWeatherRecyclerView.adapter = adapter
            binding.dailyWeatherRecyclerView.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        } else {
            adapter?.updateDataSet(flexibleItemList, true)
        }
    }
}