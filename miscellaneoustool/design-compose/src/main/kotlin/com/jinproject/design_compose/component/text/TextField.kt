package com.jinproject.design_compose.component.text

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.core.util.runIf
import com.jinproject.design_compose.component.button.DefaultIconButton
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState,
    onSearchClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    DefaultTextField(
        modifier = modifier,
        textFieldState = textFieldState,
        backgroundColor = MaterialTheme.colorScheme.primary,
        borderColor = MaterialTheme.colorScheme.surface,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onPrimary),
        lineLimits = TextFieldLineLimits.SingleLine,
    ) {
        DefaultIconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            icon = R.drawable.icon_search,
            onClick = {
                onSearchClick()
                focusManager.clearFocus()
            },
            iconSize = 24.dp,
            iconTint = MaterialTheme.colorScheme.onPrimary,
            backgroundTint = MaterialTheme.colorScheme.primary,
            interactionSource = remember { MutableInteractionSource() }
        )
    }
}

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = TextStyle.Default,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.onSurface),
    outputTransformation: OutputTransformation? = null,
    inputTransformation: InputTransformation? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits. Default,
    content: @Composable BoxScope.() -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var isFocused by remember {
        mutableStateOf(false)
    }

    BasicTextField(
        modifier = modifier
            .padding(end = 12.dp, top = 8.dp, bottom = 8.dp)
            .shadow(7.dp, RoundedCornerShape(10.dp))
            .background(
                backgroundColor,
                RoundedCornerShape(10.dp),
            )
            .border(
                1.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp),
            )
            .onFocusChanged { focusState -> isFocused = focusState.isFocused },
        state = textFieldState,
        decorator = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp),
            ) {
                if (textFieldState.text.isEmpty())
                    DescriptionSmallText(
                        text = if(isFocused) "" else stringResource(id = R.string.textfield_hint),
                        color = MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.fillMaxWidth().align(Alignment.CenterStart)
                    )

                innerTextField()

                content()
            }
        },
        enabled = enabled,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        textStyle = textStyle,
        cursorBrush = cursorBrush,
        outputTransformation = outputTransformation,
        inputTransformation = inputTransformation,
        lineLimits = lineLimits,
    )
}

class TenThousandSeparatorOutputTransformation(
    private val preFix: TextFieldBuffer.() -> Unit = {},
    private val suffix: TextFieldBuffer.() -> Unit = {},
) : OutputTransformation {
    override fun TextFieldBuffer.transformOutput() {
        preFix()

        if (length >= 4) {
            repeat((length - 1) / 3) { n ->
                val iterator = n.runIf(n > 0) {
                    n * 3
                }

                /**
                 * 천단위에 콤마 삽입
                 * (4자리 -> 1번째, 7자리 -> 1번째, 4번째, 10자리 -> 1번째, 4번째, 7번째) 각 자리수에 콤마를 삽입
                 * 2가지 단계로 분해
                 *  1. 콤마의 개수가 몇개인가?
                 *    - 천단위이기 때문에, 4자리 이상의 숫자에서 3자리가 추가될 때 마다 콤마가 삽입
                 *  2. 콤마는 언제 삽입되는가?
                 *    - 문자열 길이에 따라 삽입됨
                 *    - 문자열 길이가 4..6 일 때, 1..3 의 자리수에 한개의 콤마가 삽입
                 *    - 문자열 길이가 7..9 일 때, 1..3 와 4..6 자리수에 두개의 콤마가 각각 삽입
                 * 위의 결론에 따라, 콤마 갯수만큼 반복시키면서, 1..3 의 자릿수에 반복횟수를 계산하여 콤마 자릿수 계산
                 * 제약조건)
                 *  1. 문자열 길이는 4이상
                 *  2. insert로 stringBuffer 에 삽입하면, 반복에 따라 자리수(Length) 값이 늘어남
                 *  반영된 알고리즘)
                 *  1. Length 가 4 이상일 때, 콤마 개수만큼 반복하여
                 *  2. 문자열 길이에 따라 1 or 2 or 3 숫자값에 반복횟수 * 3 을 더하고,
                 *  3. 반복하여 insert 하면서 늘어난 문자열 길이만큼을 더하여 보완
                 */
                insert((((length - n - 1) % 3) + 1) + iterator + n, ",")
            }
        }

        suffix()
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewSearchTextField() = MiscellaneousToolTheme {
    SearchTextField(
        textFieldState = rememberTextFieldState(),
        onSearchClick = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun PreviewDefaultTextField() = MiscellaneousToolTheme {
    DefaultTextField(
        textFieldState = rememberTextFieldState(),
    )
}