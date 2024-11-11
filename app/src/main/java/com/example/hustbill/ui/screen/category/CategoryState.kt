package com.example.hustbill.ui.screen.category

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.hustbill.config.OutlayType
import com.example.hustbill.config.Type
import com.example.hustbill.db.Bill
import com.example.hustbill.utils.DateLevel
import com.example.hustbill.utils.DateRange
import com.example.hustbill.utils.getDate
import com.example.hustbill.utils.getRange
import java.math.BigDecimal


data class CategoryState(
    val billState:BillState = BillState(),
    val dateRange: DateRange = getDate().getRange(DateLevel.DAY),
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