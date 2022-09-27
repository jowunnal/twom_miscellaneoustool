package com.jinproject.twomillustratedbook.Fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jinproject.twomillustratedbook.Adapter.BookMainAdapter
import com.jinproject.twomillustratedbook.Database.BookApplication
import com.jinproject.twomillustratedbook.Item.BookViewModel
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.Repository.BookRepositoryImpl
import com.jinproject.twomillustratedbook.databinding.BookBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Book : Fragment() {
    var _binding : BookBinding ?=null
    val binding get() = _binding!!
    val model:BookViewModel by viewModels()
    lateinit var adapter :BookMainAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=BookBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager=LinearLayoutManager(activity, RecyclerView.VERTICAL,false)
        adapter= BookMainAdapter()
        binding.recyclerView.adapter=adapter

        model.content(model.dataItemType).observe(viewLifecycleOwner, Observer {
            adapter.setContentItem(it)
            adapter.notifyDataSetChanged()
        })

        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.book_option_menu, menu)
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView=menu.findItem(R.id.icon_search).actionView as androidx.appcompat.widget.SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.maxWidth=Integer.MAX_VALUE
        searchView.inputType=InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
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