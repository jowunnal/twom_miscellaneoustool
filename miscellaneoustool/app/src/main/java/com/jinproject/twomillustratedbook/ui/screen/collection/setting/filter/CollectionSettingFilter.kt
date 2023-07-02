package com.jinproject.twomillustratedbook.ui.screen.collection.setting.filter

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
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.CollectionListBinding
import com.jinproject.twomillustratedbook.ui.base.BaseFragment
import com.jinproject.twomillustratedbook.ui.base.CommonDialogFragment
import com.jinproject.twomillustratedbook.ui.listener.OnClickedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CollectionSettingFilter : BaseFragment<CollectionListBinding>() {

    private val collectionViewModel: CollectionSettingFilterViewModel by viewModels()

    @Inject
    lateinit var collectionSettingFilterAdapter: CollectionSettingFilterAdapter
    private val navArgs: CollectionSettingFilterArgs by navArgs()

    override fun getViewDataBinding(): CollectionListBinding =
        CollectionListBinding.inflate(layoutInflater)

    override var topBarVisibility: Boolean = true

    override var bottomNavigationBarVisibility: Boolean = false

    override fun initState() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            with(recyclerView) {
                layoutManager = LinearLayoutManager(
                    activity,
                    RecyclerView.VERTICAL,
                    false
                )
                adapter = collectionSettingFilterAdapter
                itemAnimator = null
            }
        }


        collectionSettingFilterAdapter.setOnClickListener(object : OnClickedListener {
            override fun setOnClickedListener(pos: Int) {
                CommonDialogFragment.show(
                    fragmentManager = requireActivity().supportFragmentManager,
                    title = "선택하신 도감을 되돌리시겠습니까?",
                    message = null,
                    positiveButtonText = "되돌리기",
                    listener = object: CommonDialogFragment.Listener() {
                        override fun onPositiveButtonClick(value: String) {
                            collectionViewModel.deleteFilter(
                                collectionSettingFilterAdapter.getItemIdOnPosition(pos).id, com.jinproject.domain.model.Category.findByStoredName(navArgs.category)
                            )
                        }
                    }
                )
            }
        })

        collectionViewModel.getCollectionList(com.jinproject.domain.model.Category.findByStoredName(navArgs.category))
        addMenuProvider()
    }

    override fun subScribeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectionViewModel.uiState.collectLatest { uiState ->
                    collectionSettingFilterAdapter.setItems(
                        items = uiState.collectionList
                    )
                }
            }
        }
    }

    private fun addMenuProvider() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.book_option_menu, menu)
                    setMenuColorOnDarkMode(menu)
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
                                        collectionSettingFilterAdapter.filter.filter(query)
                                        return false
                                    }

                                    override fun onQueryTextChange(newText: String?): Boolean {
                                        collectionSettingFilterAdapter.filter.filter(newText)
                                        return false
                                    }
                                })
                            }
                        }

                        android.R.id.home -> findNavController().popBackStack()
                    }
                    return true
                }

            }, viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }
}