package com.jinproject.twomillustratedbook.ui.screen.droplist.map

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinproject.twomillustratedbook.databinding.DroplistmapBinding
import com.jinproject.twomillustratedbook.listener.OnItemClickListener
import com.jinproject.twomillustratedbook.ui.base.BaseFragment
import com.jinproject.twomillustratedbook.ui.screen.droplist.monster.DropListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DropListMaps : BaseFragment<DroplistmapBinding>() {
    private val dropListMapViewModel: DropListMapViewModel by viewModels()
    @Inject lateinit var adapter: DropListMapAdapter

    override fun getViewDataBinding(): DroplistmapBinding =
        DroplistmapBinding.inflate(layoutInflater)

    override var topBarVisibility: Boolean = false
    override var bottomNavigationBarVisibility: Boolean = true

    override fun initState() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            mapRecyclerView.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            mapRecyclerView.adapter = adapter
            dropListViewModel = dropListViewModel
        }
        adapter.setItemClickListener(object : OnItemClickListener {
            override fun OnHomeItemClick(v: View, pos: Int) {
                val map = adapter.getItem(pos)
                val action = DropListMapsDirections.actionDropListMapsToDropList(map)
                findNavController().navigate(action)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun subScribeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dropListMapViewModel.mapState.collectLatest { mapList ->
                    adapter.setItems(mapList)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}