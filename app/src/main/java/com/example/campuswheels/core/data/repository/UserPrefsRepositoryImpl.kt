//package com.example.campuswheels.core.data.repository
//
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.emptyPreferences
//import androidx.datastore.preferences.core.stringPreferencesKey
//import com.example.campuswheels.core.domain.repository.UserPrefsRepository
//import com.example.campuswheels.core.util.Constants
//import com.example.campuswheels.core.util.LoggerUtil
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.map
//import java.lang.Exception
//import javax.inject.Inject
//
//class UserPrefsRepositoryImpl @Inject constructor(
//    private val dataStore: DataStore<Preferences>
//) : UserPrefsRepository {
//    private val logger = LoggerUtil(c = "UserPrefsRepositoryImpl")
//    private object PreferencesKey{
//        val userToken  = stringPreferencesKey("user_token")
//        val userType = stringPreferencesKey("user_type")
//    }
//
//    override suspend fun updateToken(token: String?) {
//        logger.info("$token", "updateToken")
//
//        dataStore.edit { preferences ->
//            preferences[PreferencesKey.userToken] = token?:""
//        }
//    }
//
//    override val getToken : Flow<String> = dataStore.data
//        .catch {
//            if(this is Exception){
//                logger.info("${this.message}", "getToken")
//                emit(emptyPreferences())
//            }
//        }.map { preference ->
//            preference[PreferencesKey.userToken] ?: ""
//        }
//
//
//    override suspend fun updateUserType(userType: String?) {
//        logger.info("$userType", "userType")
//
//        dataStore.edit { preferences ->
//            preferences[PreferencesKey.userType] = userType?:Constants.UserType.passenger
//        }
//    }
//
//    override val getUserType : Flow<String> = dataStore.data
//        .catch {
//            if(this is Exception){
//                logger.info("${this.message}", "getUserType")
//                emit(emptyPreferences())
//            }
//        }.map { preference ->
//            preference[PreferencesKey.userType] ?: Constants.UserType.passenger
//        }
//}

package com.example.campuswheels.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.example.campuswheels.core.domain.repository.UserPrefsRepository
import com.example.campuswheels.core.util.LoggerUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPrefsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPrefsRepository {

    private val logger = LoggerUtil(c = "UserPrefsRepositoryImpl")

    private object PreferencesKey {
        val userId = stringPreferencesKey("user_id")
        val email = stringPreferencesKey("email")
        val isEmailVerified = booleanPreferencesKey("is_email_verified")
    }

    override suspend fun updateUserId(userId: String) {
        logger.info(userId, "updateUserId")
        dataStore.edit { it[PreferencesKey.userId] = userId }
    }

    override val getUserId: Flow<String> = dataStore.data
        .catch { e->
            logger.error("getUserId error: ${e.message}")
            emit(emptyPreferences())
        }.map { it[PreferencesKey.userId] ?: "" }

    override suspend fun updateEmail(email: String) {
        logger.info(email, "updateEmail")
        dataStore.edit { it[PreferencesKey.email] = email }
    }

    override val getEmail: Flow<String> = dataStore.data
        .catch { e->
            logger.error("getEmail error: ${e.message}")
            emit(emptyPreferences())
        }.map { it[PreferencesKey.email] ?: "" }

    override suspend fun updateIsEmailVerified(isVerified: Boolean) {
        logger.info(isVerified.toString(), "updateIsEmailVerified")
        dataStore.edit { it[PreferencesKey.isEmailVerified] = isVerified }
    }

    override val getIsEmailVerified: Flow<Boolean> = dataStore.data
        .catch { e->
            logger.error("getIsEmailVerified error: ${e.message}")
            emit(emptyPreferences())
        }.map { it[PreferencesKey.isEmailVerified] ?: false }
}
