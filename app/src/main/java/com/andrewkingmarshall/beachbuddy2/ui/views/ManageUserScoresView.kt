package com.andrewkingmarshall.beachbuddy2.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.database.model.Score
import com.andrewkingmarshall.beachbuddy2.database.model.UserWithScores
import com.andrewkingmarshall.beachbuddy2.databinding.CompoundViewManageUserScoresItemBinding
import com.andrewkingmarshall.beachbuddy2.ui.VerticalSpaceItemDecoration
import com.andrewkingmarshall.beachbuddy2.ui.flexible.ManageScoreFlexibleAdapter
import com.andrewkingmarshall.beachbuddy2.ui.flexible.ScoreTallyViewFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible

class ManageUserScoresView : ConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var binding: CompoundViewManageUserScoresItemBinding =
        CompoundViewManageUserScoresItemBinding.inflate(LayoutInflater.from(context), this)

    private var adapter: ManageScoreFlexibleAdapter? = null

    var listener: InteractionListener? = null

    interface InteractionListener {
        fun onScoreIncremented(score: Score)

        fun onScoreDecremented(score: Score)
    }

    private fun resetView() {

        binding.nameTextView.text = ""
        binding.profileImageView.background = null
    }

    fun setUser(userWithScores: UserWithScores, listener: InteractionListener) {

        val user = userWithScores.user
        val scores = userWithScores.scores

        resetView()

        binding.nameTextView.text = user.firstName

        val profilePhoto = "${context.getString(R.string.base_endpoint)}${user.photoUrl}"
        loadCircleProfilePhoto(context, profilePhoto, binding.profileImageView)

        val flexibleItemList = ArrayList<IFlexible<*>>()

        val sortedScores = scores.sortedBy { score -> score.name }

        for (score in sortedScores) {
            flexibleItemList.add(ScoreTallyViewFlexibleItem(score))
        }

        if (adapter == null) {
            adapter = ManageScoreFlexibleAdapter(
                flexibleItemList,
                object : ManageScoreFlexibleAdapter.InteractionListener {
                    override fun onScoreIncremented(score: Score) {
                        listener.onScoreIncremented(score)
                    }

                    override fun onScoreDecremented(score: Score) {
                        listener.onScoreDecremented(score)
                    }
                },
                true
            )

            binding.userScoreRecyclerView.adapter = adapter
            binding.userScoreRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            // Only add decorations once
            if (binding.userScoreRecyclerView.itemDecorationCount == 0) {
                binding.userScoreRecyclerView.addItemDecoration(
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