package com.jinproject.features.droplist.monster

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinproject.features.core.BaseFragment
import com.jinproject.features.droplist.databinding.DropBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DropListMonster : BaseFragment<DropBinding>() {
    private val dropListViewModel: DropListViewModel by viewModels()
    val dropListAdapter: DropListAdapter by lazy { DropListAdapter(
        context = requireActivity(),
        getMonsterItem = dropListViewModel::itemListToSingleString
    ) }
    private val navArgs: DropListMonsterArgs by navArgs()

    override fun getViewDataBinding(): DropBinding = DropBinding.inflate(layoutInflater)
    override var topBarVisibility: Boolean = true
    override var bottomNavigationBarVisibility: Boolean = false

    override fun initState() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            DropRecyclerView.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            DropRecyclerView.adapter = dropListAdapter
        }
        dropListViewModel.getMonsterListFromMap(navArgs.mapName)
        addMenuProvider()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun subScribeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dropListViewModel.uiState.collectLatest { state ->
                    dropListAdapter.setItems(state.monster)
                    dropListAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun addMenuProvider() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(com.jinproject.design_ui.R.menu.book_option_menu, menu)
                setMenuColorOnDarkMode(menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    com.jinproject.design_ui.R.id.icon_search -> {
                        val searchManager =
                            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
                        menuItem.actionView = androidx.appcompat.widget.SearchView(requireActivity())
                            .apply {
                            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
                            maxWidth = Integer.MAX_VALUE
                            setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                                override fun onQueryTextSubmit(query: String?): Boolean {
                                    dropListAdapter.filter.filter(query)
                                    return false
                                }

                                override fun onQueryTextChange(newText: String?): Boolean {
                                    dropListAdapter.filter.filter(newText)
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
        }, viewLifecycleOwner, Lifecycle.State.CREATED)
    }
}