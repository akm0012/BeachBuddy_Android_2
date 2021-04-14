package com.andrewkingmarshall.beachbuddy2.ui.flexible

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.enums.BeachConditionItemType
import com.andrewkingmarshall.beachbuddy2.ui.domainmodels.WeatherDM
import com.andrewkingmarshall.beachbuddy2.ui.views.BeachConditionItemView
import com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels.BeachConditionItemViewModel
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

class BeachConditionFlexibleItem(
    private var itemType: BeachConditionItemType,
    private var currentWeather: WeatherDM,
    private var desiredHeight: Int
) :
    AbstractFlexibleItem<BeachConditionFlexibleItem.BeachConditionItemViewHolder>() {

    override fun equals(other: Any?): Boolean {
        return if (other is BeachConditionFlexibleItem) {
            this.itemType == other.itemType
        } else false
    }

    override fun hashCode(): Int {
        return currentWeather.hashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.container_beach_condition_item
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

        holder?.beachConditionItemView?.height = desiredHeight
        holder?.beachConditionItemView?.setViewModel(BeachConditionItemViewModel(itemType, currentWeather))

        // Fixme: Assuming Updated at is always the last item. Prob a better way to check this
        if (itemType == BeachConditionItemType.TIME_UPDATED) {
            holder?.beachConditionItemView?.setBottomDividerVisibility(false)
        } else {
            holder?.beachConditionItemView?.setBottomDividerVisibility(true)
        }
    }

    inner class BeachConditionItemViewHolder(view: View?, adapter: FlexibleAdapter<*>) :
        FlexibleViewHolder(view, adapter) {

        var beachConditionItemView: BeachConditionItemView = view as BeachConditionItemView
    }
}