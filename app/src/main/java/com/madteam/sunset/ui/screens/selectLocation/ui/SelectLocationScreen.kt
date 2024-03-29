package com.madteam.sunset.ui.screens.selectLocation.ui

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.ContinueFAB
import com.madteam.sunset.ui.common.GoBackTopAppBar
import com.madteam.sunset.ui.screens.selectLocation.state.SelectLocationUIEvent
import com.madteam.sunset.ui.screens.selectLocation.state.SelectLocationUIState
import com.madteam.sunset.ui.screens.selectLocation.viewmodel.SelectLocationViewModel
import com.madteam.sunset.utils.getCurrentLocation
import com.madteam.sunset.utils.googlemaps.MapStyles
import com.madteam.sunset.utils.googlemaps.setMapProperties
import com.madteam.sunset.utils.googlemaps.updateCameraLocation
import com.madteam.sunset.utils.hasLocationPermission

@Composable
fun SelectLocationScreen(
    navController: NavController,
    viewModel: SelectLocationViewModel = hiltViewModel(),
    lat: Float = 0f,
    long: Float = 0f
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.onEvent(
        SelectLocationUIEvent.UpdateSelectedLocation(
            LatLng(
                lat.toDouble(),
                long.toDouble()
            )
        )
    )

    Scaffold(
        topBar = {
            GoBackTopAppBar(
                title = R.string.add_location,
                onClick = { navController.popBackStack() }
            )
        },
        floatingActionButton = {
            if (state.selectedLocation.latitude != 0.0 && state.selectedLocation.longitude != 0.0) {
                ContinueFAB {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("location", state.selectedLocation)
                    navController.popBackStack()
                }
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                SelectLocationContent(
                    state = state,
                    updateSelectedLocation = {
                        viewModel.onEvent(
                            SelectLocationUIEvent.UpdateSelectedLocation(
                                it
                            )
                        )
                    },
                    updateUserLocation = {
                        viewModel.onEvent(
                            SelectLocationUIEvent.UpdateUserLocation(
                                it
                            )
                        )
                    },
                    setGoToUserLocation = {
                        viewModel.onEvent(
                            SelectLocationUIEvent.SetGoToUserLocation(
                                it
                            )
                        )
                    }
                )
            }
        }
    )
}

@Composable
fun SelectLocationContent(
    state: SelectLocationUIState,
    updateSelectedLocation: (LatLng) -> Unit,
    updateUserLocation: (LatLng) -> Unit,
    setGoToUserLocation: (Boolean) -> Unit
) {

    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState()
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    getCurrentLocation(context) { lat, long ->
                        updateUserLocation(LatLng(lat, long))
                    }
                }
            }
        )

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = setMapProperties(mapState = state.mapState, mapStyle = MapStyles.DETAILED),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false
        )
    ) {
        SetupListenersAndMapView(
            cameraPositionState = cameraPositionState,
            userLocation = state.userLocation,
            selectedLocation = state.selectedLocation,
            updateSelectedLocation = updateSelectedLocation,
            goToUserLocation = state.goToUserLocation,
            setGoToUserLocation = setGoToUserLocation
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End
    ) {
        IconButton(
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFFFB600)),
            onClick = {
                if (hasLocationPermission(context)) {
                    getCurrentLocation(context) { lat, long ->
                        updateUserLocation(LatLng(lat, long))
                        setGoToUserLocation(true)
                    }
                } else {
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}

@SuppressLint("PotentialBehaviorOverride")
@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun SetupListenersAndMapView(
    cameraPositionState: CameraPositionState,
    userLocation: LatLng,
    updateSelectedLocation: (LatLng) -> Unit,
    selectedLocation: LatLng,
    goToUserLocation: Boolean,
    setGoToUserLocation: (Boolean) -> Unit
) {

    val scope = rememberCoroutineScope()

    MapEffect(key1 = selectedLocation, key2 = goToUserLocation) { map ->
        map.setOnMapClickListener { clickedLatLng ->
            updateSelectedLocation(clickedLatLng)
            map.updateCameraLocation(scope, cameraPositionState, clickedLatLng, 10f)
        }
        if (selectedLocation.latitude != 0.0 && selectedLocation.longitude != 0.0) {
            map.clear()
            map.addMarker(
                MarkerOptions().position(selectedLocation)
            )
        }
        if (userLocation.longitude != 0.0 && userLocation.latitude != 0.0 && goToUserLocation) {
            map.updateCameraLocation(scope, cameraPositionState, userLocation, 18f)
        }
        setGoToUserLocation(false)
    }
}
