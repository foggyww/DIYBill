package com.example.hustbill.ui.screen.home

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.hustbill.base.BaseViewModel
import com.example.hustbill.config.OutlayType
import com.example.hustbill.db.AutoRecordHelper
import com.example.hustbill.db.Bill
import com.example.hustbill.db.BillHelper
import com.example.hustbill.db.IOCallback
import com.example.hustbill.ui.provider.toast
import com.example.hustbill.utils.getDate
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

    private val initialed = false
    fun init(context: Context) {


        viewModelScope.launch(Dispatchers.IO){
            AutoRecordHelper.queryAutoRecord(IOCallback(
                onCompleted = { list->
                        list.forEach { auto->
                            BillHelper.insertBill(
                                Bill(
                                    auto.msg,
                                    "",
                                    OutlayType.Other,
                                    auto.amount,
                                    getDate(),
                                    auto.packetName
                                ),
                                IOCallback(
                                    onCompleted = {
                                        AutoRecordHelper.deleteAutoRecord(auto,IOCallback())
                                    }
                                )
                            )
                        }
                }
            )
            )
        }

        if(initialed) return
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