package com.example.hustbill.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.hustbill.ui.theme.AppTypography
import com.example.hustbill.ui.theme.Gap
import com.example.hustbill.ui.theme.colors

open class AppDialog {
    protected var composable: MutableList<@Composable () -> Unit> = mutableListOf()
    protected var negativeButton: @Composable RowScope.(() -> Unit) -> Unit = {}
    protected var positiveButton: @Composable RowScope.(() -> Unit) -> Unit = {}
    protected var backgroundColor : Color = Color.White


    fun withBackground(color: Color){
        backgroundColor = color
    }


    fun withTitle(title:String, bold: Boolean = true, image: Int? = null): AppDialog {
        if (image != null) composable.add { Title(title, bold,image) }
        else composable.add { Title(title,bold) }
        composable.add { Spacer(modifier = Modifier.height(Gap.Mid)) }
        return this
    }

    fun withMessage(msg: String): AppDialog {
        composable.add { Message(msg) }
        composable.add { Spacer(modifier = Modifier.height(Gap.Mid)) }
        return this
    }

    fun withRatioList(list: List<String>, selected: Int, onSelected: (Int) -> Unit): AppDialog {
        composable.add { RatioList(list, selected, onSelected) }
        return this
    }

    fun withMultipleSelectList(list: List<Pair<String, () -> Unit>>): AppDialog {
        composable.add { MultipleSelectList(list = list) }
        return this
    }

    fun withSpace(dp: Dp){
        composable.add{
            Spacer(modifier = Modifier.height(dp))
        }
    }

    fun withView(
        content: @Composable () -> Unit
    ): AppDialog {
        composable.add {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            ) {
                content()
            }
        }
        return this
    }

    fun positive(text: String = "确认", onClick: () -> Boolean): AppDialog {

        positiveButton = {
            Button(text, true) {
                if (onClick()) it() // Dismiss
            }
        }
        return this
    }

    fun negative(text: String = "取消", onClick: () -> Boolean): AppDialog {
        negativeButton = {
            Button(text, false) {
                if (onClick()) it() // Dismiss
            }
        }
        return this
    }


    @Composable
    open fun Build(
        show: Boolean,
        properties: DialogProperties,
        onDismissRequest: () -> Unit,
        content: @Composable () -> Unit
    ) {
        Box {
            if (show) {
                Dialog(onDismissRequest = {
                    onDismissRequest()
                }, properties = properties) {
                    Column(
                        Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .fillMaxWidth()
                            .background(backgroundColor),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        composable.forEach { it() }
                        if (negativeButton != {} || positiveButton != {}) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(
                                    30.dp,
                                    Alignment.CenterHorizontally
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                negativeButton {

                                }
                                positiveButton {

                                }
                            }
                        }
                    }
                }
            }
            content()
        }
    }

    companion object {
        @Composable
        private fun Title(title: String,bold : Boolean, image: Int? = null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                if (image != null)
                    Row(
                        modifier = Modifier.weight(0.8f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        EasyImage(
                            src = image,
                            contentDescription = "头部旁边",
                            modifier = Modifier.size(if(bold) 40.dp else 30.dp)
                        )
                    }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        style = AppTypography.big
                    )
                }
                if (image != null)
                    Spacer(modifier = Modifier.weight(0.8f))
            }
        }

        @Composable
        private fun Message(message: String) {
            Text(
                text = message,
                style = AppTypography.small,
                color = colors.message,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )
        }


        @Composable
        private fun RatioList(list: List<String>, selected: Int, onSelected: (Int) -> Unit) {

        }

        @Composable
        private fun MultipleSelectList(list: List<Pair<String, () -> Unit>>) {

        }

        @Composable
        private fun RowScope.Button(text: String, positive: Boolean, onClick: () -> Unit) {
            androidx.compose.material3.Button(onClick = onClick) {
                Text(text, color = if(positive) colors.textPrimary else colors.textSecondary)
            }
        }
    }
}