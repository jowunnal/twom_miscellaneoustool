package com.jinproject.features.collection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_ui.R
import com.jinproject.features.collection.component.CollectionDetail
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.compose.LocalNavigator
import com.jinproject.features.core.compose.LocalShowSnackbar
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun CollectionDetailScreen(
    collectionId: Int,
    viewModel: CollectionDetailViewModel = hiltViewModel<CollectionDetailViewModel, CollectionDetailViewModel.Factory>(
        creationCallback = { factory -> factory.create(collectionId = collectionId) }
    ),
) {
    val collection by viewModel.collection.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current
    val showSnackBar = LocalShowSnackbar.current

    val filterRemovedMessage = stringResource(id = R.string.message_success_removed)
    val priceUpdatedMessage = stringResource(id = R.string.message_success_applied)

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is CollectionDetailSideEffect.FilterRemoved -> {
                    navigator.goBack()
                    showSnackBar(SnackBarMessage(headerMessage = filterRemovedMessage))
                }

                is CollectionDetailSideEffect.PriceUpdated -> {
                    showSnackBar(SnackBarMessage(headerMessage = priceUpdatedMessage))
                    navigator.goBack()
                }
            }
        }
    }

    collection?.let {
        CollectionDetail(
            collection = it,
            dispatchEvent = { event ->
                when (event) {
                    is CollectionEvent.AddFilteringCollectionId -> viewModel.addFilteringCollectionId(
                        event.id
                    )

                    is CollectionEvent.UpdateItemsPrice -> viewModel.updateItemsPrice(event.items)
                    is CollectionEvent.SetFilteringCollectionIdList -> {}
                }
            },
            onNavigateBack = { navigator.goBack() },
        )
    }
}
