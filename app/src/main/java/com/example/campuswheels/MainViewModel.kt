//package com.example.campuswheels
//
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.campuswheels.core.domain.repository.UserPrefsRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class MainViewModel @Inject constructor(
//    private val userPrefsRepository: UserPrefsRepository,
//    private val savedStateHandle: SavedStateHandle
//) : ViewModel(){
//    private val tokenKey = "token"
//    private val userTypeKey = "userType"
//    private val loadingKey = "loading"
//
//    val token = savedStateHandle.getStateFlow(tokenKey,"")
//    val userType = savedStateHandle.getStateFlow(userTypeKey,"")
//    val loading = savedStateHandle.getStateFlow(loadingKey,false)
//
//    init {
//        viewModelScope.launch {
//            userPrefsRepository.getUserType.collect{
//                savedStateHandle[loadingKey] = true;
//                savedStateHandle[userTypeKey] = it
//                savedStateHandle[loadingKey] = false;
//            }
//        }
//        viewModelScope.launch {
//            savedStateHandle[loadingKey] = true;
//            userPrefsRepository.getToken.collect{
//                savedStateHandle[loadingKey] = true;
//                savedStateHandle[tokenKey] = it
//                if(it.isNotEmpty()){ // To Show Splash if user logged in
//                    delay(1000)
//                }
//                savedStateHandle[loadingKey] = false;
//            }
//            savedStateHandle[loadingKey] = false;
//        }
//    }
//
//
//}

package com.example.campuswheels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuswheels.core.domain.repository.UserPrefsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPrefsRepository: UserPrefsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val emailKey = "email"
    private val userIdKey = "userId"
    private val emailVerifiedKey = "emailVerified"
    private val loadingKey = "loading"

    val email = savedStateHandle.getStateFlow(emailKey, "")
    val userId = savedStateHandle.getStateFlow(userIdKey, "")
    val isEmailVerified = savedStateHandle.getStateFlow(emailVerifiedKey, false)
    val loading = savedStateHandle.getStateFlow(loadingKey, false)

    init {
        viewModelScope.launch {
            savedStateHandle[loadingKey] = true

            userPrefsRepository.getEmail.collect {
                savedStateHandle[emailKey] = it
            }
        }

        viewModelScope.launch {
            savedStateHandle[loadingKey] = true

            userPrefsRepository.getUserId.collect {
                savedStateHandle[userIdKey] = it
                if (it.isNotEmpty()) {
                    delay(1000)
                }
            }
        }

        viewModelScope.launch {
            savedStateHandle[loadingKey] = true

            userPrefsRepository.getIsEmailVerified.collect {
                savedStateHandle[emailVerifiedKey] = it
                savedStateHandle[loadingKey] = false
            }
        }
    }
}
