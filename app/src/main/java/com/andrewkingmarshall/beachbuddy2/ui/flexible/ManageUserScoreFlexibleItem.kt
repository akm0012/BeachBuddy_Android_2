package com.andrewkingmarshall.beachbuddy2.ui.flexible

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.database.model.Score
import com.andrewkingmarshall.beachbuddy2.database.model.UserWithScores
import com.andrewkingmarshall.beachbuddy2.ui.views.ManageUserScoresView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

class ManageUserScoreFlexibleItem(private val userWithScores: UserWithScores) :
    AbstractFlexibleItem<ManageUserScoreFlexibleItem.ManageUserScoreViewHolder>() {

    private val user = userWithScores.user

    override fun equals(other: Any?): Boolean {
        return if (other is ManageUserScoreFlexibleItem) {
            this.user.userId == other.user.userId
        } else false
    }

    override fun hashCode(): Int {
        return user.hashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.container_manage_user_score
    }

    override fun createViewHolder(
        view: View?, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ): ManageUserScoreViewHolder {
        return ManageUserScoreViewHolder(view, adapter as ManageScoreFlexibleAdapter)

    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
        holder: ManageUserScoreViewHolder?,
        position: Int,
        callbacks: MutableList<Any>?
    ) {

        holder?.manageUserScoresView?.setUser(userWithScores, object : ManageUserScoresView.InteractionListener {
            override fun onScoreIncremented(score: Score) {
                holder.listener?.onScoreIncremented(score)
            }

            override fun onScoreDecremented(score: Score) {
                holder.listener?.onScoreDecremented(score)
            }
        })
    }

    inner class ManageUserScoreViewHolder(view: View?, adapter: ManageScoreFlexibleAdapter) :
        FlexibleViewHolder(view, adapter) {

        var manageUserScoresView: ManageUserScoresView = view as ManageUserScoresView

        var listener: ManageScoreFlexibleAdapter.InteractionListener? = adapter.listener
    }
}