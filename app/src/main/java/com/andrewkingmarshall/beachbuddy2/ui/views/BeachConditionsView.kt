package com.andrewkingmarshall.beachbuddy2.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrewkingmarshall.beachbuddy2.databinding.CompoundViewBeachConditionsBinding
import com.andrewkingmarshall.beachbuddy2.enums.BeachConditionItemType
import com.andrewkingmarshall.beachbuddy2.ui.domainmodels.WeatherDM
import com.andrewkingmarshall.beachbuddy2.ui.flexible.BeachConditionFlexibleItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible


class BeachConditionsView : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var adapter: FlexibleAdapter<IFlexible<*>>? = null

    private var binding: CompoundViewBeachConditionsBinding =
        CompoundViewBeachConditionsBinding.inflate(LayoutInflater.from(context), this)

    var viewWidth: Int = 0

    var viewHeight: Int = 0

    var currentWeather: WeatherDM? = null

    init {
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)

                viewWidth = measuredWidth
                viewHeight = measuredHeight - 20 // Magic number to make it not scroll on my tablet

                currentWeather?.let { showViews(it) }
            }
        })
    }

    fun setWeather(currentWeather: WeatherDM) {
        this.currentWeather = currentWeather

        if (viewHeight != 0) {
            showViews(currentWeather)
        }
    }

    private fun showViews(currentWeather: WeatherDM) {

        val flexibleItemList = ArrayList<IFlexible<*>>()

        val rowHeight = viewHeight / enumValues<BeachConditionItemType>().size

        // Iterate through all the Enum Values
        enumValues<BeachConditionItemType>().forEach { beachConditionType ->
            flexibleItemList.add(
                BeachConditionFlexibleItem(
                    beachConditionType,
                    currentWeather,
                    rowHeight
                )
            )
        }

        if (adapter == null) {

            adapter = FlexibleAdapter(flexibleItemList)

            binding.beachConditionsRecyclerView.adapter = adapter
            binding.beachConditionsRecyclerView.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        } else {
            adapter?.updateDataSet(flexibleItemList, true)
        }
    }
}