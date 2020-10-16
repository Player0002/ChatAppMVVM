package com.example.chatappexample.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.chatappexample.adapter.ChatAdapter
import com.example.chatappexample.viewmodel.MainViewModel
import com.example.chatappexample.viewmodel.MainViewModelFactory
import com.example.chatappexample.R
import com.example.chatappexample.databinding.ActivityChattingBinding

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChattingBinding
    lateinit var viewModel: MainViewModel
    var chatAdapter = ChatAdapter("NON NAMED") // 처음 채팅 어뎁터의 사용자명을 NON NAMED로 설정해둡니다.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chatting)
        viewModel = ViewModelProvider(this, MainViewModelFactory()).get(MainViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        with(binding){
            chatRecyclerview.adapter = chatAdapter
            chatRecyclerview.layoutManager = LinearLayoutManager(applicationContext)
        }

        with(viewModel) {
            //사용자 이름에 반응하여 값을 업데이트 합니다.
            name.value?.let {
                chatAdapter.updateName(it)
            }
            name.observe(this@ChatActivity){
                chatAdapter.updateName(it)
            }

            //채팅 아이템이 추가될때마다 리스트를 업데이트 합니다.
            chatItems.observe(this@ChatActivity){
                chatAdapter.arrayList = it
                chatAdapter.notifyDataSetChanged()
            }
        }
    }
}