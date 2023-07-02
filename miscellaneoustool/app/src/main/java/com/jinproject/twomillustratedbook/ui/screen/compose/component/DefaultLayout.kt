package com.jinproject.twomillustratedbook.ui.screen.compose.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun DefaultLayout(
    topBar: @Composable () -> Unit,
    scaffoldState: ScaffoldState,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = topBar,
        scaffoldState = scaffoldState,
        snackbarHost = { snackBarHostState ->
            Column() {
                SnackBarHostCustom(headerMessage = snackBarHostState.currentSnackbarData?.message ?: "",
                    contentMessage = snackBarHostState.currentSnackbarData?.actionLabel ?: "",
                    snackBarHostState = scaffoldState.snackbarHostState,
                    disMissSnackBar = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() })
                VerticalSpacer(height = 90.dp)
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        content(it)
    }
}