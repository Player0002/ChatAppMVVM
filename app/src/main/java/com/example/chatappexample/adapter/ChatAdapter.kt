package com.example.chatappexample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappexample.R
import com.example.chatappexample.databinding.ItemMyChatBinding
import com.example.chatappexample.databinding.ItemYourChatBinding
import com.example.chatappexample.model.ChatModel

class ChatAdapter(var id: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //내용을 저장합니다.
    var arrayList: ArrayList<ChatModel> = arrayListOf()

    //현재 앱을 사용하는 사람의 이름을 설정합니다.
    fun updateName(str: String) {
        id = str
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //만약 타입이 1이라면, 자신이 보낸 메시지를 inflate 합니다.
        return if (viewType == 1) {
            Holder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_my_chat,
                    parent,
                    false
                )
            )
        }
        //getItemViewType 에서 뷰타입 2을 리턴받았다면 상대채팅레이아웃을 받은 Holder2를 리턴
        else {
            Holder2(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_your_chat,
                    parent,
                    false
                )
            )
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //자기 메시지라면 자기 메시지를 설정합니다.
        if (holder is Holder) {
            holder.bindViewHolder(arrayList[position])
        }
        //onCreateViewHolder에서 리턴받은 뷰홀더가 Holder2라면 상대의 채팅, item_your_chat의 뷰들을 초기화 해줌
        else if (holder is Holder2) {
            holder.bindViewModel(arrayList[position])
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


    override fun getItemViewType(position: Int): Int {//여기서 뷰타입을 1, 2로 바꿔서 지정해줘야 내채팅 너채팅을 바꾸면서 쌓을 수 있음
        //내 아이디와 arraylist의 name이 같다면 내꺼 아니면 상대꺼
        return if (arrayList[position].user == id) 1 else 2
    }
}

class Holder(private val viewBinding: ItemMyChatBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {
    //친구목록 모델의 변수들 정의하는부분
    fun bindViewHolder(item: Any) {
        viewBinding.item = item as ChatModel
        //View를 강제로 업데이트 합니다.
        viewBinding.executePendingBindings()
    }
}

//상대가친 채팅 뷰홀더
class Holder2(private val viewBinding: ItemYourChatBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {
    fun bindViewModel(item: Any) {
        viewBinding.item = item as ChatModel
        //View를 강제로 업데이트 합니다.
        viewBinding.executePendingBindings()
    }
}