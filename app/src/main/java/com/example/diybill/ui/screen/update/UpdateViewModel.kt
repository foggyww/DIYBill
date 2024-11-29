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
import kotlinx.coroutines.coroutineScope
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
                        initImageList = it.urls
                    },
                    onError = {
                        App.CONTEXT.toast("账单加载失败！")
                    }
                ))
        }
        initial = true
    }

    fun removeImage(index:Int){
        val url = imageList[index]
        viewModelScope.launch {
            AppFile.delete("images/$url")
        }
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
            initImageList?.forEach {
                if(!uriList.contains(it)){
                    AppFile.delete("images/$it")
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
    private var initImageList :List<String>? = null
    val imageList = mutableStateListOf<Pair<Boolean, String>>()
}