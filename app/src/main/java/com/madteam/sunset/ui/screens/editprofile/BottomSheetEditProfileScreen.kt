package com.madteam.sunset.ui.screens.editprofile

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.madteam.sunset.R
import com.madteam.sunset.data.model.UserProfile
import com.madteam.sunset.ui.common.CircularLoadingDialog
import com.madteam.sunset.ui.common.CloseIconButton
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.EmailTextField
import com.madteam.sunset.ui.common.GenericTextField
import com.madteam.sunset.ui.common.ProfileImage
import com.madteam.sunset.ui.common.RoundedLightChangeImageButton
import com.madteam.sunset.ui.common.SmallButtonDark
import com.madteam.sunset.ui.common.UsernameTextField
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyL
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS
import com.madteam.sunset.utils.BackPressHandler
import com.madteam.sunset.utils.Resource

@Composable
fun BottomSheetEditProfileScreen(
    onCloseButton: () -> Unit,
    onProfileUpdated: (UserProfile) -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel()
) {

    val username by viewModel.username.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val name by viewModel.name.collectAsStateWithLifecycle()
    val userImage by viewModel.userImage.collectAsStateWithLifecycle()
    val location by viewModel.location.collectAsStateWithLifecycle()
    val dataHasChanged by viewModel.dataHasChanged.collectAsStateWithLifecycle()
    val uploadProgress by viewModel.updloadProgress.collectAsStateWithLifecycle()

    BackPressHandler {
        onCloseButton()
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.updateSelectedProfileImage(uri)
            }
        }
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height((LocalConfiguration.current.screenHeightDp * 0.98).dp),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        BottomSheetEditProfileContent(
            usernameValue = username,
            emailValue = email,
            nameValue = name,
            locationValue = location,
            userImage = userImage,
            updateName = viewModel::updateName,
            updateLocation = viewModel::updateLocation,
            saveData = viewModel::updateData,
            onEditProfileImageClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            dataHasChanged = dataHasChanged,
            uploadProgress = uploadProgress,
            clearUploadProgress = viewModel::clearUpdateProgressState,
            onCloseButton = onCloseButton,
            onProfileUpdated = onProfileUpdated
        )
    }
}

@Composable
fun BottomSheetEditProfileContent(
    usernameValue: String,
    emailValue: String,
    nameValue: String,
    locationValue: String,
    userImage: String,
    updateName: (String) -> Unit,
    updateLocation: (String) -> Unit,
    saveData: () -> Unit,
    onEditProfileImageClick: () -> Unit,
    dataHasChanged: Boolean,
    uploadProgress: Resource<String>,
    clearUploadProgress: () -> Unit,
    onCloseButton: () -> Unit,
    onProfileUpdated: (UserProfile) -> Unit
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        CustomSpacer(size = 16.dp)
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CloseIconButton { onCloseButton() }
            CustomSpacer(size = 16.dp)
            Text(text = "Edit profile", style = secondarySemiBoldHeadLineS)
        }
        CustomSpacer(size = 36.dp)
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
        ) {
            val (profileImage, changeImageButton) = createRefs()
            ProfileImage(
                imageUrl = userImage,
                size = 150.dp,
                modifier = Modifier.constrainAs(profileImage) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            RoundedLightChangeImageButton(onClick = { onEditProfileImageClick() },
                modifier = Modifier.constrainAs(changeImageButton)
                {
                    bottom.linkTo(profileImage.bottom)
                    start.linkTo(profileImage.end, (-32).dp)
                })
        }
        CustomSpacer(size = 36.dp)
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Username", style = secondarySemiBoldBodyL, color = Color(0xFF333333))
            CustomSpacer(size = 8.dp)
            UsernameTextField(
                usernameValue = usernameValue,
                onValueChange = {/* to do */ },
                enabled = false
            )
            CustomSpacer(size = 16.dp)
            Text(text = "Email address", style = secondarySemiBoldBodyL, color = Color(0xFF333333))
            CustomSpacer(size = 8.dp)
            EmailTextField(emailValue = emailValue, onValueChange = {/* to do */ }, enabled = false)
            CustomSpacer(size = 16.dp)
            Text(text = "Name", style = secondarySemiBoldBodyL, color = Color(0xFF333333))
            CustomSpacer(size = 8.dp)
            GenericTextField(
                value = nameValue,
                onValueChange = {
                    updateName(it)
                },
                hint = R.string.name
            )
            CustomSpacer(size = 16.dp)
            Text(text = "Location", style = secondarySemiBoldBodyL, color = Color(0xFF333333))
            CustomSpacer(size = 8.dp)
            GenericTextField(
                value = locationValue,
                onValueChange = {
                    updateLocation(it)
                },
                hint = R.string.location
            )
            CustomSpacer(size = 16.dp)
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            SmallButtonDark(onClick = {
                saveData()
            }, text = R.string.save, enabled = dataHasChanged)
            CustomSpacer(size = 24.dp)
        }
    }

    when (uploadProgress) {
        is Resource.Loading -> {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.background(Color.Transparent)
                ) {
                    CircularLoadingDialog()
                }
            }
        }

        is Resource.Success -> {
            if (uploadProgress.data != "") {
                LaunchedEffect(key1 = uploadProgress.data) {
                    Toast.makeText(context, R.string.data_updated, Toast.LENGTH_LONG).show()
                    clearUploadProgress()
                    onProfileUpdated(
                        UserProfile(
                            username = usernameValue,
                            email = emailValue,
                            provider = "",
                            creation_date = "",
                            name = nameValue,
                            location = locationValue,
                            image = userImage,
                            admin = false
                        )
                    )
                }
            } else if (uploadProgress.data.contains("Error")) {
                Toast.makeText(context, "Error, try again later.", Toast.LENGTH_SHORT).show()
                clearUploadProgress()
            }
        }

        else -> {}
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBottomSheetEditProfileContent() {

}