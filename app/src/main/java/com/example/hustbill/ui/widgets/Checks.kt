package com.example.hustbill.ui.widgets

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hustbill.ui.theme.colors

@Composable
fun AppSwitch(modifier: Modifier = Modifier, checked: Boolean, onChanged: (Boolean) -> Unit){
    Switch(
        modifier = modifier,
        checked = checked, onCheckedChange = {
            onChanged(it)
        }, colors = SwitchDefaults.colors(
            checkedTrackColor = colors.primary ,
            uncheckedBorderColor = colors.unfocusedSecondary,
            uncheckedThumbColor = colors.background,
            uncheckedTrackColor = colors.unfocusedSecondary
        )
    )
}