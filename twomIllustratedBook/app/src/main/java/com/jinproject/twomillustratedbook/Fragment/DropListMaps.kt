package com.jinproject.twomillustratedbook.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinproject.twomillustratedbook.Adapter.DropListMapAdapter
import com.jinproject.twomillustratedbook.Database.BookApplication
import com.jinproject.twomillustratedbook.Item.BookViewModel
import com.jinproject.twomillustratedbook.Item.BookViewModelFactory
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.DroplistmapBinding
import com.jinproject.twomillustratedbook.listener.OnItemClickListener

class DropListMaps : Fragment() {
    var _binding:DroplistmapBinding ?=null
    val binding get()=_binding!!
    val model:BookViewModel by activityViewModels(){BookViewModelFactory((activity?.application as BookApplication).repository)}
    lateinit var adapter: DropListMapAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=DroplistmapBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController= Navigation.findNavController(view)

        binding.mapRecyclerView.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        adapter= DropListMapAdapter(requireActivity())
        binding.mapRecyclerView.adapter=adapter

        model.droplistMaps.observe(viewLifecycleOwner, Observer {
            adapter.setItems(it)
            adapter.notifyDataSetChanged()
        })

        adapter.setItemClickListener(object:OnItemClickListener{
            override fun OnHomeItemClick(v: View, pos: Int) {
                model.data_map=adapter.getItem(pos)
                navController.navigate(R.id.action_dropListMaps_to_dropList)
            }
        })
    }

    override fun onDestroyView() {
        _binding=null
        super.onDestroyView()
    }
}