package com.example.campuswheels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campuswheels.core.presentation.navigation.Navigation
import com.example.campuswheels.core.presentation.navigation.ScreenRoutes
import com.example.campuswheels.core.util.LoggerUtil
import com.example.campuswheels.ui.theme.BusTrackingAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val logger = LoggerUtil(c = "MainActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BusTrackingAppTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val mainViewModel = viewModel<MainViewModel>()

                    val userId by mainViewModel.userId.collectAsStateWithLifecycle()
                    val isEmailVerified by mainViewModel.isEmailVerified.collectAsStateWithLifecycle()
                    val isLoading by mainViewModel.loading.collectAsStateWithLifecycle()

                    var initialScreen = ScreenRoutes.SplashScreen.route
                    if (!isLoading) {
                        initialScreen = if (userId.isEmpty()) ScreenRoutes.AuthScreen.route
                        else ScreenRoutes.DashboardScreen.route
                    }

                    logger.info("Loading = $isLoading, InitialScreen = $initialScreen, UserId = $userId")

                    Navigation(
                        startDestination = initialScreen,
                        userType = "passenger"
                    )
                }
            }
        }
    }
}
