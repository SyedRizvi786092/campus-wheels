//package com.example.campuswheels.feature_bus_routes.data.remote.socket
//
//import com.example.campuswheels.core.util.Constants
//import io.socket.client.IO
//import io.socket.client.Socket
//
//class BusRouteSocketIO(token : String){
//
//    private var socket: Socket = IO.socket("${Constants.socketBaseUrl}/user?token=$token")
//
//    fun connect() {
//        socket.connect()
//    }
//
//    fun emitEvent(eventName: String) {
//        socket.emit(eventName)
//    }
//
//    fun listenEvent(eventName: String) {
//        socket.on(eventName){
//
//        }
//    }
//
//    fun disconnect() {
//        socket.disconnect()
//    }
//}
