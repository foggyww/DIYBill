package com.example.diybill.ui.screen.add

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diybill.config.Type
import com.example.diybill.db.Bill
import com.example.diybill.db.BillHelper
import com.example.diybill.db.IOCallback
import com.example.diybill.db.getAppDatabase
import com.example.diybill.utils.AppCache
import com.example.diybill.utils.AppFile
import com.example.diybill.utils.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class AddViewModel:ViewModel() {
    fun insertBill(
        name:String,
        type:Type,
        amount:String,
        date:Date,
        picUris:List<String>,
        ioCallback: IOCallback<Unit>){
        viewModelScope.launch(Dispatchers.IO){
            val uriList = picUris.map{
                val realUri = "${System.currentTimeMillis()}_$it"
                AppCache.getFile(it)?.let { file->
                    AppFile.write("images/$realUri",file.readBytes())
                }
                realUri
            }
            BillHelper.insertBill(
                Bill(
                    -1,
                    name,
                    "",
                    type,
                    amount,
                    date,
                    "",
                    uriList
                ),
                ioCallback
            )
        }
    }

    val imageList  = mutableStateListOf<String>()
}