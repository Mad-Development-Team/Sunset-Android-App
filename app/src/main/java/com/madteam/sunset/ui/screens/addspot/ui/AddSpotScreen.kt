package com.madteam.sunset.ui.screens.addspot.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Brightness4
import androidx.compose.material.icons.outlined.Brightness5
import androidx.compose.material.icons.outlined.Brightness6
import androidx.compose.material.icons.outlined.Brightness7
import androidx.compose.material.icons.outlined.BrightnessLow
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.madteam.sunset.R
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.navigation.SunsetRoutes
import com.madteam.sunset.ui.common.AttributesBigListSelectable
import com.madteam.sunset.ui.common.AutoSlidingCarousel
import com.madteam.sunset.ui.common.CircularLoadingDialog
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.CustomTextField
import com.madteam.sunset.ui.common.DismissAndPositiveDialog
import com.madteam.sunset.ui.common.GoBackTopAppBar
import com.madteam.sunset.ui.common.ScoreSlider
import com.madteam.sunset.ui.common.SunsetButton
import com.madteam.sunset.ui.screens.addpost.ui.MAX_IMAGES_SELECTED
import com.madteam.sunset.ui.screens.addreview.ui.FAVORABLE_ATTRIBUTES
import com.madteam.sunset.ui.screens.addreview.ui.NON_FAVORABLE_ATTRIBUTES
import com.madteam.sunset.ui.screens.addreview.ui.SUNSET_ATTRIBUTES
import com.madteam.sunset.ui.screens.addspot.state.AddSpotUIEvent
import com.madteam.sunset.ui.screens.addspot.state.AddSpotUIState
import com.madteam.sunset.ui.screens.addspot.viewmodel.AddSpotViewModel
import com.madteam.sunset.ui.theme.primaryBoldDisplayS
import com.madteam.sunset.ui.theme.primaryBoldHeadlineL
import com.madteam.sunset.ui.theme.primaryBoldHeadlineM
import com.madteam.sunset.ui.theme.primaryMediumHeadlineS
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondaryRegularHeadlineS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldHeadLineS
import com.madteam.sunset.utils.Resource
import com.madteam.sunset.utils.googlemaps.MapStyles
import com.madteam.sunset.utils.googlemaps.setMapProperties
import com.madteam.sunset.utils.googlemaps.updateCameraLocation
import com.madteam.sunset.utils.shimmerBrush
import kotlinx.coroutines.launch

private const val MAX_CHAR_LENGTH_SPOT_TITLE = 24
private const val MAX_CHAR_LENGTH_SPOT_DESCRIPTION = 580

@Composable
fun AddSpotScreen(
    navController: NavController,
    viewModel: AddSpotViewModel = hiltViewModel(),
    selectedLocation: LatLng = LatLng(0.0, 0.0)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = MAX_IMAGES_SELECTED),
        onResult = { uris -> viewModel.onEvent(AddSpotUIEvent.UpdateSelectedImages(uris)) })

    viewModel.onEvent(AddSpotUIEvent.UpdateSpotLocation(selectedLocation))
    viewModel.onEvent(AddSpotUIEvent.ObtainCountryAndLocality)

    BackHandler {
        viewModel.onEvent(AddSpotUIEvent.ShowExitDialog(true))
    }

    Scaffold(
        topBar = {
            GoBackTopAppBar(
                title = R.string.add_spot,
                onClick = { viewModel.onEvent(AddSpotUIEvent.ShowExitDialog(true)) }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                AddSpotContent(
                    state = state,
                    onAddImagesClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    navigateTo = navController::navigate,
                    navController = navController,
                    exitAddSpot = navController::popBackStack,
                    onImageSelected = { viewModel.onEvent(AddSpotUIEvent.ImageSelected(it)) },
                    onDeleteImagesClick = { viewModel.onEvent(AddSpotUIEvent.DeleteSelectedImage) },
                    onSpotTitleChanged = { viewModel.onEvent(AddSpotUIEvent.UpdateSpotName(it)) },
                    onSpotDescriptionChanged = {
                        viewModel.onEvent(
                            AddSpotUIEvent.UpdateSpotDescription(
                                it
                            )
                        )
                    },
                    onAttributeClicked = { viewModel.onEvent(AddSpotUIEvent.AttributeClicked(it)) },
                    onReviewScoreChanged = { viewModel.onEvent(AddSpotUIEvent.UpdateSpotScore(it)) },
                    clearUploadProgress = { viewModel.onEvent(AddSpotUIEvent.ClearUploadProgress) },
                    clearErrorToast = { viewModel.onEvent(AddSpotUIEvent.ClearErrorToastText) },
                    setShowExitDialog = { viewModel.onEvent(AddSpotUIEvent.ShowExitDialog(it)) },
                    onContinueClick = { viewModel.onEvent(AddSpotUIEvent.CreateNewSpot) },
                    canContinue = (
                            state.spotScore != 0 &&
                                    state.selectedAttributes.isNotEmpty() &&
                                    state.spotName.isNotEmpty() &&
                                    state.spotDescription.isNotEmpty() &&
                                    state.imageUris.isNotEmpty()
                            )
                )
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun AddSpotContent(
    state: AddSpotUIState,
    onImageSelected: (Uri) -> Unit,
    onAddImagesClick: () -> Unit,
    onDeleteImagesClick: () -> Unit,
    onSpotTitleChanged: (String) -> Unit,
    onSpotDescriptionChanged: (String) -> Unit,
    navigateTo: (String) -> Unit,
    navController: NavController,
    onAttributeClicked: (SpotAttribute) -> Unit,
    onReviewScoreChanged: (Float) -> Unit,
    clearUploadProgress: () -> Unit,
    clearErrorToast: () -> Unit,
    exitAddSpot: () -> Unit,
    setShowExitDialog: (Boolean) -> Unit,
    onContinueClick: () -> Unit,
    canContinue: Boolean
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val isLocationSelected =
        (state.spotLocation.longitude != 0.0 && state.spotLocation.latitude != 0.0)
    val cameraPositionState = rememberCameraPositionState()

    if (state.errorToastText != -1) {
        Toast.makeText(context, state.errorToastText, Toast.LENGTH_SHORT).show()
        clearErrorToast()
    }

    if (state.showExitDialog) {
        DismissAndPositiveDialog(
            setShowDialog = { setShowExitDialog(it) },
            dialogTitle = R.string.are_you_sure,
            dialogDescription = R.string.exit_post_dialog,
            positiveButtonText = R.string.discard_changes,
            dismissButtonText = R.string.cancel,
            dismissClickedAction = { setShowExitDialog(false) },
            positiveClickedAction = {
                setShowExitDialog(false)
                exitAddSpot()
            }
        )
    }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        LaunchedEffect(key1 = state.spotLocation) {
            if (state.spotLocation.latitude != 0.0 && state.spotLocation.longitude != 0.0) {
                scope.launch {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            }
        }

        //Add featured images section

        Box {
            AutoSlidingCarousel(
                itemsCount = state.imageUris.size,
                autoSlideDuration = 0,
                itemContent = { index ->
                    GlideImage(
                        model = state.imageUris[index],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.background(shimmerBrush(targetValue = 2000f))
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(388.dp)
            )
            if (state.imageUris.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.add_images_spot),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    style = primaryBoldHeadlineL,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
        LazyRow {
            itemsIndexed(state.imageUris) { _, image ->
                Box(Modifier.size(150.dp)) {
                    GlideImage(
                        model = image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clickable {
                                onImageSelected(image)
                            }
                    )
                    if (image == state.selectedImageUri) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(Color(0xCCFFB600)),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = { onDeleteImagesClick() }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Delete image",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                IconButton(
                    onClick = { onAddImagesClick() },
                    modifier = Modifier
                        .size(150.dp)
                        .background(Color(0xFFFFB600))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add image",
                        tint = Color.White
                    )
                }
            }
        }

        //Add title and description section

        CustomSpacer(size = 16.dp)
        CustomTextField(
            value = state.spotName,
            onValueChange = {
                if (it.length <= MAX_CHAR_LENGTH_SPOT_TITLE) {
                    onSpotTitleChanged(it)
                } else {
                    Toast.makeText(context, R.string.max_characters_reached, Toast.LENGTH_SHORT)
                        .show()
                }
            },
            hint = R.string.add_name_spot,
            textStyle = secondarySemiBoldHeadLineS,
            textColor = Color(0xFF666666),
            maxLines = 2
        )
        CustomTextField(
            value = state.spotDescription,
            onValueChange = {
                if (it.length <= MAX_CHAR_LENGTH_SPOT_DESCRIPTION) {
                    onSpotDescriptionChanged(it)
                } else {
                    Toast.makeText(context, R.string.max_characters_reached, Toast.LENGTH_SHORT)
                        .show()
                }
            },
            hint = R.string.add_description_review,
            textStyle = secondaryRegularHeadlineS,
            textColor = Color(0xFF666666),
            maxLines = 6
        )
        CustomSpacer(size = 16.dp)

        //Add location section

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFFD9D9D9)),
            contentAlignment = Alignment.Center
        ) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize(),
                properties = setMapProperties(mapState = state.mapState, MapStyles.NAKED),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    compassEnabled = false,
                    zoomGesturesEnabled = false,
                    scrollGesturesEnabled = false,
                    indoorLevelPickerEnabled = false,
                    mapToolbarEnabled = false,
                    myLocationButtonEnabled = false,
                    rotationGesturesEnabled = false,
                    scrollGesturesEnabledDuringRotateOrZoom = false,
                    tiltGesturesEnabled = false
                )
            ) {
                SetupMapView(
                    cameraPositionState = cameraPositionState,
                    userLocation = state.spotLocation
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 0f,
                            endY = 1000f
                        )
                    )
            )
            Button(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 20.dp),
                onClick = {
                    if (isLocationSelected) {
                        navigateTo("select_location_screen/lat=${state.spotLocation.latitude}long=${state.spotLocation.longitude}")
                    } else {
                        navigateTo(SunsetRoutes.SelectLocationScreen.route)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB600))
            ) {
                if (!isLocationSelected) {
                    Text(text = stringResource(id = R.string.add_location), color = Color.White)
                } else {
                    Text(
                        text = stringResource(id = R.string.modify_location),
                        color = Color.White
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = state.spotLocationCountry,
                    style = primaryBoldHeadlineM,
                    color = Color.White
                )
                Text(
                    text = state.spotLocationLocality,
                    style = primaryMediumHeadlineS,
                    color = Color.White
                )
            }

        }

        // Add attributes section
        CustomSpacer(size = 24.dp)
        Text(
            text = stringResource(id = R.string.add_attributes_review),
            style = secondarySemiBoldHeadLineS,
            modifier = Modifier.padding(start = 16.dp)
        )
        CustomSpacer(size = 16.dp)
        Text(
            text = stringResource(id = R.string.good_attributes),
            style = secondaryRegularHeadlineS,
            modifier = Modifier.padding(start = 16.dp),
            color = Color(0xFF666666)
        )
        CustomSpacer(size = 8.dp)
        AttributesBigListSelectable(
            attributesList = state.attributesList,
            selectedAttributes = state.selectedAttributes,
            onAttributeClick = { onAttributeClicked(it) },
            filterAttributesBy = FAVORABLE_ATTRIBUTES
        )
        CustomSpacer(size = 16.dp)
        Text(
            text = stringResource(id = R.string.bad_attributes),
            style = secondaryRegularHeadlineS,
            modifier = Modifier.padding(start = 16.dp),
            color = Color(0xFF666666)
        )
        CustomSpacer(size = 8.dp)
        AttributesBigListSelectable(
            attributesList = state.attributesList,
            selectedAttributes = state.selectedAttributes,
            onAttributeClick = { onAttributeClicked(it) },
            filterAttributesBy = NON_FAVORABLE_ATTRIBUTES
        )
        CustomSpacer(size = 16.dp)
        Text(
            text = stringResource(id = R.string.sunset_attributes),
            style = secondaryRegularHeadlineS,
            modifier = Modifier.padding(start = 16.dp),
            color = Color(0xFF666666)
        )
        CustomSpacer(size = 8.dp)
        AttributesBigListSelectable(
            attributesList = state.attributesList,
            selectedAttributes = state.selectedAttributes,
            onAttributeClick = { onAttributeClicked(it) },
            filterAttributesBy = SUNSET_ATTRIBUTES
        )
        CustomSpacer(size = 32.dp)

        //Add score section

        Text(
            text = stringResource(id = R.string.review_score),
            style = primaryBoldHeadlineL,
            modifier = Modifier.padding(start = 16.dp)
        )
        CustomSpacer(size = 4.dp)
        Text(
            text = stringResource(id = R.string.add_review_score),
            style = secondarySemiBoldBodyM,
            modifier = Modifier.padding(start = 16.dp)
        )
        ScoreSlider(
            value = state.spotScore.toFloat(),
            onValueChange = { onReviewScoreChanged(it) },
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = when {
                    state.spotScore < 3 -> {
                        Icons.Outlined.BrightnessLow
                    }

                    state.spotScore < 5 -> {
                        Icons.Outlined.Brightness4
                    }

                    state.spotScore < 7 -> {
                        Icons.Outlined.Brightness5
                    }

                    state.spotScore < 9 -> {
                        Icons.Outlined.Brightness6
                    }

                    state.spotScore > 9 -> {
                        Icons.Outlined.Brightness7
                    }

                    else -> {
                        Icons.Outlined.BrightnessLow
                    }
                },
                contentDescription = "Score icon",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(36.dp)
            )
            CustomSpacer(size = 8.dp)
            Text(text = state.spotScore.toString(), style = primaryBoldDisplayS)
        }
        CustomSpacer(size = 24.dp)
        if (!canContinue) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFB600))
            ) {
                Text(
                    text = stringResource(id = R.string.rules_publish_spot),
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                    color = Color.White,
                    style = secondaryRegularBodyM
                )
            }
            CustomSpacer(size = 24.dp)
        }
        SunsetButton(
            text = R.string.continue_text,
            enabled = canContinue,
            onClick = {
                onContinueClick()
            },
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        CustomSpacer(size = 24.dp)
    }

    when (state.uploadProgress) {
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
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .pointerInput(Unit) {}
                ) {
                    CircularLoadingDialog()
                }
            }
        }

        is Resource.Success -> {
            if (state.uploadProgress.data != "") {
                LaunchedEffect(key1 = state.uploadProgress.data) {
                    navController.popBackStack(
                        route = "spot_detail_screen/spotReference=${state.uploadProgress.data}",
                        inclusive = false
                    )
                    navController.navigate("spot_detail_screen/spotReference=${state.uploadProgress.data}") {
                        popUpTo(SunsetRoutes.AddSpotScreen.route) { inclusive = true }
                    }
                    clearUploadProgress()
                }
            } else if (state.uploadProgress.data.contains("Error")) {
                Toast.makeText(context, "Error, try again later.", Toast.LENGTH_SHORT).show()
                clearUploadProgress()
            }
        }

        else -> {
            //Not necessary
        }
    }
}

@SuppressLint("PotentialBehaviorOverride")
@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun SetupMapView(
    cameraPositionState: CameraPositionState,
    userLocation: LatLng,
) {
    val scope = rememberCoroutineScope()
    MapEffect(key1 = userLocation) { map ->
        if (userLocation.latitude != 0.0 && userLocation.longitude != 0.0) {
            map.updateCameraLocation(scope, cameraPositionState, userLocation, 5f)
        }
    }
}