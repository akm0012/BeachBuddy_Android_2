package com.andrewkingmarshall.beachbuddy2.ui.flexible

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.database.model.User
import com.andrewkingmarshall.beachbuddy2.database.model.UserWithScores
import com.andrewkingmarshall.beachbuddy2.ui.views.LeaderBoardItemView
import com.andrewkingmarshall.beachbuddy2.ui.views.viewmodels.LeaderBoardItemViewModel
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

class LeaderBoardItemFlexibleItem(val userWithScores: UserWithScores) :
    AbstractFlexibleItem<LeaderBoardItemFlexibleItem.LeaderBoardItemViewHolder>() {

    private val user = userWithScores.user

    override fun equals(other: Any?): Boolean {
        return if (other is LeaderBoardItemFlexibleItem) {
            this.user.userId == other.user.userId
        } else false
    }

    override fun hashCode(): Int {
        return user.hashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.container_leader_board_item_view
    }

    override fun createViewHolder(
        view: View?, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?
    ): LeaderBoardItemViewHolder {
        return LeaderBoardItemViewHolder(view, adapter as LeaderBoardFlexibleAdapter)

    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
        holder: LeaderBoardItemViewHolder?,
        position: Int,
        callbacks: MutableList<Any>?
    ) {

        holder?.leaderBoardItemView?.setViewModel(LeaderBoardItemViewModel(
            holder.leaderBoardItemView.context,
            userWithScores))

        holder?.leaderBoardItemView?.setOnClickListener {
            holder.listener?.onLeaderBoardItemClicked(user)
        }
    }

    inner class LeaderBoardItemViewHolder(view: View?, adapter: LeaderBoardFlexibleAdapter) :
        FlexibleViewHolder(view, adapter) {

        var leaderBoardItemView: LeaderBoardItemView = view as LeaderBoardItemView

        var listener: LeaderBoardFlexibleAdapter.InteractionListener? = adapter.listener
    }
}