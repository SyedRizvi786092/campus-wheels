package com.example.campuswheels.feature_home.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campuswheels.R
import com.example.campuswheels.core.presentation.components.CustomImage
import com.example.campuswheels.ui.theme.Gray100

//@Composable
//fun BusTile(
//    modifier : Modifier = Modifier,
//    routeNo : String,
//    routeName : String,
//    vehNo : String,
//    onClick : ()->Unit,
//){
//    Card(
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.tertiary
//        )
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = modifier
//                .width(130.dp)
//                .clickable {
//                    onClick()
//                }
//                .padding(8.dp)
//        ) {
//            CustomImage(
//                drawableId = R.drawable.locate_bus,
//                color = Gray100,
//                size = 54f,
//            )
//            Text(
//                text = "Veh No:  $vehNo",
//                style = MaterialTheme.typography.labelMedium,
//                maxLines = 1,
//                textAlign = TextAlign.Center,
//
//            )
//            Text(
//                text = "Route No:  $routeNo",
//                style = MaterialTheme.typography.labelSmall,
//                maxLines = 1,
//                textAlign = TextAlign.Center
//            )
//            Text(
//                text = routeName,
//                style = MaterialTheme.typography.labelSmall,
//                maxLines = 1,
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}

@Composable
fun BusTile(
    modifier: Modifier = Modifier,
    routeNo: String,
    routeName: String,
    vehNo: String,
    onClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Optional image — remove if not needed
            CustomImage(
                drawableId = R.drawable.locate_bus,
                color = Gray100,
                size = 48f,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Route No: $routeNo",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = routeName,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Veh No: $vehNo",
                    style = MaterialTheme.typography.labelSmall
                )
            }

            IconButton(onClick = { onInfoClick() }) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Route Info"
                )
            }
        }
    }
}


@Preview
@Composable
private fun BusTilePreview(){
    BusTile(
        routeNo = "52",
        routeName = "Alambagh",
        vehNo = "UP32 TC77",
        onClick = {},
        onInfoClick = {}
    )
}
