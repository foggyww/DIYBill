package com.example.hustbill.ui.screen.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.hustbill.base.BaseViewModel
import com.example.hustbill.config.BillType
import com.example.hustbill.db.Bill
import com.example.hustbill.db.getAppDatabase
import com.example.hustbill.utils.Date
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

    fun init() {
        viewModelScope.launch(Dispatchers.IO){
            getAppDatabase().billBookDao().queryAllBillBook()
        }

        val r = _state.update {
            it.copy(
                billState = BillState(
                    listOf(
                        DayBill(
                            Date(2024, 10, 29),
                            listOf(
                                Bill("老乡鸡", "饮食", BillType.Eating, "-40.20", Date(2024, 10, 29), "微信"),
                                Bill("买卫衣", "买衣服", BillType.Cloth, "-380.60", Date(2024, 10, 29), "支付宝")
                            )
                        ),
                        DayBill(
                            Date(2024, 10, 28),
                            listOf(
                                Bill("老乡鸡", "饮食", BillType.Eating, "-140.99", Date(2024, 10, 29), "微信"),
                                Bill("买卫衣", "买衣服", BillType.Cloth, "-580.00", Date(2024, 10, 29), "支付宝")
                            )
                        ),
                        DayBill(
                            Date(2024, 10, 26),
                            listOf(
                                Bill("老乡鸡", "饮食", BillType.Eating, "-30.00", Date(2024, 10, 29), "微信"),
                                Bill("买卫衣", "买衣服", BillType.Cloth, "-180.00", Date(2024, 10, 29), "支付宝")
                            )
                        )
                    )
                )
            )
        }
    }

}