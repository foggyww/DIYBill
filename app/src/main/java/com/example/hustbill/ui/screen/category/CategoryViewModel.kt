package com.example.hustbill.ui.screen.category

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.hustbill.base.BaseViewModel
import com.example.hustbill.config.Type
import com.example.hustbill.db.BillHelper
import com.example.hustbill.db.IOCallback
import com.example.hustbill.ui.provider.toast
import com.example.hustbill.ui.screen.home.MainState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryViewModel() : BaseViewModel<CategoryState>(CategoryState()) {


    private var initialed = false
    private var job: Job? = null
    fun init() {
        if (initialed) return
        loadBillList()
        initialed = true
    }

    private fun loadBillList() {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            BillHelper.collectBillListByDateRange(dateRange = state.value.dateRange,
                ioCallback = IOCallback(
                    onCompleted = { flow ->
                        flow.onSuccess { l ->
                            _state.update { old ->
                                val list = l.filter {
                                    old.labelState.labelSelected.contains(it.type)
                                }.reversed()
                                old.copy(
                                    billState = BillState(list),
                                    totalAmount = list.sumOf { if (it.type.isOutlay) -it.amount.toBigDecimal() else it.amount.toBigDecimal() })
                            }
                        }
                            .onError {
                                toast("收集失败" + it.message)
                            }
                            .execute()
                    },
                    onError = { toast("加载失败" + it.message) }
                ))
        }
    }

    fun changeLabel(
        newState: LabelState,
    ) {
        _state.update {
            it.copy(labelState = newState)
        }
        loadBillList()
    }

}