package com.admin.shoppinappadmin.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import java.lang.Error

@Composable
fun CommonTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    value:String,
    isError: Boolean
) {

    OutlinedTextField(value = value, onValueChange = {
        onTextChanged(it)
    }, label = { Text(text = text) },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        modifier = modifier.fillMaxWidth(),
        isError = !isError
    )

}
