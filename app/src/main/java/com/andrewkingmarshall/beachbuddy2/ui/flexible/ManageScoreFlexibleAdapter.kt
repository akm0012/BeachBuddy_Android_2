package com.andrewkingmarshall.beachbuddy2.ui.flexible

import com.andrewkingmarshall.beachbuddy2.database.model.Score
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class ManageScoreFlexibleAdapter : FlexibleAdapter<IFlexible<*>> {

    var listener: InteractionListener? = null
        private set

    interface InteractionListener {
        fun onScoreIncremented(score: Score)

        fun onScoreDecremented(score: Score)
    }

    constructor(items: List<IFlexible<*>>?) : super(items) {}

    constructor(items: List<IFlexible<*>>?, listener: InteractionListener?) : super(items, listener) {
        this.listener = listener
    }

    constructor(items: List<IFlexible<*>>?, listener: InteractionListener?, stableIds: Boolean) : super(
        items,
        listener,
        stableIds
    ) {
        this.listener = listener
    }
}
