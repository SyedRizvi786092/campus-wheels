package com.example.campuswheels.feature_bus_routes.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campuswheels.core.domain.models.Stop
import com.example.campuswheels.core.presentation.components.logo.LogoMiniLight
import com.example.campuswheels.ui.theme.White

@Composable
fun BusRouteTile(
    modifier: Modifier = Modifier,
    routeNo: String,
    stops: List<Stop>,
    totalStops: Int? = null,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(8.dp)
    ) {
        LogoMiniLight(
            radius = 70f,
            padding = 0f
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = routeNo,
                style = MaterialTheme.typography.titleSmall
            )

            // Show 1st, 5th and last stop if available
            if (stops.isNotEmpty()) {
                val firstStop = stops.first().name
                val fifthStop = if (stops.size >= 5) stops[4].name else null
                val lastStop = stops.last().name

                val pathText = buildString {
                    append(firstStop)
                    if (fifthStop != null) append(" -> $fifthStop")
                    append(" -> $lastStop")
                }

                Text(
                    text = pathText,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if(totalStops != null)
                Text(
                    text = "Total Stops: $totalStops",
                    style = MaterialTheme.typography.bodySmall
                )
        }
    }
}

@Preview
@Composable
private fun BusRouteTilePreview(){
    BusRouteTile(
        routeNo = "111_UP",
        stops = emptyList(),
        onClick = {},
        totalStops = 20,
        modifier = Modifier.background(White)
    )
}
