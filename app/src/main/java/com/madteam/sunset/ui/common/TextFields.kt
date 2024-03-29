package com.madteam.sunset.ui.common

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madteam.sunset.R
import com.madteam.sunset.ui.theme.secondaryRegularBodyL

@Composable
fun DesignSystemTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes hint: Int,
    textType: KeyboardType,
    endIcon: @Composable () -> Unit,
    enabled: Boolean = true,
    isError: Boolean = false,
    @StringRes errorMessage: Int? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = if (isError) {
                Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(8.dp))
            } else {
                Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF999999), RoundedCornerShape(8.dp))
            },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                textColor = Color(0xFF333333),
                cursorColor = Color.Black,
                focusedLabelColor = Color(0xFF999999),
                unfocusedLabelColor = Color(0xFF999999),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            label = {
                Text(
                    text = stringResource(hint), color = if (isError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        Color.Unspecified
                    }
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = textType),
            trailingIcon = endIcon,
            enabled = enabled,
            visualTransformation = visualTransformation
        )
        if (isError && errorMessage != null) {
            Text(
                text = stringResource(errorMessage),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            )
        }
    }
}

@Composable
fun PasswordTextField(
    passwordValue: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: Int? = null,
    endIcon: @Composable () -> Unit = { Spacer(modifier = Modifier.size(0.dp)) }
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    DesignSystemTextField(
        value = passwordValue,
        onValueChange = onValueChange,
        isError = isError,
        errorMessage = errorMessage,
        hint = R.string.password,
        textType = KeyboardType.Password,
        endIcon = {
            val image = if (passwordVisible) {
                Icons.Outlined.Visibility
            } else {
                Icons.Outlined.VisibilityOff
            }
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, "")
            }
        },
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    )
}

@Composable
fun EmailTextField(
    emailValue: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: Int? = null,
    enabled: Boolean = true,
    endIcon: @Composable () -> Unit = { Spacer(modifier = Modifier.size(0.dp)) }
) {
    DesignSystemTextField(
        value = emailValue,
        isError = isError,
        errorMessage = errorMessage,
        onValueChange = onValueChange,
        hint = R.string.email_address,
        textType = KeyboardType.Email,
        enabled = enabled,
        endIcon = (endIcon)
    )
}

@Composable
fun GenericTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: Int? = null,
    @StringRes hint: Int,
    enabled: Boolean = true,
    endIcon: @Composable () -> Unit = { Spacer(modifier = Modifier.size(0.dp)) }
) {
    DesignSystemTextField(
        value = value,
        isError = isError,
        errorMessage = errorMessage,
        onValueChange = onValueChange,
        hint = hint,
        textType = KeyboardType.Text,
        enabled = enabled,
        endIcon = (endIcon)
    )
}

@Composable
fun UsernameTextField(
    usernameValue: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: Int? = null,
    endIcon: @Composable () -> Unit = { Spacer(modifier = Modifier.size(0.dp)) }
) {
    DesignSystemTextField(
        value = usernameValue,
        isError = isError,
        errorMessage = errorMessage,
        onValueChange = onValueChange,
        hint = R.string.username,
        textType = KeyboardType.Text,
        endIcon = (endIcon),
        enabled = enabled
    )
}

@Composable
fun ChatTextField(
    textValue: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column {
        TextField(
            value = textValue,
            maxLines = 3,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(50.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                textColor = Color(0xFF333333),
                cursorColor = Color.Black,
                focusedLabelColor = Color(0xFF999999),
                unfocusedLabelColor = Color(0xFF999999),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            textStyle = secondaryRegularBodyL,
            modifier = modifier
                .fillMaxWidth()
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(50.dp)),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "",
                    tint = if (textValue.isNotEmpty()) {
                        Color(0xFFFFB600)
                    } else {
                        Color(0xFF999999)
                    },
                    modifier = Modifier.clickable {
                        onSendClick()
                    }
                )
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.comment_keyboard_placeholder),
                    style = secondaryRegularBodyL,
                    textAlign = TextAlign.Start,
                    color = Color(0xFF999999)
                )
            }
        )
    }
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes hint: Int,
    textStyle: TextStyle,
    @ColorRes textColor: Color,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLines: Int = 10
) {
    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                textColor = Color(0xFF333333),
                cursorColor = Color.Black,
                focusedLabelColor = Color(0xFF999999),
                unfocusedLabelColor = Color(0xFF999999),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            singleLine = false,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            enabled = enabled,
            visualTransformation = visualTransformation,
            placeholder = {
                Text(
                    text = stringResource(id = hint),
                    style = textStyle,
                    textAlign = TextAlign.Start,
                    color = textColor
                )
            },
            textStyle = textStyle,
            maxLines = maxLines
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewTextFields() {

}

