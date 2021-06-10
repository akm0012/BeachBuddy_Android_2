package com.andrewkingmarshall.beachbuddy2.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.database.model.User
import com.andrewkingmarshall.beachbuddy2.database.model.UserWithScores
import com.andrewkingmarshall.beachbuddy2.databinding.CompoundViewLeaderBoardBinding
import com.andrewkingmarshall.beachbuddy2.ui.VerticalSpaceItemDecoration
import com.andrewkingmarshall.beachbuddy2.ui.flexible.LeaderBoardFlexibleAdapter
import com.andrewkingmarshall.beachbuddy2.ui.flexible.LeaderBoardItemFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible

class LeaderBoardView : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var binding: CompoundViewLeaderBoardBinding =
        CompoundViewLeaderBoardBinding.inflate(LayoutInflater.from(context), this)

    private var adapter: LeaderBoardFlexibleAdapter? = null

    interface InteractionListener {
        fun onSettingsClicked()

        fun onDarkModeToggleClicked()

        fun onUserClicked(user: User)
    }

    init {
        if (resources.getBoolean(R.bool.isTv)) {
            binding.settingImageView.visibility = GONE
        }
    }

    fun setUsers(userList: List<UserWithScores>, listener: InteractionListener? = null) {

        if (listener != null) {
            binding.settingImageView.setOnClickListener { listener.onSettingsClicked() }
            binding.darModeToggleImageView.setOnClickListener { listener.onDarkModeToggleClicked() }
        }

        val flexibleItemList = ArrayList<IFlexible<*>>()

        for (user in userList) {
            flexibleItemList.add(LeaderBoardItemFlexibleItem(user))
        }

        if (adapter == null) {
            adapter = LeaderBoardFlexibleAdapter(
                flexibleItemList,
                object : LeaderBoardFlexibleAdapter.InteractionListener {
                    override fun onSettingsClicked() {
                        listener?.onSettingsClicked()
                    }

                    override fun onLeaderBoardItemClicked(user: User) {
                        listener?.onUserClicked(user)
                    }
                },
                true
            )

            binding.leaderBoardRecyclerView.adapter = adapter
            binding.leaderBoardRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            // Only add decorations once
            if (binding.leaderBoardRecyclerView.itemDecorationCount == 0) {
                binding.leaderBoardRecyclerView.addItemDecoration(
                    VerticalSpaceItemDecoration(
                        resources.getDimension(R.dimen.leader_board_item_space).toInt(), true
                    )
                )
            }

        } else {
            adapter?.updateDataSet(flexibleItemList, true)
        }
    }
}