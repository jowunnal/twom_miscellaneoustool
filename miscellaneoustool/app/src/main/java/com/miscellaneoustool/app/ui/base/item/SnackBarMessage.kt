package com.miscellaneoustool.app.ui.base.item

data class SnackBarMessage(
    val headerMessage: String,
    val contentMessage: String = ""
) {
    companion object {
        fun getInitValues() = SnackBarMessage(
            headerMessage = "",
            contentMessage = ""
        )
    }
}
