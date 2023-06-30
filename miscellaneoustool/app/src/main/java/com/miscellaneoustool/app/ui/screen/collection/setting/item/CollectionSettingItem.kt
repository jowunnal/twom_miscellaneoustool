package com.miscellaneoustool.app.ui.screen.collection.setting.item

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.miscellaneoustool.app.R
import com.miscellaneoustool.app.databinding.CollectionSettingItemListBinding
import com.miscellaneoustool.app.ui.base.BaseFragment
import com.miscellaneoustool.app.ui.base.CommonDialogFragment
import com.miscellaneoustool.app.ui.listener.OnClickedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CollectionSettingItem : BaseFragment<CollectionSettingItemListBinding>() {

    private val viewModel: CollectionSettingITemViewModel by viewModels()
    @Inject
    lateinit var listAdapter: CollectionSettingItemAdapter

    override fun getViewDataBinding(): CollectionSettingItemListBinding =
        CollectionSettingItemListBinding.inflate(layoutInflater)

    override var topBarVisibility: Boolean = true

    override var bottomNavigationBarVisibility: Boolean = false

    override fun initState() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            itemList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            itemList.adapter = listAdapter
        }

        listAdapter.setOnClickListener(object : OnClickedListener {
            override fun setOnClickedListener(pos: Int) {
                CommonDialogFragment.show(
                    fragmentManager = requireActivity().supportFragmentManager,
                    title = "${listAdapter.getItemInfo(pos).name} 의 가격을 입력해주세요.",
                    message = "",
                    positiveButtonText = "변경",
                    listener = object : CommonDialogFragment.Listener() {
                        override fun onPositiveButtonClick(value: String) {
                            if (value.isNotBlank())
                                viewModel.updateItemPrice(
                                    name = listAdapter.getItemInfo(pos).name,
                                    price = value.toInt()
                                )
                        }
                    }
                )
            }
        })

        provideMenu()
    }

    override fun subScribeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    val scrollPos = binding.itemList.layoutManager?.onSaveInstanceState()

                    listAdapter.setItems(state.items) {
                        binding.itemList.layoutManager?.onRestoreInstanceState(scrollPos)
                    }
                }
            }
        }
    }

    private fun provideMenu() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.book_option_menu, menu)
                    setMenuColorOnDarkMode(menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    when (menuItem.itemId) {
                        android.R.id.home -> findNavController().popBackStack()
                        R.id.icon_search -> {
                            val searchManager =
                                requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

                            menuItem.actionView = SearchView(requireActivity()).apply {
                                setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
                                maxWidth = Integer.MAX_VALUE
                                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                                    override fun onQueryTextSubmit(query: String?): Boolean {
                                        listAdapter.filter.filter(query)
                                        return false
                                    }

                                    override fun onQueryTextChange(newText: String?): Boolean {
                                        listAdapter.filter.filter(newText)
                                        return false
                                    }
                                })
                            }
                        }
                    }
                    return true
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }
}