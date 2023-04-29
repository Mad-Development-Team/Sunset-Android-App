package com.madteam.sunset.navigation

sealed class SunsetRoutes(val route: String) {
    object WelcomeScreen : SunsetRoutes(route = "welcome_screen")
    object SignInCard : SunsetRoutes(route = "sign_in_card")
    object SignUpCard : SunsetRoutes(route = "sign_up_card")
    object MyProfileScreen : SunsetRoutes(route = "my_profile_screen")
}