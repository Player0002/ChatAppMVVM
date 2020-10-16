package com.example.chatappexample.viewmodel

import android.util.Log
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatappexample.model.ChatModel
import com.example.chatappexample.service.SingleLiveEvent
import com.example.chatappexample.service.socket.SocketListeners
import com.example.chatappexample.service.socket.SocketManager
import org.json.JSONException
import org.json.JSONObject

class MainViewModel : ViewModel(), SocketListeners {

    //채팅 아이템들의 live Data입니다.
    private var chatItemsLive = MutableLiveData<ArrayList<ChatModel>>()
    val chatItems: LiveData<ArrayList<ChatModel>>
        get() = chatItemsLive

    //채팅아이템을 설정하기 위한 기본적인 데이터 목록입니다.
    private val chatItemsList = arrayListOf<ChatModel>()

    //버튼이 눌리면 발생시킬 이벤트 입니다.
    private val joinButtonEvent = SingleLiveEvent<Button>()
    val joinButton: LiveData<Button>
        get() = joinButtonEvent

    //메시지를 보낼경우 발생시킬 이벤트 입니다.
    private val sendButtonEvent = SingleLiveEvent<Button>()
    val sendButton: LiveData<Button>
        get() = sendButtonEvent

    //사용자명을 기록할 변수들 입니다.
    var name = MutableLiveData<String>();
    var target = MutableLiveData<String>();

    //메시지를 기록할 변수 입니다.
    var message = MutableLiveData<String>()

    //소켓정보를 담을 변수입니다.
    val socket = SocketManager.getSocket()

    //처음 생성되면 이 클래스를 observer에 추가합니다.
    init {
        SocketManager.observe(this)
    }

    //Send 버튼을 눌렀을때, 서버로 메시지를 전송합니다.
    fun sendMessageBtnClick() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("room", target.value)
            jsonObject.put("message", message.value)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        socket.emit("message", jsonObject)
    }

    //방에 입장할때 서버로 메시지를 전송합니다.
    fun joinRoom() {
        val obj = JSONObject()
        obj.put("id", name.value)
        socket.emit("user connect", obj)
    }

    //아이템이 추가될때 기본 리스트에 값을 추가하고, 라이브 데이터 값을 설정합니다.
    fun addItem(chatModel: ChatModel) {
        chatItemsList.add(chatModel)
        chatItemsLive.value = chatItemsList
    }

    //메시지를 받았을 경우에, 리스트에 아이템을 추가합니다.
    override fun onMessageReceive(model: ChatModel) {
        addItem(model)
    }

    override fun onConnect() {
        Log.d("TAG", "CONNECTED!")
    }

    override fun onDisconnect() {
        Log.d("TAG", "DISCONNECTED")
    }

    //채팅방 접속에 성공할경우, 이벤트를 발생시킵니다. 이로써 화면전환을 구현합니다.
    override fun onUserConnect(success: Boolean) {
        if (success) {
            joinButtonEvent.call()
        }
    }

    //메시지를 전송할경우, 자신의 메시지를 표시해줍니다.
    override fun onUserSendMessage(success: Boolean) {
        if (success) {
            val model = ChatModel(user = name.value!!, message = message.value!!)
            addItem(model)
            sendButtonEvent.call()
        }
    }
}