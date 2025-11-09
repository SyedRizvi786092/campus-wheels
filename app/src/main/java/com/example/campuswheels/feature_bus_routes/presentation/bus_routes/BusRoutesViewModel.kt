package com.example.campuswheels.feature_bus_routes.presentation.bus_routes

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuswheels.core.domain.models.Route
import com.example.campuswheels.core.util.Resource
import com.example.campuswheels.feature_bus_routes.domain.repository.BusRouteRepository
//import com.example.campuswheels.feature_bus_routes.domain.use_case.BusRouteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusRoutesViewModel @Inject constructor(
    private val busRouteRepository: BusRouteRepository
//    private val busRouteUseCases: BusRouteUseCases
) : ViewModel(){
    var uiState by mutableStateOf(BusRoutesUiState())
        private set

    var busRoutesState = mutableStateListOf<Route>()
        private set

    init {
        getAllBusRoutes()
    }

    fun onSearchInputChange(newVal : String) {
        uiState = uiState.copy(
            searchInput = newVal
        )
        val searchInput = newVal.trim()
        uiState = if(searchInput.isNotEmpty()){
            uiState.copy(
                busRoutes = uiState.allRoutes.filter {
                    it.routeNo.contains(searchInput, true)
                            || it.name.contains(searchInput, true)
                }
            )
        } else {
            uiState.copy(busRoutes = uiState.allRoutes)
        }
    }

    fun getAllBusRoutes() {
        uiState = uiState.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            busRouteRepository.fetchAllBusRoutes(
                onSuccess = { routes ->
                    busRoutesState = routes.toMutableStateList()
                    uiState = uiState.copy(isLoading = false)
                },
                onFailure = { uiState = uiState.copy(isLoading = false, error = it.message) }
            )
        }
    }

//    fun getAllBusRoutes(isLoading : Boolean = false, isRefreshing : Boolean = false){
//        if(uiState.isLoading || uiState.isRefreshing){
//            return
//        }
//        busRouteUseCases.getAllBusRoutes().onEach { result->
//            uiState = when(result){
//                is Resource.Success ->{
//                    uiState.copy(
//                        allRoutes = result.data?: emptyList(),
//                        busRoutes = result.data?: emptyList(),
//                        isLoading = false,
//                        isRefreshing = false,
//                        error = null
//                    )
//                }
//                is Resource.Error ->{
//                    uiState.copy(error = result.message, isLoading = false, isRefreshing = false)
//                }
//                is Resource.Loading ->{
//                    uiState.copy(isLoading = isLoading, isRefreshing = isRefreshing, error = null)
//                }
//            }
//        }
//            .launchIn(viewModelScope)
//    }
}
