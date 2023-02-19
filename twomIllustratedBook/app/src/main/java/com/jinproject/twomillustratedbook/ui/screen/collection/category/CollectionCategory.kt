package com.jinproject.twomillustratedbook.ui.screen.collection.category

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinproject.twomillustratedbook.databinding.CollectionCategoryBinding
import com.jinproject.twomillustratedbook.domain.model.Category
import com.jinproject.twomillustratedbook.ui.listener.OnItemClickListener
import com.jinproject.twomillustratedbook.ui.base.BaseFragment
import com.jinproject.twomillustratedbook.ui.screen.droplist.monster.DropListViewModel
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
        collectionCategoryAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun OnHomeItemClick(v: View, pos: Int) {
                val category = collectionCategoryAdapter.getItem(pos)
                val action = CollectionCategoryDirections.actionCollectionCategoryToCollectionList(category)
                findNavController().navigate(action)
            }
        })
    }
}