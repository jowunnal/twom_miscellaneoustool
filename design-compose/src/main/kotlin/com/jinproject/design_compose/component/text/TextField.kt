package com.jinproject.design_compose.component.text

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.core.util.runIf
import com.jinproject.design_compose.component.HorizontalSpacer
import com.jinproject.design_compose.component.button.DefaultIconButton
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    borderColor: Color = MaterialTheme.colorScheme.surface,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall.copy(
        MaterialTheme.colorScheme.contentColorFor(
            backgroundColor
        )
    ),
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.onPrimary),
    exitIconVisibility: Boolean = false,
    changeExitIconVisibility: (Boolean) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val exitIcon by rememberUpdatedState(exitIconVisibility)
    val focusRequester = remember {
        FocusRequester()
    }

    DefaultTextField(
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.hasFocus)
                    changeExitIconVisibility(true)
            },
        textFieldState = textFieldState,
        backgroundColor = backgroundColor,
        borderColor = borderColor,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        textStyle = textStyle,
        cursorBrush = cursorBrush,
        lineLimits = TextFieldLineLimits.SingleLine,
    ) {
        AnimatedContent(exitIcon) { targetState ->
            if (targetState)
                DefaultIconButton(
                    modifier = Modifier,
                    icon = R.drawable.ic_x,
                    onClick = {
                        textFieldState.clearText()
                        focusManager.clearFocus()
                        changeExitIconVisibility(false)
                    },
                    iconSize = 24.dp,
                    iconTint = MaterialTheme.colorScheme.contentColorFor(backgroundColor),
                    backgroundTint = backgroundColor,
                    interactionSource = remember { MutableInteractionSource() }
                )
            else
                DefaultIconButton(
                    modifier = Modifier,
                    icon = R.drawable.icon_search,
                    onClick = {
                        focusRequester.requestFocus()
                        changeExitIconVisibility(true)
                    },
                    iconSize = 24.dp,
                    iconTint = MaterialTheme.colorScheme.contentColorFor(backgroundColor),
                    backgroundTint = backgroundColor,
                    interactionSource = remember { MutableInteractionSource() }
                )
        }
    }
}

@Composable
fun ChatTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState,
    onEntered: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    DefaultTextField(
        modifier = modifier.padding(horizontal = 6.dp),
        textFieldState = textFieldState,
        backgroundColor = MaterialTheme.colorScheme.primary,
        borderColor = MaterialTheme.colorScheme.surface,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onPrimary),
        lineLimits = TextFieldLineLimits.MultiLine(),
    ) {
        DefaultIconButton(
            modifier = Modifier,
            icon = R.drawable.ic_enter,
            onClick = {
                onEntered()
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
fun OutlineVerifyTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState,
    outputTransformation: OutputTransformation? = null,
    isError: Boolean = false,
    errorMessage: String = "",
    headerIcon: @Composable (() -> Unit)? = null,
    hint: String = "",
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
    content: @Composable RowScope.() -> Unit = {},
) {
    val error by rememberUpdatedState(newValue = isError && textFieldState.text.isNotBlank())

    Column(modifier) {
        DefaultTextField(
            modifier = Modifier,
            textFieldState = textFieldState,
            keyboardOptions = keyboardOptions,
            borderColor = if (error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            lineLimits = TextFieldLineLimits.MultiLine(),
            headerIcon = headerIcon,
            outputTransformation = outputTransformation,
            hint = hint,
            enabled = enabled,
            content = content,
        )
        if (error)
            DescriptionSmallText(
                text = errorMessage,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.error
            )
    }
}

@Composable
fun NumberInputTextField(
    modifier: Modifier = Modifier,
    initialValue: String,
    maxNumber: Int,
    textFieldState: TextFieldState,
    outputTransformation: OutputTransformation? = null,
    inputTransformation: InputTransformation? = LimitNumberInputTransformation(maxNumber),
    headerIcon: @Composable (() -> Unit)? = null,
    hint: String = stringResource(R.string.watch_setting_input_number),
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    borderColor: Color = MaterialTheme.colorScheme.surface,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall.copy(
        MaterialTheme.colorScheme.contentColorFor(
            backgroundColor
        )
    ),
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.onPrimary),
    content: @Composable RowScope.() -> Unit = {},
) {
    DefaultTextField(
        modifier = modifier.onFocusChanged {
            textFieldState.edit {
                if (it.hasFocus)
                    delete(start = 0, end = length)
                else {
                    if (length == 0)
                        insert(0, initialValue)
                }
            }
        },
        textFieldState = textFieldState,
        keyboardOptions = keyboardOptions,
        backgroundColor = backgroundColor,
        borderColor = borderColor,
        textStyle = textStyle,
        cursorBrush = cursorBrush,
        lineLimits = TextFieldLineLimits.SingleLine,
        headerIcon = headerIcon,
        outputTransformation = outputTransformation,
        inputTransformation = inputTransformation,
        hint = hint,
        enabled = enabled,
        content = content,
    )
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
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    headerIcon: (@Composable () -> Unit)? = null,
    hint: String = stringResource(id = R.string.textfield_hint),
    content: @Composable RowScope.() -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember {
        mutableStateOf(false)
    }

    BasicTextField(
        modifier = modifier
            .background(
                backgroundColor,
                RoundedCornerShape(10.dp),
            )
            .border(
                1.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp),
            )
            .graphicsLayer {
                alpha = if (enabled) 1f else 0.3f
            }
            .onFocusChanged { focusState -> isFocused = focusState.isFocused },
        state = textFieldState,
        decorator = { innerTextField ->
            Row(
                modifier = Modifier
                    .height(textStyle.lineHeight.value.dp + 36.dp)
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                headerIcon?.let {
                    it()
                }
                HorizontalSpacer(width = 4.dp)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                ) {
                    if (textFieldState.text.isEmpty())
                        DescriptionSmallText(
                            text = if (isFocused) "" else hint,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        )

                    innerTextField()
                }

                content()
            }
        },
        enabled = enabled,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = {
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

class PasswordOutputTransformation(
    private val mask: Char = '\u2022',
    private val hasFocus: Boolean = false
) : OutputTransformation {
    override fun TextFieldBuffer.transformOutput() {
        if (!hasFocus)
            replace(0, length, mask.toString().repeat(length))
        else if (length > 1)
            replace(0, length - 1, mask.toString().repeat(length - 1))

    }
}

/**
 * 숫자만 입력받는 텍스트 필드에서 최대값으로 제한하는 inputTransformation
 *
 * @param maxNumber 제한하기 위한 최대값
 */
class LimitNumberInputTransformation(
    private val maxNumber: Int = 0,
) : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        if ((asCharSequence().toString().toIntOrNull() ?: 0) > maxNumber)
            replace(0, length, maxNumber.toString())
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewSearchTextField() = MiscellaneousToolTheme {
    SearchTextField(
        textFieldState = rememberTextFieldState(),
    )
}

@Composable
@Preview(showBackground = true)
private fun PreviewChatTextField() = MiscellaneousToolTheme {
    ChatTextField(
        textFieldState = rememberTextFieldState(),
        onEntered = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun PreviewDefaultTextField() = MiscellaneousToolTheme {
    DefaultTextField(
        textFieldState = rememberTextFieldState(),
    )
}


@Composable
@Preview(showBackground = true)
private fun PreviewOutlineVerifyTextField() = MiscellaneousToolTheme {
    OutlineVerifyTextField(
        textFieldState = rememberTextFieldState("abcd@gmail.com"),
        isError = true,
        errorMessage = "잘못된 비밀번호 양식이에요. 8~16글자까지 입력가능하고 영어,숫자,!@#\$%^&amp;*특수문자만 사용가능해요."
    )
}