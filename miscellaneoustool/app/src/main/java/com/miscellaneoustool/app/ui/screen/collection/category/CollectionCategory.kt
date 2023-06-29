package com.miscellaneoustool.app.ui.screen.collection.category

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.miscellaneoustool.app.databinding.CollectionCategoryBinding
import com.miscellaneoustool.app.domain.model.Category
import com.miscellaneoustool.app.ui.base.BaseFragment
import com.miscellaneoustool.app.ui.listener.OnClickedListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CollectionCategory : BaseFragment<CollectionCategoryBinding>() {
    @Inject
    lateinit var collectionCategoryAdapter: CollectionCategoryAdapter

    override fun getViewDataBinding(): CollectionCategoryBinding = CollectionCategoryBinding.inflate(layoutInflater)
    override var topBarVisibility: Boolean = false
    override var bottomNavigationBarVisibility: Boolean = true

    override fun initState() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            bookTypeRecyclerView.layoutManager =
                LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            bookTypeRecyclerView.adapter = collectionCategoryAdapter
        }
        collectionCategoryAdapter.setItem(Category.values().toList())
        collectionCategoryAdapter.setItemClickListener(object : OnClickedListener {
            override fun setOnClickedListener(pos: Int) {
                val category = collectionCategoryAdapter.getItem(pos)
                val action = CollectionCategoryDirections.actionCollectionCategoryToCollectionList(category)
                findNavController().navigate(action)
            }
        })
    }
}