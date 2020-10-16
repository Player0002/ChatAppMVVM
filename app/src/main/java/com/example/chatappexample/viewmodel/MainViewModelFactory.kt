package com.example.chatappexample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory : ViewModelProvider.Factory{
    //Factory를 구현하기 위함입니다.
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            //두곳에서 같은 viewModel을 Share하기 위해서 하나의 인스턴스만을 생성하도록 고유키를 부여합니다.
            val str = "MAIN_VIEW_MODEL"
            //키가 없다면, 새로운 뷰모델을 생성합니다.
            if(!cache.containsKey(str)) addViewModel(str, MainViewModel())
            return getViewModel(str) as T
        }
        throw IllegalArgumentException("Class arguments is not assigned from MainViewModel.")
    }
    //ViewModel들을 저장하기 위한 cache입니다.
    companion object SavedFactories {
        private val cache = HashMap<String, ViewModel>()
        fun addViewModel(str :String, model : ViewModel) {
            cache[str] = model
        }
        fun getViewModel(str : String) : ViewModel? {
            return cache[str]
        }
    }
}
