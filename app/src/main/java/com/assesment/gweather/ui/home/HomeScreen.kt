package com.assesment.gweather.ui.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.assesment.gweather.ui.weather.CurrentWeatherScreen
import com.assesment.gweather.ui.weather.WeatherHistoryScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
) {
    val context = LocalContext.current
    val permissionRequired = Manifest.permission.ACCESS_FINE_LOCATION

    var showPermissionDialog by remember { mutableStateOf(false) }
    var hasLocationPermission by remember { mutableStateOf(false) }

    val locationPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(), onResult = { isGranted ->
            hasLocationPermission = isGranted
            if (!isGranted) showPermissionDialog = true
        })

    LaunchedEffect(Unit) {
        locationPermissionResultLauncher.launch(permissionRequired)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Weather App") }, actions = {
                IconButton(onClick = onLogout) {
                    Icon(
                        Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                    )
                }
            })
        }) { paddingValues ->
        if (hasLocationPermission) {
            HomeContent(
                modifier = Modifier.padding(paddingValues),
            )
        } else {

            LocationDeniedScreen(
                modifier = Modifier.padding(paddingValues),
                onRequestPermissionAgain = {
                    locationPermissionResultLauncher.launch(permissionRequired)
                },
            )
        }
    }

    if (showPermissionDialog) {
        PermissionDialog(
            isPermanentlyDeclined = !ActivityCompat.shouldShowRequestPermissionRationale(
            context as Activity, permissionRequired
        ), onDismiss = { showPermissionDialog = false }, onOkClick = {
            locationPermissionResultLauncher.launch(permissionRequired)
            showPermissionDialog = false
        }, onGoToAppSettingsClick = {
            context.openAppSettings()
            showPermissionDialog = false
        })
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    modifier: Modifier,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Current Weather", "History")

    Column(modifier = modifier) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTab == index,
                    onClick = { selectedTab = index })
            }
        }

        when (selectedTab) {
            0 -> CurrentWeatherScreen()
            1 -> WeatherHistoryScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDeniedScreen(
    modifier: Modifier = Modifier,
    onRequestPermissionAgain: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOff,
                contentDescription = "Location Denied",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(96.dp)
            )

            Text(
                text = "Location permission is required to see weather data.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onRequestPermissionAgain,
                modifier = Modifier.defaultMinSize(minWidth = 180.dp)
            ) {
                Text("Enable Permission")
            }
        }
    }
}

@Composable
fun PermissionDialog(
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Permission Required") }, text = {
        Text(
            if (isPermanentlyDeclined) {
                "You have permanently denied location permission. Please enable it in app settings."
            } else {
                "This app needs location permission to fetch weather data."
            }
        )
    }, confirmButton = {
        TextButton(
            onClick = {
                if (isPermanentlyDeclined) {
                    onGoToAppSettingsClick()
                } else {
                    onOkClick()
                }
            }) {
            Text(if (isPermanentlyDeclined) "Go to Settings" else "Allow")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    })
}

fun Context.openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null)
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}