package com.example.chatappexample.service.socket

import com.example.chatappexample.model.ChatModel
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

object SocketManager {
    private var socket: Socket? = null

    private var observers: ArrayList<SocketListeners> = arrayListOf()
    fun getSocket(): Socket {
        synchronized(this) {
            if (socket == null) {
                socket = IO.socket("http://192.168.10.190:8080");

                //서버에 연결되었을때, 모든 observer에게 연결됨을 메인스레드편에서 전송합니다.
                socket!!.on(Socket.EVENT_CONNECT) {
                    for (observer in observers) {
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                observer.onConnect()
                            }
                        }
                    }
                }
                //메시지 전송에 성공할경우, 모든 observer에게 전송됨을 메인스레드편에서 전송합니다.
                socket!!.on("send message") { it ->
                    val data = it[0] as JSONObject
                    val success: Boolean
                    success = data.getBoolean("success")
                    observers.forEach {
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) { it.onUserSendMessage(success) }
                        }
                    }
                }

                //채팅방 접속에 성공할경우, 모든 observer에게 접속됨을 메인스레드편에서 전송합니다.
                socket!!.on("user connect") {

                    val data = it[0] as JSONObject
                    val success: Boolean
                    success = data.getBoolean("success")
                    observers.forEach {
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) { it.onUserConnect(success) }
                        }
                    }
                }

                //메시지를 받을경우, 모든 observer에게 메시지를 받음을 메인스레드편에서 알립니다.
                socket!!.on("message") { args ->
                    val data = args[0] as JSONObject

                    val receiveMessage = data.getString("message")
                    val receiveUser = data.getString("user")
                    observers.forEach {

                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                it.onMessageReceive(
                                    ChatModel(
                                        receiveUser,
                                        receiveMessage
                                    )
                                )
                            }
                        }
                    }
                }

                //소켓 연결이 종료될경우, 모든 observer에게 종료됨을 메인스레드편에서 전송합니다.
                socket!!.on(Socket.EVENT_DISCONNECT) {
                    for (observer in observers) {
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) { observer.onDisconnect() }
                        }
                    }
                }
                socket!!.connect()
            }
            return socket!!;
        }
    }

    fun observe(listener: SocketListeners) {
        //같은 구독자가 2번 구독하는것을 막기 위함입니다.
        if (!observers.contains(listener))
            observers.add(listener)
    }
}