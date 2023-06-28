package com.miscellaneoustool.app.ui.screen.collection.item

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
import com.miscellaneoustool.app.R
import com.miscellaneoustool.app.databinding.CollectionListBinding
import com.miscellaneoustool.app.domain.model.Category
import com.miscellaneoustool.app.ui.base.BaseFragment
import com.miscellaneoustool.app.ui.listener.OnClickedListener
import com.miscellaneoustool.app.ui.listener.OnLongClickedListener
import com.miscellaneoustool.app.ui.screen.collection.item.item.CollectionMenu
import com.miscellaneoustool.app.ui.screen.collection.item.item.CollectionState
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
            onCreate = {menu, menuInflater -> menuInflater.inflate(R.menu.book_option_menu, menu) },
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

                    android.R.id.home -> {
                        findNavController().popBackStack()
                    }
                }
            }
        )
    }

    private val collectionMenu: CollectionMenu by lazy {
        CollectionMenu(
            onCreate = { menu, menuInflater ->
                menuInflater.inflate(R.menu.collection_customize_menu, menu)
            },
            onClick = { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete -> {
                        scrollState = binding.recyclerView.layoutManager?.onSaveInstanceState()
                        collectionViewModel.deleteCollection(
                            collectionList = collectionListAdapter.getCheckedItems(),
                            category = Category.findByStoredName(navArgs.category)
                        )
                        removeMenuProvider(collectionMenu)
                        addMenuProvider(basicMenu)
                    }
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
                itemAnimator = null
            }
            dropListViewModel = dropListViewModel
        }


        collectionListAdapter.apply {

            setOnClickListener(object : OnClickedListener {
                override fun setOnClickedListener(pos: Int) {
                    with(collectionListAdapter) {
                        if (getCollectionItem(pos).isCheck != CollectionState.CheckState.INVISIBLE) {
                            setItemCheck(pos)
                        }
                    }
                }
            })

            setOnLongClickListener(object : OnLongClickedListener {
                override fun setOnLongClickedListener(pos: Int) {
                    removeMenuProvider(basicMenu)
                    addMenuProvider(collectionMenu)

                    with(collectionListAdapter) {
                        if (getCollectionItem(pos).isCheck == CollectionState.CheckState.INVISIBLE) {
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
                            removeMenuProvider(collectionMenu)
                            addMenuProvider(basicMenu)

                            val scrollPos =
                                binding.recyclerView.layoutManager?.onSaveInstanceState()
                            setItemsDisableClick {
                                binding.recyclerView.layoutManager?.onRestoreInstanceState(
                                    scrollPos
                                )
                            }
                        } else
                            findNavController().popBackStack()
                    }
                }
            })

        collectionViewModel.getCollectionList(Category.findByStoredName(navArgs.category))
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

    private fun addMenuProvider(menuProvider: MenuProvider) {
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun removeMenuProvider(menuProvider: MenuProvider) {
        requireActivity().removeMenuProvider(menuProvider)
    }
}