package com.example.diybill.ui.widgets

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.example.diybill.ui.theme.Gap
import com.example.diybill.ui.theme.colors

/**
 * Plain TextField 普通TextEditor(带Hint)
 */
@Composable
fun PlainTextField(
    modifier: Modifier = Modifier.fillMaxWidth(),
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    hint: String,
    hintColor: Color = colors.textSecondary,
    textColor: Color = colors.textPrimary,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorColor: Color = Color.Black
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle.copy(textColor),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        onTextLayout = onTextLayout,
        cursorBrush = SolidColor(cursorColor),
        decorationBox = {
            Surface(
                modifier = Modifier,
                color = (Color.Transparent),
            ) {
                it()
                if (value.isEmpty()) {
                    Text(
                        text = hint,
                        style = textStyle.copy(
                            color = hintColor
                        ),
                        modifier = Modifier.padding(horizontal = Gap.Tiny)
                    )
                }
            }
        }
    )
}