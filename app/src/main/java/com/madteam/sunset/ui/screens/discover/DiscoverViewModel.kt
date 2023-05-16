package com.madteam.sunset.ui.screens.discover

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.ktx.model.polygonOptions
import com.madteam.sunset.utils.googlemaps.MapState
import com.madteam.sunset.utils.googlemaps.calculateCameraViewPoints
import com.madteam.sunset.utils.googlemaps.clusters.ZoneClusterItem
import com.madteam.sunset.utils.googlemaps.clusters.ZoneClusterManager
import com.madteam.sunset.utils.googlemaps.getCenterOfPolygon
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor() : ViewModel() {

  val mapState: MutableState<MapState> = mutableStateOf(
    MapState(
      lastKnownLocation = null,
      clusterItems = listOf(
        ZoneClusterItem(
          id = "zone-1",
          title = "Zone 1",
          snippet = "This is Zone 1.",
          polygonOptions = polygonOptions {
            add(LatLng(49.105, -122.524))
            add(LatLng(49.101, -122.529))
            add(LatLng(49.092, -122.501))
            add(LatLng(49.1, -122.506))
            fillColor(POLYGON_FILL_COLOR)
          }
        ),
        ZoneClusterItem(
          id = "zone-2",
          title = "Zone 2",
          snippet = "This is Zone 2.",
          polygonOptions = polygonOptions {
            add(LatLng(49.110, -122.554))
            add(LatLng(49.107, -122.559))
            add(LatLng(49.103, -122.551))
            add(LatLng(49.112, -122.549))
            fillColor(POLYGON_FILL_COLOR)
          }
        )
      )
    )
  )

  @SuppressLint("MissingPermission")
  fun getDeviceLocation(
    fusedLocationProviderClient: FusedLocationProviderClient
  ) {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
    try {
      val locationResult = fusedLocationProviderClient.lastLocation
      locationResult.addOnCompleteListener { task ->
        if (task.isSuccessful) {
          mapState.value = mapState.value.copy(
            lastKnownLocation = task.result,
          )
        }
      }
    } catch (e: SecurityException) {
      // Show error or something
    }
  }

  fun setupClusterManager(
    context: Context,
    map: GoogleMap,
  ): ZoneClusterManager {
    val clusterManager = ZoneClusterManager(context, map)
    clusterManager.addItems(mapState.value.clusterItems)
    return clusterManager
  }

  fun calculateZoneLatLngBounds(): LatLngBounds {
    // Get all the points from all the polygons and calculate the camera view that will show them all.
    val latLngs = mapState.value.clusterItems.map { it.polygonOptions }
      .map { it.points.map { LatLng(it.latitude, it.longitude) } }.flatten()
    return latLngs.calculateCameraViewPoints().getCenterOfPolygon()
  }

  companion object {
    private val POLYGON_FILL_COLOR = Color.parseColor("#ABF44336")
  }
}