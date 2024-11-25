package com.example.diybill.ui.screen.update

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diybill.App
import com.example.diybill.config.Type
import com.example.diybill.db.Bill
import com.example.diybill.db.BillHelper
import com.example.diybill.db.IOCallback
import com.example.diybill.ui.provider.toast
import com.example.diybill.utils.AppCache
import com.example.diybill.utils.AppFile
import com.example.diybill.utils.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateViewModel : ViewModel() {

    private var initial = false
    val bill: MutableState<Bill?> = mutableStateOf(null)
    fun init(id: Int) {
        if(initial) return
        viewModelScope.launch(Dispatchers.IO) {
            BillHelper.queryBill(id,
                IOCallback(
                    onSuccess = {
                        bill.value = it
                        imageList.addAll(it.urls.map { u->
                            Pair(true,u)
                        })
                    },
                    onError = {
                        App.CONTEXT.toast("账单加载失败！")
                    }
                ))
        }
        initial = true
    }

    fun updateBill(
        name: String,
        type: Type,
        amount: String,
        date: Date,
        picUris: List<Pair<Boolean, String>>,
        ioCallback: IOCallback<Unit>,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val uriList =
                picUris.map {
                    if (!it.first) {
                        val realUri = "${System.currentTimeMillis()}_$it"
                        AppCache.getFile(it.second)?.let { file ->
                            AppFile.write("images/$realUri", file.readBytes())
                        }
                        realUri
                    } else {
                        it.second
                    }
                }
            bill.value?.let {
                BillHelper.updateBill(
                    it.copy(
                        name = name,
                        type = type,
                        amount = amount,
                        date = date,
                        urls = uriList
                    ),
                    ioCallback
                )
            }
        }
    }

    fun deleteBill(
        bill: Bill,
        ioCallback: IOCallback<Unit>
    ){
        viewModelScope.launch(Dispatchers.IO){
            BillHelper.deleteBill(bill,ioCallback)
        }
    }

    val imageList = mutableStateListOf<Pair<Boolean, String>>()
}