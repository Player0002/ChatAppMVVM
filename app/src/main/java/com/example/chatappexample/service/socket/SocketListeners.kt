package com.example.chatappexample.service.socket

import com.example.chatappexample.model.ChatModel

interface SocketListeners {
    fun onMessageReceive(model : ChatModel);
    fun onConnect();
    fun onDisconnect();
    fun onUserConnect(success : Boolean);
    fun onUserSendMessage(success: Boolean)
}