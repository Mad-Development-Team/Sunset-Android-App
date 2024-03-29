package com.madteam.sunset.ui.screens.lostpassword.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.madteam.sunset.R
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.EmailTextField
import com.madteam.sunset.ui.common.ErrorIcon
import com.madteam.sunset.ui.common.SmallButtonDark
import com.madteam.sunset.ui.common.SuccessIcon
import com.madteam.sunset.ui.screens.lostpassword.state.LostPasswordUIEvent
import com.madteam.sunset.ui.screens.lostpassword.viewmodel.LostPasswordViewModel
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.primaryBoldHeadlineS
import com.madteam.sunset.ui.theme.primaryMediumHeadlineXS

@Composable
fun LostPasswordScreen(
    navController: NavController,
    viewModel: LostPasswordViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LostPasswordContent(
        isValidForm = state.isValidForm,
        validateForm = { viewModel.onEvent(LostPasswordUIEvent.ValidateForm(it)) },
        sendButton = { viewModel.onEvent(LostPasswordUIEvent.ResetPasswordWithEmailIntent(it)) },
        navigateTo = navController::navigate
    )
}

@Composable
fun LostPasswordContent(
    isValidForm: Boolean,
    sendButton: (String) -> Unit,
    validateForm: (String) -> Unit,
    navigateTo: (String) -> Unit
) {

    val animComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.sunsetlighthouse))
    var emailValueText by remember { mutableStateOf("") }
    var emailSent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomSpacer(size = 16.dp)
        IconButton(
            modifier = Modifier.align(alignment = Alignment.Start),
            onClick = { navigateTo(SunsetRoutes.WelcomeScreen.route) }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Arrow back icon",
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp)
            )
        }
        CustomSpacer(size = 48.dp)
        LottieAnimation(
            modifier = Modifier
                .height(250.dp)
                .width(250.dp),
            composition = animComposition,
            iterations = LottieConstants.IterateForever
        )
        CustomSpacer(size = 80.dp)
        Text(
            text = stringResource(id = R.string.lost_password),
            style = primaryBoldHeadlineL,
            color = Color(0xFF333333)
        )
        CustomSpacer(size = 24.dp)
        Text(
            text = stringResource(id = R.string.lost_password_enter_email),
            style = primaryBoldHeadlineS,
            color = Color(0xFF999999),
            textAlign = TextAlign.Center
        )
        CustomSpacer(size = 40.dp)
        EmailTextField(
            emailValue = emailValueText,
            enabled = !emailSent,
            onValueChange = { email ->
                emailValueText = email
                validateForm(emailValueText)
            },
            endIcon = {
                if (isValidForm) SuccessIcon() else if (emailValueText.isNotBlank()) {
                    ErrorIcon()
                }
            }
        )
        CustomSpacer(size = 24.dp)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (emailSent) {
                Snackbar(containerColor = Color(0xFFFFB600)) {
                    Text(
                        text = stringResource(id = R.string.lost_password_email_sent),
                        style = primaryMediumHeadlineXS,
                        color = Color.White
                    )
                }
            } else {
                SmallButtonDark(onClick = {
                    sendButton(emailValueText)
                    emailSent = true
                }, text = R.string.send_link, enabled = isValidForm)
            }
            CustomSpacer(size = 24.dp)
        }
    }
}