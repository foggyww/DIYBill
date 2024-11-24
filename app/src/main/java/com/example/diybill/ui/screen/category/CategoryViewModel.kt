package com.example.diybill.ui.screen.category

import androidx.lifecycle.viewModelScope
import com.example.diybill.base.BaseViewModel
import com.example.diybill.db.BillHelper
import com.example.diybill.db.IOCallback
import com.example.diybill.utils.DateRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    fun changeDateRange(
        newRange:DateRange
    ){
        _state.update {
            it.copy(dateRange = newRange)
        }
        loadBillList()
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