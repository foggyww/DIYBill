package com.example.diybill.ui.screen.home

import androidx.lifecycle.viewModelScope
import com.example.diybill.base.BaseViewModel
import com.example.diybill.config.OutlayType
import com.example.diybill.db.AutoRecordHelper
import com.example.diybill.db.Bill
import com.example.diybill.db.BillHelper
import com.example.diybill.db.IOCallback
import com.example.diybill.utils.getDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel<MainState>(MainState()) {
    val config = MainConfig()

    fun uploadAutoRecord(){
        viewModelScope.launch(Dispatchers.IO){
            AutoRecordHelper.queryAutoRecord(IOCallback(
                onCompleted = { list->
                    list.forEach { auto->
                        BillHelper.insertBill(
                            Bill(
                                -1,
                                auto.msg,
                                "",
                                OutlayType.Other,
                                auto.amount,
                                getDate(),
                                auto.packetName,
                                emptyList()
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
    }

    private var initialed = false
    fun init() {
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
                            toast("加载失败！"+t.message)
                        }
                        .execute()
                }
            ))
        }
        initialed = true
    }

}