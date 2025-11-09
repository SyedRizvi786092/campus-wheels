//package com.example.campuswheels.core.domain.repository
//
//import kotlinx.coroutines.flow.Flow
//
//
//interface UserPrefsRepository {
//    suspend fun updateToken(token : String?)
//    val getToken : Flow<String>
//
//    suspend fun updateUserType(userType : String?)
//    val getUserType : Flow<String>
//}

package com.example.campuswheels.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPrefsRepository {
//    suspend fun updateToken(token : String?)
//    val getToken : Flow<String>

    suspend fun updateUserId(userId: String)
    val getUserId: Flow<String>

    suspend fun updateEmail(email: String)
    val getEmail: Flow<String>

    suspend fun updateIsEmailVerified(isVerified: Boolean)
    val getIsEmailVerified: Flow<Boolean>
}
