package com.madteam.sunset.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.madteam.sunset.navigation.SunsetRoutes.LostPasswordScreen
import com.madteam.sunset.navigation.SunsetRoutes.MyProfileScreen
import com.madteam.sunset.navigation.SunsetRoutes.SignInCard
import com.madteam.sunset.navigation.SunsetRoutes.SignUpCard
import com.madteam.sunset.navigation.SunsetRoutes.WelcomeScreen
import com.madteam.sunset.ui.screens.lostpassword.LostPasswordScreen
import com.madteam.sunset.ui.screens.myprofile.MyProfileScreen
import com.madteam.sunset.ui.screens.welcome.WelcomeScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SunsetNavigation() {
  val navController = rememberAnimatedNavController()

  AnimatedNavHost(navController = navController, startDestination = WelcomeScreen.route) {

    composable(WelcomeScreen.route) {
      WelcomeScreen(navController)
    }

    composable(SignUpCard.route) {
      WelcomeScreen(navController)
    }

    composable(SignInCard.route) {
      WelcomeScreen(navController)
    }

    composable(MyProfileScreen.route) {
      MyProfileScreen(navController)
    }

    composable(
      LostPasswordScreen.route,
      enterTransition = { slideInVertically(initialOffsetY = { it }, animationSpec = tween(500)) },
      exitTransition = { slideOutVertically(targetOffsetY = { it }, animationSpec = tween(500)) }) {
      LostPasswordScreen(navController)
    }
  }
}