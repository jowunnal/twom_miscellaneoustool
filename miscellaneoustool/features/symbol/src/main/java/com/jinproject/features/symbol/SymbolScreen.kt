package com.jinproject.features.symbol

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.jinproject.design_compose.component.DescriptionText
import com.jinproject.design_compose.component.FooterText
import com.jinproject.design_compose.component.HeadlineText
import com.jinproject.design_compose.component.TextButton
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.VerticalWeightSpacer
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.core.base.item.SnackBarMessage

@Composable
fun SymbolScreen(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    navigateToGallery: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { result ->
            if (!result) {
                showSnackBar(
                    SnackBarMessage(
                        headerMessage = "이미지 읽기 권한을 수락되지 않았어요.",
                        contentMessage = "권한을 수락 하지 않으면 기능을 이용 하실 수 없어요."
                    )
                )
            } else
                navigateToGallery()
        }

    TextLayout(
        modifier = Modifier.fillMaxSize(),
        headline = "길드 마크 심볼!\n이제 편리하게 만드세요.",
        desc = "이미지를 불러온 후, 픽셀단위로 색상코드와 함께 화면에 보여드려요.\n더 이상 어렵게 길드마크를 만들지 않게 될거에요.",
        footer = "본 상품은 1회 불러오기당 소액의 요금이 과금되요.\n불러온 이미지가 정확한지 작은 사이즈로 변환해서 미리보기를 제공해 드리고 있어요.\n불러온 이미지가 마음에 드신다면 이후 과정을 진행해 주세요.\n결제 이후 청약철회가 불가능한 상품이니 신중하게 결정해주세요!",
        footerContent = {
            TextButton(
                text = "이미지 불러오기",
                onClick = {
                    checkPermission(
                        context = context,
                        permission =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            Manifest.permission.READ_MEDIA_IMAGES
                        else
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        onGranted = {
                            navigateToGallery()
                        },
                        permissionLauncher = permissionLauncher,
                    )
                },
            )
        }
    )
}

fun checkPermission(
    context: Context,
    permission: String,
    onGranted: () -> Unit,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
) {
    if (ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    )
        onGranted()
    else
        permissionLauncher.launch(permission)
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
private fun PreviewSymbolScreen() = MiscellaneousToolTheme {
    SymbolScreen(
        navigateToGallery = {},
        showSnackBar = {},
    )
}

@Composable
fun TextLayout(
    modifier: Modifier = Modifier,
    headline: String,
    headLineContent: @Composable ColumnScope.() -> Unit = {},
    desc: String,
    descriptionContent: @Composable ColumnScope.() -> Unit = {},
    footer: String,
    footerContent: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        modifier = modifier
            .padding(vertical = 32.dp, horizontal = 20.dp),
    ) {
        HeadlineText(
            text = headline,
            maxLines = 2
        )
        VerticalSpacer(height = 50.dp)
        headLineContent()
        VerticalWeightSpacer(float = 1f)
        DescriptionText(text = desc)
        VerticalSpacer(height = 30.dp)
        descriptionContent()
        VerticalWeightSpacer(float = 1f)
        FooterText(text = footer)
        VerticalWeightSpacer(float = 1f)
        footerContent()
    }
}