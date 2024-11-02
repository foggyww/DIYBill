package com.example.hustbill.ui.screen.home

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.hustbill.base.BaseViewModel
import com.example.hustbill.db.BillHelper
import com.example.hustbill.db.IOCallback
import com.example.hustbill.ui.provider.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel<MainState>() {
    override val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState())

    override val state: StateFlow<MainState> = _state.asStateFlow()

    val config = MainConfig()

    fun init(context: Context) {
        viewModelScope.launch(Dispatchers.IO){
            BillHelper.collectOutlayList(IOCallback(
                onCompleted = {
                    it.onSuccess { list->
                        _state.update { s->
                            s.copy(billState = BillState(list.toDayBillList))
                        }
                    }
                        .onError { t->
                            context.toast("加载失败！"+t.message)
                        }
                        .execute()
                }
            ))
        }
    }

}