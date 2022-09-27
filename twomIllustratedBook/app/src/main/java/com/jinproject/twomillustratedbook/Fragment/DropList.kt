package com.jinproject.twomillustratedbook.Fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinproject.twomillustratedbook.Adapter.DropListAdapter
import com.jinproject.twomillustratedbook.Database.BookApplication
import com.jinproject.twomillustratedbook.Item.BookViewModel
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.DropBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DropList : Fragment() {
    var _binding : DropBinding?=null
    val binding get()=_binding!!
    val model:BookViewModel by viewModels()
    lateinit var dropListAdapter: DropListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=DropBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding.DropRecyclerView.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        dropListAdapter= DropListAdapter(requireActivity())
        binding.DropRecyclerView.adapter=dropListAdapter

        model.inputData(model.data_map).observe(viewLifecycleOwner, Observer {
            dropListAdapter.setItems(it)
            dropListAdapter.notifyDataSetChanged()
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.book_option_menu,menu)
        val searchManager=requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView=menu.findItem(R.id.icon_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.maxWidth=Integer.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                dropListAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                dropListAdapter.filter.filter(newText)
                return false
            }

        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        _binding=null
        super.onDestroyView()
    }
}