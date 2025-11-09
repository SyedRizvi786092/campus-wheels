package com.example.campuswheels.feature_home.presentation.home

import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuswheels.core.domain.models.BusLocation
import com.example.campuswheels.core.domain.models.UniversityBus
import com.example.campuswheels.core.domain.repository.LocationRepository
import com.example.campuswheels.core.util.LoggerUtil
import com.example.campuswheels.core.util.Resource
//import com.example.campuswheels.feature_bus.domain.use_cases.GetNearbyBusesUseCase
//import com.example.campuswheels.feature_bus_stop.domain.use_case.BusStopUseCases
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
//    private val busStopUseCases: BusStopUseCases,
//    private val nearbyBusesUseCase: GetNearbyBusesUseCase,
    private val locationRepository: LocationRepository,
    private val firebaseAuth: FirebaseAuth
): ViewModel(){
    private val logger = LoggerUtil(c= "HomeVieModel")
    var uiState by mutableStateOf(HomeUiState())
        private set

    var isLoading by mutableStateOf(true)
        private set
    var locationState by mutableStateOf<Location?>(null)
        private set
//    var busLocationsState = mutableStateListOf<BusLocation>() // delete later
//        private set
    private val _universityBuses = MutableStateFlow<List<UniversityBus>>(emptyList())
    val universityBuses: StateFlow<List<UniversityBus>> = _universityBuses.asStateFlow()

    init {
        getDeviceLocation()
        getUniversityBuses()
//        fetchAllBusLocations()
        simulateAllBuses()
//        observeLiveUpdates()
//        getLocationBusesStops()
    }

    private fun getDeviceLocation() {
        locationRepository.getLocation(
            onSuccess = { location ->
                locationState = location
            },
            onError = { exc ->
                Log.w("LOC", "getDeviceLocation: Exception occurred!", exc)
            }
        )
    }

    fun postDeviceLocation() {
        getDeviceLocation()
        viewModelScope.launch(Dispatchers.IO) {
            locationState?.let {
                locationRepository.shareLocation(
                    userId = firebaseAuth.currentUser?.uid!!,
                    latitude = it.latitude,
                    longitude = it.longitude
                )
            } ?: logger.error(Exception("Error while posting location!"), "Reason: Location is null.")
        }
    }

//    private fun fetchAllBusLocations() {
//        viewModelScope.launch(Dispatchers.IO) {
//            busLocationsState = locationRepository.getAllBusLocations().toMutableStateList()
//        }
//    }

    private fun getUniversityBuses() {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepository.getUniversityBuses()
                .onStart { isLoading = true }
                .onEach { buses ->
                    _universityBuses.value = buses
                    isLoading = false
                }
                .launchIn(this)
        }
    }

    private fun simulateAllBuses() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
//                busLocationsState.forEachIndexed { index, bus ->
//                    locationRepository.simulateBusMovement(
//                        bus = bus,
//                        onSuccess = { updatedBus ->
//                            busLocationsState[index] = updatedBus
//                        }
//                    )
//                }
                val currentBusList = _universityBuses.value.toMutableList()
                currentBusList.forEachIndexed { index, bus ->
                    locationRepository.simulateBusMovement(
                        bus = bus,
                        onSuccess = { updatedBus ->
                            currentBusList[index] = updatedBus
                            _universityBuses.value = currentBusList
                        }
                    )
                    delay(1000L)
                }
                delay(10000L)
            }
        }
    }

//    private fun observeLiveUpdates() {
//        viewModelScope.launch(Dispatchers.IO) {
//            locationRepository.observeBusLocations { updatedLocations ->
//                busLocationsState.clear()
//                busLocationsState.addAll(updatedLocations)
//            }
//        }
//    }

//    fun getLocationBusesStops(){
//        if( uiState.isLoadingLocation){
//            return
//        }
//        uiState = uiState.copy(isLoadingLocation = true)
//        locationRepository.getCurrentLocation(
//            callback = {
////                logger.info("current location : $it","getLocationBusesStops")
//                uiState = uiState.copy(location = it, errorLocation = null, isLoadingLocation = false)
//                getNearbyBuses(isLoading = true)
//                getNearbyStops(isLoading = true)
//            },
//            onError = {
//                uiState = uiState.copy(errorLocation = it.message, isLoadingLocation = false)
//            },
//            isLive = false,
//        )
//        locationRepository.getLocation(
//            onSuccess = {
//                uiState = uiState.copy(location = it, errorLocation = null)
//                getNearbyBuses(isLoading = true)
//                getNearbyStops(isLoading = true)
//            },
//            onError = {
//                uiState = uiState.copy(errorLocation = it.message)
//            }
//        )
//        uiState = uiState.copy(isLoadingLocation = false)
//    }

//    private fun getAllBusStops(isLoading : Boolean = false, isRefreshing : Boolean = false ){
//        if( uiState.isLoadingLocation || uiState.isLoadingNearbyStops || uiState.isRefreshingNearbyStops){
//            return
//        }
//        busStopUseCases.getAllBusStops().onEach { result->
//            uiState = when(result){
//                is Resource.Success ->{
//                    uiState.copy(
//                        nearbyBusStops = result.data?: emptyList(),
//                        isLoadingNearbyStops = false,
//                        isRefreshingNearbyStops = false,
//                        errorNearbyBuses = null,
//                    )
//                }
//                is Resource.Error ->{
//                    uiState.copy(
//                        errorNearbyBuses = result.message,
//                        isLoadingNearbyStops = false,
//                        isRefreshingNearbyStops = false,
//                    )
//                }
//                is Resource.Loading ->{
//                    uiState.copy(
//                        errorNearbyBuses = null,
//                        isLoadingNearbyStops = isLoading,
//                        isRefreshingNearbyStops = isRefreshing,
//                    )
//                }
//            }
//        }
//            .launchIn(viewModelScope)
//    }

//    fun getNearbyStops(isLoading : Boolean = false, isRefreshing : Boolean = false ){
//        if( uiState.isLoadingLocation || uiState.isLoadingNearbyStops || uiState.isRefreshingNearbyStops){
//            return
//        }
//        if(uiState.location==null){
//            uiState = uiState.copy(errorNearbyStops = "Couldn't fetch current location")
//            return
//        }
//        busStopUseCases.getNearbyBusStops(
//            uiState.location!!.latitude,
//            uiState.location!!.longitude
//        ).onEach { result->
//            uiState = when(result){
//                is Resource.Success ->{
//                    uiState.copy(
//                        nearbyBusStops = result.data?: emptyList(),
//                        isLoadingNearbyStops = false,
//                        isRefreshingNearbyStops = false,
//                        errorNearbyBuses = null,
//                    )
//                }
//                is Resource.Error ->{
//                    uiState.copy(
//                        errorNearbyBuses = result.message,
//                        isLoadingNearbyStops = false,
//                        isRefreshingNearbyStops = false,
//                    )
//                }
//                is Resource.Loading ->{
//                    uiState.copy(
//                        errorNearbyBuses = null,
//                        isLoadingNearbyStops = isLoading,
//                        isRefreshingNearbyStops = isRefreshing,
//                    )
//                }
//            }
//        }
//            .launchIn(viewModelScope)
//    }
//
//    fun getNearbyBuses(isLoading : Boolean = false, isRefreshing : Boolean = false ){
//        if( uiState.isLoadingLocation || uiState.isLoadingNearbyBuses || uiState.isRefreshingNearbyBuses){
//            return
//        }
//        if(uiState.location==null){
//            uiState = uiState.copy(errorNearbyStops = "Couldn't fetch current location")
//            return
//        }
//
//        nearbyBusesUseCase(
//            uiState.location!!.latitude,
//            uiState.location!!.longitude
//        ).onEach { result->
//            uiState = when(result){
//                is Resource.Success ->{
//                    uiState.copy(
//                        nearbyBuses = result.data?: emptyList(),
//                        isLoadingNearbyBuses = false,
//                        isRefreshingNearbyBuses = false,
//                        errorNearbyBuses = null,
//                    )
//                }
//                is Resource.Error ->{
//                    uiState.copy(
//                        errorNearbyBuses = result.message,
//                        isLoadingNearbyBuses = false,
//                        isRefreshingNearbyBuses = false,
//                    )
//                }
//                is Resource.Loading ->{
//                    uiState.copy(
//                        errorNearbyBuses = null,
//                        isLoadingNearbyBuses = isLoading,
//                        isRefreshingNearbyBuses = isRefreshing,
//                    )
//                }
//            }
//        }
//            .launchIn(viewModelScope)
//    }
}

//    private val _uiState = MutableHomeUiState()
//    val uiState: HomeUiState = _uiState
//
//    init {
//        getNearbyBuses(isLoading = true)
//        getNearbyStops(isLoading = true)
//    }
//
//    fun getNearbyStops(isLoading : Boolean = false, isRefreshing : Boolean = false ){
//        if(uiState.isLoadingNearbyStops || uiState.isRefreshingNearbyStops){
//            return
//        }
//        busStopUseCases.getAllBusStops().onEach { result->
//            when(result){
//                is Resource.Success ->{
//                    _uiState.apply {
//                        nearbyBusStops = result.data?: emptyList()
//                        isLoadingNearbyStops = false
//                        isRefreshingNearbyStops = false
//                        errorNearbyBuses = null
//                    }
//                }
//                is Resource.Error ->{
//                    _uiState.apply{
//                        errorNearbyBuses = result.message
//                        isLoadingNearbyStops = false
//                        isRefreshingNearbyStops = false
//                    }
//                }
//                is Resource.Loading ->{
//                    _uiState.apply{
//                        errorNearbyBuses = null
//                        isLoadingNearbyStops = isLoading
//                        isRefreshingNearbyStops = isRefreshing
//                    }
//                }
//            }
//        }
//            .launchIn(viewModelScope)
//    }
//
//    fun getNearbyBuses(isLoading : Boolean = false, isRefreshing : Boolean = false ){
//        if(uiState.isLoadingNearbyBuses || uiState.isRefreshingNearbyBuses){
//            return
//        }
//        busUseCases.getAllBuses().onEach { result->
//            when(result){
//                is Resource.Success ->{
//                    _uiState.apply {
//                        nearbyBuses = result.data ?: emptyList()
//                        isLoadingNearbyBuses = false
//                        isRefreshingNearbyBuses = false
//                        errorNearbyBuses = null
//                    }
//                }
//                is Resource.Error ->{
//                    _uiState.apply {
//                        errorNearbyBuses = result.message
//                        isLoadingNearbyBuses = false
//                        isRefreshingNearbyBuses = false
//                    }
//                }
//                is Resource.Loading ->{
//                    _uiState.apply {
//                        errorNearbyBuses = null
//                        isLoadingNearbyBuses = isLoading
//                        isRefreshingNearbyBuses = isRefreshing
//                    }
//                }
//            }
//        }
//            .launchIn(viewModelScope)
//    }
