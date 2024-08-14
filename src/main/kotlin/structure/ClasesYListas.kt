package structure

import db.Product
import java.time.LocalDate

data class ProdInOrder(
    var product: Product,
    var quantity: Int
)

data class Filter(
    val orderNumber: IntRange = (0..(orderQueries.maxId().executeAsOneOrNull()?.max ?: 0).toInt()),
    val dateRange: ClosedRange<LocalDate> = LocalDate.of(2022, 8, 10)..LocalDate.now(),
    val waiter: String = "Todos",
    val amountRange: ClosedFloatingPointRange<Float> = (0.0f..1000.00f)
)