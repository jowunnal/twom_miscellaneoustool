package com.jinproject.features.collection.item

import android.app.SearchManager
import android.content.Context
import android.os.Parcelable
import androidx.activity.OnBackPressedCallback
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
import com.jinproject.features.collection.R
import com.jinproject.features.collection.databinding.CollectionListBinding
import com.jinproject.features.collection.item.item.CollectionMenu
import com.jinproject.features.collection.item.item.CollectionState
import com.jinproject.features.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CollectionList : BaseFragment<CollectionListBinding>() {
    private val collectionViewModel: CollectionViewModel by viewModels()

    @Inject
    lateinit var collectionListAdapter: CollectionListAdapter
    private val navArgs: CollectionListArgs by navArgs()

    private var scrollState: Parcelable? = null

    private val basicMenu by lazy {
        CollectionMenu(
            onCreate = { menu, menuInflater ->
                menuInflater.inflate(R.menu.book_option_menu, menu)
                menu.findItem(R.id.icon_setting).isVisible = true
                setMenuColorOnDarkMode(menu)
            },
            onClick = { menuItem ->
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

                    R.id.icon_setting -> {
                        val action =
                            CollectionListDirections.actionCollectionListToCollectionSetting(navArgs.category)
                        findNavController().navigate(action)
                    }

                    R.id.home -> findNavController().popBackStack()
                }
            }
        )
    }

    private val collectionMenu: CollectionMenu by lazy {
        CollectionMenu(
            onCreate = { menu, menuInflater ->
                menuInflater.inflate(R.menu.collection_customize_menu, menu)
                setMenuColorOnDarkMode(menu)
            },
            onClick = { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete -> {
                        scrollState = binding.recyclerView.layoutManager?.onSaveInstanceState()
                        collectionViewModel.deleteCollection(
                            collectionList = collectionListAdapter.getCheckedItems(),
                            category = com.jinproject.domain.model.Category.findByStoredName(
                                navArgs.category
                            )
                        )
                        removeMenuProvider(collectionMenu)
                        addMenuProvider(basicMenu)
                    }
                    R.id.home -> cancelDeleteItemMode()
                }
            }
        )
    }

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
                adapter = collectionListAdapter
            }
        }


        collectionListAdapter.apply {

            setOnClickListener(object : com.jinproject.features.core.listener.OnClickedListener {
                override fun setOnClickedListener(pos: Int) {
                    with(collectionListAdapter) {
                        if (getCollectionItem(pos).isCheck != CollectionState.CheckState.INVISIBLE) {
                            setItemCheck(pos)
                        }
                    }
                }
            })

            setOnLongClickListener(object :
                com.jinproject.features.core.listener.OnLongClickedListener {
                override fun setOnLongClickedListener(pos: Int) {
                    with(collectionListAdapter) {
                        if (getCollectionItem(pos).isCheck == CollectionState.CheckState.INVISIBLE) {
                            removeMenuProvider(basicMenu)
                            addMenuProvider(collectionMenu)

                            val scrollPos =
                                binding.recyclerView.layoutManager?.onSaveInstanceState()
                            setItemsEnableClick {
                                binding.recyclerView.layoutManager?.onRestoreInstanceState(scrollPos)
                            }
                        }
                    }

                }
            })
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    with(collectionListAdapter) {
                        if (getItemCheck()) {
                            cancelDeleteItemMode()
                        } else
                            findNavController().popBackStack()
                    }
                }
            })

        collectionViewModel.getCollectionList(
            com.jinproject.domain.model.Category.findByStoredName(
                navArgs.category
            )
        )
        addMenuProvider(basicMenu)
    }

    override fun subScribeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectionViewModel.uiState.collectLatest { uiState ->
                    collectionListAdapter.setItems(
                        items = uiState.collectionList,
                        onChanged = {
                            if (scrollState != null) {
                                binding.recyclerView.layoutManager?.onRestoreInstanceState(
                                    scrollState
                                )
                            }
                        }
                    )

                }
            }
        }
    }

    private fun cancelDeleteItemMode() {
        removeMenuProvider(collectionMenu)
        addMenuProvider(basicMenu)

        val scrollPos =
            binding.recyclerView.layoutManager?.onSaveInstanceState()
        collectionListAdapter.setItemsDisableClick {
            binding.recyclerView.layoutManager?.onRestoreInstanceState(
                scrollPos
            )
        }
    }

    private fun addMenuProvider(menuProvider: MenuProvider) {
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun removeMenuProvider(menuProvider: MenuProvider) {
        requireActivity().removeMenuProvider(menuProvider)
    }
}