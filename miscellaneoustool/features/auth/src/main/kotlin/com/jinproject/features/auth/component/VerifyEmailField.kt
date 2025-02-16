package com.jinproject.features.auth.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.HorizontalSpacer
import com.jinproject.design_compose.component.button.TextButton
import com.jinproject.design_compose.component.text.auth.AuthTextFieldState
import com.jinproject.design_compose.component.text.auth.EmailField
import com.jinproject.design_compose.component.text.auth.rememberAuthTextFieldState
import com.jinproject.design_compose.theme.MiscellaneousToolTheme

@Composable
internal fun VerifyEmailField(
    modifier: Modifier = Modifier,
    authTextFieldState: AuthTextFieldState = rememberAuthTextFieldState(),
    sendCode: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        EmailField(
            modifier = Modifier.weight(1f),
            authTextFieldState = authTextFieldState,
        )

        HorizontalSpacer(width = 8.dp)

        TextButton(
            text = stringResource(id = com.jinproject.design_ui.R.string.verify),
            shape = RoundedCornerShape(10.dp),
            contentPaddingValues = PaddingValues(vertical = 18.dp, horizontal = 24.dp),
            onClick = {
                sendCode()
            },
        )
    }
}

@Composable
@Preview
private fun PreviewVerifyEmailFieldScreen() = MiscellaneousToolTheme {
    Column(modifier = Modifier.fillMaxWidth()) {
        VerifyEmailField(
            authTextFieldState = AuthTextFieldState(TextFieldState("abcd")),
            sendCode = {},
        )
    }
}