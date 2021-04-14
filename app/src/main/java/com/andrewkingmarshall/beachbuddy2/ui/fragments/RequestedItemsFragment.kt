package com.andrewkingmarshall.beachbuddy2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.database.model.RequestedItem
import com.andrewkingmarshall.beachbuddy2.ui.domainmodels.RequestedItemsDM
import com.andrewkingmarshall.beachbuddy2.ui.flexible.RequestedItemEmptyStateFlexibleItem
import com.andrewkingmarshall.beachbuddy2.ui.flexible.RequestedItemFlexibleAdapter
import com.andrewkingmarshall.beachbuddy2.ui.flexible.RequestedItemFlexibleItem
import com.andrewkingmarshall.beachbuddy2.ui.views.CompletedItemsHeaderView
import com.andrewkingmarshall.beachbuddy2.viewmodels.DashboardViewModel
import com.andrewkingmarshall.beachbuddy2.viewmodels.RequestedItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.fragment_requested_items.*

@AndroidEntryPoint
class RequestedItemsFragment : Fragment() {

    lateinit var viewModel: RequestedItemViewModel

    private val adapter = RequestedItemFlexibleAdapter(
        null,
        object : RequestedItemFlexibleAdapter.InteractionListener {
            override fun onRequestedItemChecked(requestedItem: RequestedItem) {
                viewModel.onRequestedItemChecked(requestedItem)
            }

        },
        true
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(RequestedItemViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requested_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSwipeToRefresh()

        setUpRecyclerView()

        viewModel.requestedItemsDomainModel.observe(viewLifecycleOwner,
            { setBothRequestedAndCompletedItems(it) })
    }

    private fun setUpSwipeToRefresh() {
        swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            ContextCompat.getColor(requireContext(), R.color.colorAccent)
        )
        swipeRefreshLayout.setOnRefreshListener { viewModel.onPullToRefresh() }
        viewModel.showLoadingEvent.observe(
            viewLifecycleOwner, { swipeRefreshLayout.isRefreshing = it })
    }

    private fun setUpRecyclerView() {
        recyclerView.adapter = this.adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private fun setBothRequestedAndCompletedItems(requestedItemsDomainModel: RequestedItemsDM) {

        val requestedItems = requestedItemsDomainModel.nonCompletedItems
        val completedItems = requestedItemsDomainModel.completedItems

        val flexibleItemsList = ArrayList<IFlexible<*>>()

        requestedItems.forEach {
            flexibleItemsList.add(RequestedItemFlexibleItem(it, null))
        }

        val headerView = CompletedItemsHeaderView(requestedItems.isEmpty())

        completedItems.forEach {
            flexibleItemsList.add(RequestedItemFlexibleItem(it, headerView))
        }

        if (requestedItems.isEmpty() && completedItems.isEmpty()) {
            flexibleItemsList.add(RequestedItemEmptyStateFlexibleItem())
        }

        adapter.updateDataSet(flexibleItemsList)
    }
}