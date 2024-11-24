package com.example.diybill.ui.screen.category

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.diybill.config.OutlayType
import com.example.diybill.config.Type
import com.example.diybill.db.Bill
import com.example.diybill.utils.DateRange
import com.example.diybill.utils.getDate
import com.example.diybill.utils.plus
import java.math.BigDecimal


data class CategoryState(
    val billState:BillState = BillState(),
    val dateRange: DateRange = DateRange(getDate()+(-7),getDate()),
    val labelState: LabelState = LabelState(),
    val totalAmount:BigDecimal = BigDecimal("0.00").setScale(2)
)

@Stable
@Immutable
data class LabelState(
    val labelSelected : List<Type> = OutlayType.entries.toList()
)

@Stable
@Immutable
data class BillState(
    val billList :List<Bill> = emptyList()
)