package com.jinproject.features.collection.setting

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jinproject.features.collection.R
import com.jinproject.features.collection.databinding.CollectionSettingBinding
import com.jinproject.features.core.BaseFragment

class CollectionSetting : BaseFragment<CollectionSettingBinding>() {
    override fun getViewDataBinding(): CollectionSettingBinding =
        CollectionSettingBinding.inflate(layoutInflater)

    private val navArgs: CollectionSettingArgs by navArgs()
    override var topBarVisibility: Boolean = true

    override fun initState() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            navController = findNavController()
            tvSettingFilter.setOnClickListener {
                val action =
                    CollectionSettingDirections.actionCollectionSettingToCollectionSettingFilter(
                        navArgs.category
                    )
                findNavController().navigate(action)
            }
        }
        provideMenu()
    }

    private fun provideMenu() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.book_option_menu, menu)
                    menu.findItem(R.id.icon_search).isVisible = false
                    setMenuColorOnDarkMode(menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    when (menuItem.itemId) {
                        android.R.id.home -> findNavController().popBackStack()
                    }
                    return true
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }
}