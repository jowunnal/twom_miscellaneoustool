package com.miscellaneoustool.app.ui.screen.droplist.map

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.miscellaneoustool.app.databinding.DroplistmapBinding
import com.miscellaneoustool.app.ui.base.BaseFragment
import com.miscellaneoustool.app.ui.listener.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DropListMaps : BaseFragment<DroplistmapBinding>() {
    private val dropListMapViewModel: DropListMapViewModel by viewModels()
    @Inject lateinit var dropListMapAdapter: DropListMapAdapter

    override fun getViewDataBinding(): DroplistmapBinding =
        DroplistmapBinding.inflate(layoutInflater)

    override var topBarVisibility: Boolean = false
    override var bottomNavigationBarVisibility: Boolean = true

    override fun initState() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            mapRecyclerView.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            mapRecyclerView.adapter = dropListMapAdapter
            dropListViewModel = dropListViewModel
        }
        dropListMapAdapter.setItemClickListener(object : OnItemClickListener {
            override fun OnHomeItemClick(v: View, pos: Int) {
                val map = dropListMapAdapter.getItem(pos)
                val action = DropListMapsDirections.actionDropListMapsToDropList(map)
                findNavController().navigate(action)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun subScribeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            dropListMapViewModel.mapState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle,Lifecycle.State.STARTED)
                .collectLatest { mapList ->
                    dropListMapAdapter.setItems(mapList)
                    dropListMapAdapter.notifyDataSetChanged()
            }
        }
    }
}