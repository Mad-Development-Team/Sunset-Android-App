package com.madteam.sunset.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextAlign.Companion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.madteam.sunset.R
import com.madteam.sunset.ui.theme.primaryBoldHeadlineM
import com.madteam.sunset.ui.theme.primaryBoldHeadlineS
import com.madteam.sunset.ui.theme.secondaryRegularBodyL
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondaryRegularBodyS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyL

@Composable
fun DismissAndPositiveDialog(
    setShowDialog: (Boolean) -> Unit,
    @StringRes dialogTitle: Int,
    @StringRes dialogDescription: Int,
    @StringRes positiveButtonText: Int? = null,
    @StringRes dismissButtonText: Int,
    image: Int? = null,
    dismissClickedAction: () -> Unit,
    positiveClickedAction: () -> Unit = {}
) {
    Dialog(onDismissRequest = { }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = 2.dp,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Column {
                if (image != null) {
                    Image(
                        modifier = Modifier
                            .width(140.dp)
                            .height(140.dp)
                            .align(alignment = Alignment.CenterHorizontally),
                        painter = painterResource(id = image),
                        contentDescription = "Dialog image",
                        alignment = Alignment.Center
                    )
                } else {
                    CustomSpacer(size = 16.dp)
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 24.dp),
                        text = stringResource(dialogTitle),
                        style = primaryBoldHeadlineS,
                        textAlign = TextAlign.Center
                    )
                }
                CustomSpacer(size = 24.dp)
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = stringResource(dialogDescription),
                    style = secondaryRegularBodyL,
                    textAlign = TextAlign.Center
                )
                CustomSpacer(size = 24.dp)
                Row(Modifier.fillMaxWidth()) {
                    Button(
                        onClick = dismissClickedAction,
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFE094)),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Text(
                            text = stringResource(dismissButtonText),
                            style = secondarySemiBoldBodyL,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                    if (positiveButtonText != null) {
                        Button(
                            onClick = positiveClickedAction,
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                            shape = RoundedCornerShape(0.dp)
                        ) {
                            Text(
                                text = stringResource(positiveButtonText),
                                style = secondarySemiBoldBodyL,
                                color = Color.White,
                                textAlign = Companion.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CircularLoadingDialog() {
    Box(
        Modifier
            .size(100.dp)
            .background(color = Color.White, RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color(0xFFFFB600))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDialog(
    setShowDialog: (Boolean) -> Unit,
    setShowReportSent: (Boolean) -> Unit,
    @StringRes dialogTitle: Int,
    @StringRes dialogDescription: Int,
    availableOptions: List<String>,
    setSelectedOption: (String) -> Unit,
    selectedOptionText: String = availableOptions[0],
    additionalInformation: String,
    setAdditionalInformation: (String) -> Unit,
    @StringRes buttonText: Int,
    buttonClickedAction: () -> Unit,
    reportSent: Boolean
) {

    var expandedMenu by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = {
        setShowDialog(false)
        setShowReportSent(false)
    }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = 2.dp,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .wrapContentHeight()
        ) {
            if (!reportSent) {
                Column(modifier = Modifier.verticalScroll(scrollState)) {
                    CustomSpacer(size = 16.dp)
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 24.dp),
                            text = stringResource(dialogTitle),
                            style = primaryBoldHeadlineS,
                            textAlign = TextAlign.Center
                        )
                    }
                    CustomSpacer(size = 24.dp)
                    ExposedDropdownMenuBox(
                        expanded = expandedMenu,
                        onExpandedChange = { expandedMenu = !expandedMenu },
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        val containerColor = Color(0xFFFFE094)
                        TextField(
                            value = selectedOptionText,
                            onValueChange = {},
                            textStyle = secondaryRegularBodyL,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = containerColor,
                                unfocusedContainerColor = containerColor,
                                disabledContainerColor = containerColor,
                            ),
                            readOnly = true,
                            modifier = Modifier.menuAnchor(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expandedMenu
                                )
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = expandedMenu,
                            onDismissRequest = { expandedMenu = false }) {
                            availableOptions.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(text = selectionOption) },
                                    onClick = {
                                        setSelectedOption(selectionOption)
                                        expandedMenu = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    modifier = Modifier.background(Color(0xFFFFE094))
                                )
                            }
                        }
                    }
                    CustomSpacer(size = 24.dp)
                    val containerColor = Color(0xFFFFE094)
                    TextField(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        textStyle = secondaryRegularBodyL,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = containerColor,
                            unfocusedContainerColor = containerColor,
                            disabledContainerColor = containerColor,
                        ),
                        value = additionalInformation,
                        onValueChange = { setAdditionalInformation(it) },
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.additional_information),
                                style = secondaryRegularBodyL,
                                color = Color(0xFF666666)
                            )
                        }
                    )
                    CustomSpacer(size = 24.dp)
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(dialogDescription),
                        style = secondaryRegularBodyL,
                        textAlign = TextAlign.Center
                    )
                    CustomSpacer(size = 24.dp)
                    Column(
                        modifier = Modifier.height(60.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Button(
                            onClick = buttonClickedAction,
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                            shape = RoundedCornerShape(0.dp)
                        ) {
                            Text(
                                text = stringResource(buttonText),
                                style = secondarySemiBoldBodyL,
                                color = Color.White,
                                textAlign = Companion.Center
                            )
                        }
                    }
                }
            } else {
                Column {
                    CustomSpacer(size = 16.dp)
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 24.dp),
                            text = stringResource(R.string.report_sent),
                            style = primaryBoldHeadlineS,
                            textAlign = TextAlign.Center
                        )
                    }
                    CustomSpacer(size = 16.dp)
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.report_sent_description),
                        style = secondaryRegularBodyL,
                        textAlign = TextAlign.Center
                    )
                    CustomSpacer(size = 16.dp)
                    Button(
                        onClick = {
                            setShowDialog(false)
                            setShowReportSent(false)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.continue_text),
                            style = secondarySemiBoldBodyL,
                            color = Color.White,
                            textAlign = Companion.Center
                        )
                    }
                }
            }

        }

    }
}

@Preview
@Composable
fun PreviewReportDialog() {
    ReportDialog(
        setShowDialog = {},
        dialogTitle = R.string.inform_about_spot,
        availableOptions = listOf("opcion 1", "opcion 2"),
        setSelectedOption = {},
        dialogDescription = R.string.inform_about_spot_description,
        selectedOptionText = "opcion 1",
        additionalInformation = "",
        setAdditionalInformation = {},
        buttonText = R.string.continue_text,
        buttonClickedAction = {},
        reportSent = false,
        setShowReportSent = {}
    )
}

@Composable
fun LocationPermissionDialog(
    onContinueClick: () -> Unit
) {
    Dialog(onDismissRequest = {}) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = 2.dp,
            modifier = Modifier
                .padding(24.dp)
                .wrapContentSize()
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                val (title, animation, description, button) = createRefs()
                val locationAnimation by rememberLottieComposition(
                    spec = LottieCompositionSpec.RawRes(
                        R.raw.location_animation
                    )
                )

                LottieAnimation(
                    composition = locationAnimation,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .size(200.dp)
                        .constrainAs(animation) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
                Text(
                    text = stringResource(id = R.string.enable_location_title),
                    style = primaryBoldHeadlineM,
                    textAlign = Companion.Center,
                    modifier = Modifier.constrainAs(title) {
                        top.linkTo(animation.bottom, 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
                Text(
                    text = stringResource(id = R.string.enable_location_description),
                    style = secondaryRegularBodyL,
                    textAlign = Companion.Center,
                    modifier = Modifier.constrainAs(description) {
                        top.linkTo(title.bottom, 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
                SmallButtonSunset(
                    onClick = { onContinueClick() },
                    text = R.string.enable_location,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(button) {
                            top.linkTo(description.bottom, 24.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
            }
        }
    }
}

@Composable
fun NotificationsPermissionDialog(
    onContinueClick: () -> Unit
) {
    Dialog(onDismissRequest = {}) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = 2.dp,
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize()
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                val (title, animation, description, button) = createRefs()
                val notificationAnimation by rememberLottieComposition(
                    spec = LottieCompositionSpec.RawRes(
                        R.raw.notification_animation
                    )
                )

                LottieAnimation(
                    composition = notificationAnimation,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .size(200.dp)
                        .constrainAs(animation) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
                Text(
                    text = stringResource(id = R.string.enable_notifications_title),
                    style = primaryBoldHeadlineM,
                    textAlign = Companion.Center,
                    modifier = Modifier.constrainAs(title) {
                        top.linkTo(animation.bottom, 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
                Text(
                    text = stringResource(id = R.string.enable_notifications_description),
                    style = secondaryRegularBodyL,
                    textAlign = Companion.Center,
                    modifier = Modifier.constrainAs(description) {
                        top.linkTo(title.bottom, 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
                SmallButtonSunset(
                    onClick = { onContinueClick() },
                    text = R.string.enable_notifications,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(button) {
                            top.linkTo(description.bottom, 24.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LocationPermissionDialogPreview() {
    LocationPermissionDialog {}
}

@Preview(showSystemUi = true)
@Composable
fun NotificationsPermissionDialogPreview() {
    NotificationsPermissionDialog {}
}

@Composable
fun SunsetPhasesInfoDialog(
    phase: String,
    setShowDialog: (Boolean) -> Unit
) {
    val phaseTitle: Int = when (phase) {
        "sunset" -> {
            R.string.sunset
        }

        "golden_hour" -> {
            R.string.golden_hour
        }

        "blue_hour" -> {
            R.string.blue_hour
        }

        else -> {
            R.string.daylight
        }
    }
    val phaseDescription: Int = when (phase) {
        "sunset" -> {
            R.string.daylight_description
        }

        "golden_hour" -> {
            R.string.golden_hour_description
        }

        "blue_hour" -> {
            R.string.blue_hour_description
        }

        else -> {
            R.string.daylight_description
        }
    }
    val phaseAnimation: Int = when (phase) {
        "sunset" -> {
            R.raw.sun_vector_animation
        }

        "golden_hour" -> {
            R.raw.golden_hour_animation
        }

        "blue_hour" -> {
            R.raw.moon_vector_animation
        }

        else -> {
            R.raw.sun_vector_animation
        }
    }
    val iconAnimation by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            phaseAnimation
        )
    )
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = 2.dp
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                LottieAnimation(
                    composition = iconAnimation,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(80.dp),
                    alignment = Alignment.Center
                )
                CustomSpacer(size = 8.dp)
                androidx.compose.material3.Text(
                    text = stringResource(id = phaseTitle),
                    style = secondarySemiBoldBodyL,
                    textAlign = TextAlign.Center
                )
                CustomSpacer(size = 8.dp)
                androidx.compose.material3.Text(
                    text = stringResource(id = phaseDescription),
                    style = secondaryRegularBodyM,
                    textAlign = TextAlign.Center
                )
                CustomSpacer(size = 16.dp)
                androidx.compose.material3.Text(
                    text = stringResource(id = R.string.credits_sunrisesunsetio),
                    style = secondaryRegularBodyS,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun SunsetQualityInfoDialog(
    score: Int,
    setShowDialog: (Boolean) -> Unit
) {
    val qualityTitle = if (score <= 25) {
        R.string.poor
    } else if (score <= 50) {
        R.string.fair
    } else if (score <= 75) {
        R.string.good
    } else {
        R.string.great
    }
    val qualityDescription = if (score <= 25) {
        R.string.poor_description
    } else if (score <= 50) {
        R.string.fair_description
    } else if (score <= 75) {
        R.string.good_description
    } else {
        R.string.great_description
    }
    val qualityAnimation = if (score <= 25) {
        R.raw.sad_cat_animation
    } else if (score <= 50) {
        R.raw.cat_tv_animation
    } else if (score <= 75) {
        R.raw.dog_selfie_animation
    } else {
        R.raw.guy_photo_animation
    }
    val iconAnimation by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            qualityAnimation
        )
    )
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = 2.dp
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                LottieAnimation(
                    composition = iconAnimation,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(80.dp),
                    alignment = Alignment.Center
                )
                CustomSpacer(size = 8.dp)
                androidx.compose.material3.Text(
                    text = stringResource(id = qualityTitle),
                    style = secondarySemiBoldBodyL,
                    textAlign = TextAlign.Center
                )
                CustomSpacer(size = 8.dp)
                androidx.compose.material3.Text(
                    text = stringResource(id = qualityDescription),
                    style = secondaryRegularBodyM,
                    textAlign = TextAlign.Center
                )
                CustomSpacer(size = 16.dp)
                androidx.compose.material3.Text(
                    text = stringResource(id = R.string.credits_weatherapi),
                    style = secondaryRegularBodyS,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

