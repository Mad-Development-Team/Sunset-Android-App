package com.madteam.sunset.ui.screens.myprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.ProfileImage
import com.madteam.sunset.ui.common.SmallButtonDark
import com.madteam.sunset.ui.common.UserLocationText
import com.madteam.sunset.ui.common.UserUsernameText

@Composable
fun MyProfileScreen(
  navController: NavController = rememberNavController(),
  myProfileViewModel: MyProfileViewModel = hiltViewModel(),
) {
  val username = myProfileViewModel.username.collectAsState().value

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .background(Color.White),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    CustomSpacer(size = 40.dp)
    ProfileImage(image = R.drawable.logo_degrade)
    CustomSpacer(size = 24.dp)
    UserUsernameText(username = username)
    CustomSpacer(size = 8.dp)
    UserLocationText(location = "Terrassa, BCN")
    CustomSpacer(size = 48.dp)
    SmallButtonDark(
      onClick = { /* TODO */ },
      text = R.string.log_out,
      enabled = true
    )
  }
}

@Composable
@Preview
fun MyProfileScreenPreview() {
  MyProfileScreen()
}