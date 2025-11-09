package com.example.campuswheels.feature_home.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.campuswheels.core.domain.models.UniversityBus
import com.example.campuswheels.core.util.DateTimeUtil.formatTime

@Composable
fun BusInfoDialog(bus: UniversityBus, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Bus Info")
        },
        text = {
            Column {
                Text("Vehicle No.: ${bus.vehicleNumber}")
                Text("Route No.: ${bus.route.routeNumber}")
                bus.currentLocation?.let { Text("Last Updated: ${formatTime(bus.currentLocation.timestamp)}") }
                // Add more fields if needed
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
            ) {
                Text("OK")
            }
        }
    )
}
