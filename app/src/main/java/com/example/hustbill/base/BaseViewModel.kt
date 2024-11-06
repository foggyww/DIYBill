package com.example.hustbill.base

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hustbill.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S>(s:S) :ViewModel() {
    protected val _state:MutableStateFlow<S> = MutableStateFlow(s)
    val state:StateFlow<S> = _state.asStateFlow()

    fun toast(msg:String){
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(App.CONTEXT, msg, Toast.LENGTH_SHORT).show()
        }
    }
}