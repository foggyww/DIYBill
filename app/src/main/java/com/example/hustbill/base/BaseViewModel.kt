package com.example.hustbill.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<S> :ViewModel() {
    protected abstract val _state:MutableStateFlow<S>
    abstract val state:StateFlow<S>
}