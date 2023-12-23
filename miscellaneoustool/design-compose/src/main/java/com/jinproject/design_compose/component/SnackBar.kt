package com.jinproject.design_compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.PreviewMiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.design_compose.theme.Typography

@Composable
fun SnackBarHostCustom(
    headerMessage: String,
    contentMessage: String,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    disMissSnackBar: () -> Unit
){
    Column {
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = modifier,
            snackbar = {
                SnackBarCustom(
                    headerMessage,
                    contentMessage,
                    disMissSnackBar
                )
            }
        )
        VerticalSpacer(height = 90.dp)
    }
}

@Composable
private fun SnackBarCustom(
    headerMessage: String,
    contentMessage: String,
    disMissSnackBar: () -> Unit
){
    Snackbar(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(78.dp),
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = RoundedCornerShape(8.dp),
        action =  {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                androidx.compose.material3.IconButton(
                    onClick = disMissSnackBar,
                    modifier = Modifier
                        .size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_x),
                        contentDescription = "snackBarCloseButton",
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.scrim
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = headerMessage,
                style = Typography.bodyLarge
            )
            if(contentMessage.isNotBlank()){
                VerticalSpacer(height = 4.dp)
                Text(
                    text = contentMessage,
                    style = Typography.bodySmall
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSnackBarCustom1(){
    PreviewMiscellaneousToolTheme {
        SnackBarCustom(
            headerMessage = "헤더메세지는 이렇게 보입니다.",
            contentMessage = "컨텐트메세지는 이렇게 보입니다.",
            disMissSnackBar = {}
        )
    }
}

@Preview
@Composable
private fun PreviewSnackBarCustom2(){
    PreviewMiscellaneousToolTheme {
        SnackBarCustom(
            headerMessage = "컨텐트메세지가 없다면 이렇게 보입니다.",
            contentMessage = "",
            disMissSnackBar = {}
        )
    }
}