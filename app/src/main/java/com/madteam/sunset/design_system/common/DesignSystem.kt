package com.madteam.sunset.common.design_system

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.madteam.sunset.R
import com.madteam.sunset.ui.theme.primaryBoldDisplayM
import com.madteam.sunset.ui.theme.secondaryRegularBodyL
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineM
import com.madteam.sunset.welcome.ui.CARD_HEIGHT

// Spacers

@Composable
fun CustomSpacer(size: Dp) {
    Spacer(modifier = Modifier.size(size))
}

@Composable
fun CustomDivider(
    modifier: Modifier,
    height: Dp = 1.dp,
    color: Color
) {
    Divider(
        color = color,
        modifier = modifier,
        thickness = height
    )
}

// Welcome Screen

@Composable
fun MainTitle() {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.hello),
            style = primaryBoldDisplayM
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.we_are_sunset),
            style = primaryBoldDisplayM
        )
    }
}

@Composable
fun SubTitle(modifier: Modifier) {
    Text(
        text = stringResource(R.string.welcome_subtitle),
        style = secondaryRegularBodyL,
        modifier = modifier
    )
}


//Sign In / Sign Up Card

@Composable
fun CardHandler() {
    Divider(
        color = Color(0xFFD9D9D9),
        thickness = 6.dp,
        modifier = Modifier
            .width(122.dp)
            .clip(RoundedCornerShape(50.dp))
    )
}

@Composable
fun CardShade() {
    Box(
        modifier = Modifier
            .height((LocalConfiguration.current.screenHeightDp * CARD_HEIGHT + 8).dp)
            .width((LocalConfiguration.current.screenWidthDp * 0.8).dp)
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(Color(0xFFFFe094))
    )
}

@Composable
fun CardTitle(@StringRes text: Int) {
    Text(
        text = stringResource(id = text),
        style = secondarySemiBoldHeadLineM
    )
}

@Composable
fun CardSubtitle(text: Int) {
    Text(
        text = stringResource(id = text),
        style = secondaryRegularBodyL
    )
}

@Composable
fun ForgotPasswordText() {
    Text(
        text = stringResource(R.string.forgot_your_password),
        style = secondarySemiBoldBodyM,
        color = Color(0xFF333333)
    )
}

@Composable
fun OtherLoginMethodsText(modifier: Modifier, @StringRes text: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = text),
            style = secondarySemiBoldBodyM,
            color = Color(0xFF666666),
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OtherLoginMethodsSection(@StringRes text: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CustomDivider(Modifier.weight(0.5f), color = Color(0xFF999999))
        OtherLoginMethodsText(
            Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            text = text
        )
        CustomDivider(Modifier.weight(0.5f), color = Color(0xFF999999))
    }
}
