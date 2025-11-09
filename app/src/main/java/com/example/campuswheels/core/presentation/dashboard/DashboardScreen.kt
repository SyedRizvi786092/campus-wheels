package com.example.campuswheels.core.presentation.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.campuswheels.core.presentation.components.BottomNavItem
import com.example.campuswheels.core.presentation.components.LocationPermissionWrapper
import com.example.campuswheels.feature_bus_routes.presentation.bus_routes.BusRoutesScreen
import com.example.campuswheels.feature_home.presentation.home.HomeScreen
import com.example.campuswheels.feature_profile.presentation.profile.ProfileScreen


@Composable
fun DashboardScreen(
    bottomNavViewModel: DashboardViewModel = hiltViewModel(),
    onBusRouteClick: (String) -> Unit,
    onBusStopClick: (String) -> Unit,
    onBusClick: (String) -> Unit,
    onAllBusStopsClick: () -> Unit,
    userType : String

) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                items = {bottomNavViewModel.items},
                selectedItem = {bottomNavViewModel.selectedItem},
                onItemClick = bottomNavViewModel::onItemClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LocationPermissionWrapper {
                when(bottomNavViewModel.selectedItem) {
                    is BottomNavItem.Home -> {
//                        if(userType==Constants.UserType.driver)
                        HomeScreen(
                            onNearbyBusClick = onBusClick,
                            onNearbyBusStopClick = onBusStopClick,
                            onAllBusStopsClick = onAllBusStopsClick
                        )
//                        else
//                            ProfileScreen()
                    }
                    is BottomNavItem.BusRoutes -> {
                        BusRoutesScreen(
                            onRouteItemClick = onBusRouteClick
                        )
                    }
                    is BottomNavItem.Profile -> {
                        ProfileScreen()
                    }
                }
            }

        }
    }
}


@Composable
private fun BottomNavBar(
    items: () -> List<BottomNavItem>,
    selectedItem: () -> BottomNavItem,
    onItemClick: (item: BottomNavItem) -> Unit,
) {

    NavigationBar(
        modifier = Modifier
            .padding(
                horizontal = 12.dp, vertical = 12.dp
            )
            .clip(RoundedCornerShape(12.dp))
        ,
        tonalElevation = 12.dp,
    ) {
        items().forEach{ item->
            val selected = selectedItem() == item

            NavigationBarItem(
                selected = selected,
                alwaysShowLabel = false,
                onClick = {
                    onItemClick(item)
                },
                icon = {
                    if(item.drawableIcon!=null)
                        Icon(painterResource(id = item.drawableIcon!!), contentDescription = null)
                    else
                        Icon(item.icon, contentDescription = item.title)
                },
                label = { Text(text = item.title) },
            )
        }
    }
}
