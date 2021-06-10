package com.andrewkingmarshall.beachbuddy2.ui.fragments

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.database.model.Score
import com.andrewkingmarshall.beachbuddy2.database.model.UserWithScores
import com.andrewkingmarshall.beachbuddy2.extensions.toast
import com.andrewkingmarshall.beachbuddy2.ui.ItemOffsetDecoration
import com.andrewkingmarshall.beachbuddy2.ui.flexible.ManageScoreFlexibleAdapter
import com.andrewkingmarshall.beachbuddy2.ui.flexible.ManageUserScoreFlexibleItem
import com.andrewkingmarshall.beachbuddy2.viewmodels.ScoreManagementViewModel
import dagger.hilt.android.AndroidEntryPoint
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.fragment_score_management.*

@AndroidEntryPoint
class ScoreManagementFragment : BaseFragment() {

    lateinit var viewModel: ScoreManagementViewModel

    private var adapter: ManageScoreFlexibleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ScoreManagementViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score_management, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showToast.observe(viewLifecycleOwner, { it.toast(requireContext()) })

        viewModel.usersWithScores.observe(viewLifecycleOwner, { setScoreBoard(it) })

        addGameButton.setOnClickListener {
            MaterialDialog(requireContext()).show {
                input(
                    hint = "Game Name",
                    waitForPositiveButton = true,
                    maxLength = 15,
                    inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
                ) { dialog, text ->
                    val inputField = dialog.getInputField()
                    val isValid = text.isNotEmpty() && text.length < 15

                    inputField.error = if (isValid) null else "Invalid"
                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)

                    viewModel.onAddNewGame(inputField.text.toString())
                }

                title(text = "Add a New Game")
                positiveButton(text = "Add Game")
                negativeButton(text = "Cancel")
            }
        }
    }

    private fun setScoreBoard(users: List<UserWithScores>) {

        val flexibleItemList = ArrayList<IFlexible<*>>()

        for (user in users) {
            flexibleItemList.add(ManageUserScoreFlexibleItem(user))
        }

        if (adapter == null) {
            adapter = ManageScoreFlexibleAdapter(
                flexibleItemList,
                object : ManageScoreFlexibleAdapter.InteractionListener {
                    override fun onScoreIncremented(score: Score) {
                        viewModel.onScoreIncremented(score)
                    }

                    override fun onScoreDecremented(score: Score) {
                        viewModel.onScoreDecremented(score)
                    }
                },
                true
            )

            manageUserScoreRecyclerView.adapter = adapter
            manageUserScoreRecyclerView.layoutManager = GridLayoutManager(context, 3)

            if (manageUserScoreRecyclerView.itemDecorationCount == 0) {
                val itemDecoration =
                    ItemOffsetDecoration(requireContext(), R.dimen.grid_item_offset)
                manageUserScoreRecyclerView.addItemDecoration(itemDecoration)
            }

        } else {
            adapter?.updateDataSet(flexibleItemList, true)
        }

    }

}