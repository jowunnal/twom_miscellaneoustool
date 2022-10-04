package com.jinproject.twomillustratedbook.Fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jinproject.twomillustratedbook.Adapter.BookMainAdapter
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.BookBinding
import com.jinproject.twomillustratedbook.viewModel.BookViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Book : BindFragment<BookBinding>(R.layout.book,false) {
    val bookViewModel: BookViewModel by activityViewModels()
    val adapter :BookMainAdapter by lazy{BookMainAdapter()}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager=LinearLayoutManager(activity, RecyclerView.VERTICAL,false)
        binding.recyclerView.adapter=adapter
        binding.bookViewModel=bookViewModel

        bookViewModel.content(bookViewModel.dataItemType).observe(viewLifecycleOwner, Observer {
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
}