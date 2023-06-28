package com.miscellaneoustool.app.ui.screen.collection.item.item

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider

class CollectionMenu(
    private val onCreate: (Menu, MenuInflater) -> Unit,
    private val onClick: (MenuItem) -> Unit
) : MenuProvider {
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        onCreate(menu, menuInflater)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        onClick(menuItem)
        return true
    }
}