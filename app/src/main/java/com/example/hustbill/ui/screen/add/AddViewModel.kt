package com.example.hustbill.ui.screen.add

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hustbill.db.Bill
import com.example.hustbill.db.BillHelper
import com.example.hustbill.db.IOCallback
import com.example.hustbill.ui.provider.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddViewModel:ViewModel() {
    fun insertBill(bill: Bill,ioCallback: IOCallback<Unit>){
        viewModelScope.launch(Dispatchers.IO){
            BillHelper.insertBill(
                bill,
                ioCallback
            )
        }
    }
}