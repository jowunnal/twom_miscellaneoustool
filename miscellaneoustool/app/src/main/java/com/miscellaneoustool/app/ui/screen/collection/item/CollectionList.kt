package com.miscellaneoustool.app.ui.screen.collection.item

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miscellaneoustool.app.R
import com.miscellaneoustool.app.databinding.CollectionListBinding
import com.miscellaneoustool.app.domain.model.Category
import com.miscellaneoustool.app.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CollectionList : BaseFragment<CollectionListBinding>() {
    private val collectionViewModel: CollectionViewModel by viewModels()

    @Inject lateinit var collectionListAdapter: CollectionListAdapter
    private val navArgs: CollectionListArgs by navArgs()

    override fun getViewDataBinding(): CollectionListBinding = CollectionListBinding.inflate(layoutInflater)
    override var topBarVisibility: Boolean = true
    override var bottomNavigationBarVisibility: Boolean = false

    override fun initState() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            recyclerView.layoutManager = LinearLayoutManager(
                activity,
                RecyclerView.VERTICAL,
                false
            )
            recyclerView.adapter = collectionListAdapter
            dropListViewModel = dropListViewModel
        }
        collectionViewModel.getCollectionList(Category.findByStoredName(navArgs.category))
        addMenuProvider()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun subScribeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectionViewModel.uiState.collectLatest { uiState ->
                    collectionListAdapter.setItems(uiState.collectionList)
                    collectionListAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun addMenuProvider() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.book_option_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.icon_search -> {
                        val searchManager =
                            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
                        menuItem.actionView = SearchView(requireActivity()).apply {
                            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
                            maxWidth = Integer.MAX_VALUE
                            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                                override fun onQueryTextSubmit(query: String?): Boolean {
                                    collectionListAdapter.filter.filter(query)
                                    return false
                                }

                                override fun onQueryTextChange(newText: String?): Boolean {
                                    collectionListAdapter.filter.filter(newText)
                                    return false
                                }
                            })
                        }
                    }
                    android.R.id.home -> {
                        findNavController().popBackStack()
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}