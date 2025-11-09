//package com.example.campuswheels.core.util
//
//import com.example.campuswheels.core.domain.repository.UserPrefsRepository
//import okhttp3.Interceptor
//import okhttp3.Response
//import javax.inject.Inject
//
////package com.example.campuswheels.core.util
////
////import com.example.campuswheels.core.domain.repository.UserPrefsRepository
////import kotlinx.coroutines.flow.first
////import kotlinx.coroutines.runBlocking
////import okhttp3.Interceptor
////import okhttp3.Response
////import java.lang.Exception
////import javax.inject.Inject
////
////class AuthInterceptor @Inject constructor(
////    private val userPrefsRepository: UserPrefsRepository
////) : Interceptor {
////    private val logger = LoggerUtil(c = "AuthInterceptor")
////
////    override fun intercept(chain: Interceptor.Chain): Response {
////        try {
////            var request = chain.request()
////            val url = request.url().toString()
////
////            runBlocking {
//////                logger.info("Thread : ${Thread.currentThread().name}")
//////                delay(1000)
////                if (!url.contains("/auth/")) {
////                    val token = userPrefsRepository.getToken.first()
////                    request = request
////                        .newBuilder()
////                        .addHeader("Authorization", token)
////                        .build()
////                }
////            }
////
//////            logger.info("request : ${request.url()}")
////            return chain.proceed(request)
////        } catch (e: Exception) {
////            throw e
////        }
////    }
////}
//
//class AuthInterceptor @Inject constructor(
//    private val userPrefsRepository: UserPrefsRepository
//) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request()
//        return chain.proceed(request)
//    }
//}
