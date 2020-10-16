package com.example.chatappexample.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.chatappexample.viewmodel.MainViewModel
import com.example.chatappexample.viewmodel.MainViewModelFactory
import com.example.chatappexample.R
import com.example.chatappexample.databinding.FragmentChattingBinding

class JoinActivity : AppCompatActivity() {
    lateinit var binding : FragmentChattingBinding
    lateinit var viewModel : MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_chatting)
        viewModel = ViewModelProvider(this, MainViewModelFactory()).get(MainViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        with(viewModel) {
            //JoinButton 이벤트가 발생하면 화면을 넘겨줍니다.
            joinButton.observe(this@JoinActivity) {
                startActivity(Intent(applicationContext, ChatActivity::class.java))
            }
        }
    }
}