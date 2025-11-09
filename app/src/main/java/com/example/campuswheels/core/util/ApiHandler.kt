//package com.example.campuswheels.core.util
//
//import com.example.campuswheels.core.data.remote.dto.ApiResponse
//import com.google.gson.Gson
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//import retrofit2.HttpException
//
//abstract class ApiHandler {
//    private val logger = LoggerUtil(c = "ApiHandler")
//
//    fun <T> makeRequest(
//        apiCall: suspend () -> ApiResponse<T>,
//        onSuccess : (suspend (T)->Unit)?=null,
//    ): Flow<Resource<T>> = flow{
//        try {
//            emit(Resource.Loading())
//            val apiResponse = apiCall()
//            //success
//            logger.info("res : $apiResponse")
//            if (onSuccess != null) {
//                onSuccess(apiResponse.data)
//            }
//            emit( Resource.Success( apiResponse.data, apiResponse.message ))
//
//        } catch (e: HttpException) {
//            val errorBody = e.response()?.errorBody()?.string()
//            val errorResponse = Gson().fromJson(errorBody, ApiResponse::class.java)
//            logger.error("HttpException $errorResponse")
//            if(errorResponse.message!=null){
//                emit( Resource.Error( errorResponse.message))
//            }else{
//                emit( Resource.Error(e.message ?: e.toString() ))
//            }
//        }catch (e : Exception){
//            logger.error("Exception $e")
//            emit( Resource.Error( e.message ?: e.toString() ))
//        }
//
//    }
//}

package com.example.campuswheels.core.util

import com.example.campuswheels.core.data.remote.dto.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class ApiHandler {
    private val logger = LoggerUtil(c = "ApiHandler")

    fun <T> makeRequest(
        apiCall: suspend () -> ApiResponse<T>,
        onSuccess: (suspend (T) -> Unit)? = null,
    ): Flow<Resource<T>> = flow {
        emit(Resource.Loading())
        try {
            when (val apiResponse = apiCall()) {
                is ApiResponse.Success -> {
                    logger.info("res : $apiResponse")
                    onSuccess?.invoke(apiResponse.data)
                    emit(Resource.Success(apiResponse.data))
                }

                is ApiResponse.Error -> {
                    logger.error("Api error: ${apiResponse.message}")
                    emit(Resource.Error(apiResponse.message))
                }
            }
        } catch (e: Exception) {
            logger.error("Exception: $e")
            emit(Resource.Error(e.message ?: e.toString()))
        }
    }
}
