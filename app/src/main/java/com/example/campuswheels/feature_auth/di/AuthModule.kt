//package com.example.campuswheels.feature_auth.di
//
//import com.example.campuswheels.feature_auth.data.remote.api.AuthApiService
//import com.example.campuswheels.feature_auth.data.repository.AuthRepositoryImpl
//import com.example.campuswheels.feature_auth.domain.repository.AuthRepository
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import kotlinx.coroutines.CoroutineDispatcher
//import retrofit2.Retrofit
//import javax.inject.Singleton
//
//
//@Module
//@InstallIn(SingletonComponent::class)
//object AuthModule {
//
//    @Singleton
//    @Provides
//    fun provideAuthApiService(
//        retrofit: Retrofit
//    ) : AuthApiService = retrofit.create(AuthApiService::class.java)
//
//    @Singleton
//    @Provides
//    fun provideAuthRepository(
//        authApiService: AuthApiService,
//        dispatcher : CoroutineDispatcher
//    ) : AuthRepository = AuthRepositoryImpl( authApiService, dispatcher)
//
//}

package com.example.campuswheels.feature_auth.di

import com.example.campuswheels.feature_auth.data.repository.AuthRepositoryImpl
import com.example.campuswheels.feature_auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideAuthRepository(
        dispatcher: CoroutineDispatcher,
        firebaseAuth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(dispatcher, firebaseAuth)
}
