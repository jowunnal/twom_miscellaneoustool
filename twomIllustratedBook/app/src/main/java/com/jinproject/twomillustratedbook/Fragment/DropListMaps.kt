package com.jinproject.twomillustratedbook.Fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinproject.twomillustratedbook.Adapter.DropListMapAdapter
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.DroplistmapBinding
import com.jinproject.twomillustratedbook.listener.OnItemClickListener
import com.jinproject.twomillustratedbook.viewModel.DropListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DropListMaps : BindFragment<DroplistmapBinding>(R.layout.droplistmap,true) {
    val dropListViewModel: DropListViewModel by activityViewModels()
    lateinit var adapter: DropListMapAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController= Navigation.findNavController(view)

        binding.mapRecyclerView.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        adapter= DropListMapAdapter(requireActivity())
        binding.mapRecyclerView.adapter=adapter
        binding.dropListViewModel=dropListViewModel

        dropListViewModel.droplistMaps.observe(viewLifecycleOwner, Observer {
            adapter.setItems(it)
            adapter.notifyDataSetChanged()
        })

        adapter.setItemClickListener(object:OnItemClickListener{
            override fun OnHomeItemClick(v: View, pos: Int) {
                dropListViewModel.data_map=adapter.getItem(pos)
                navController.navigate(R.id.action_dropListMaps_to_dropList)
            }
        })
    }
}