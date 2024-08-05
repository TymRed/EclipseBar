package structure

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import db.Product
import java.time.LocalDate
import java.time.LocalTime

data class ProdInOrder(
    var product: Product,
    var quantity: Int
)

data class Order(
    val number: Int,
    val date: LocalDate,
    val time: LocalTime,
    val amount: Double,
    val waiter: String
)

val time: LocalTime = LocalTime.now()
val orders: SnapshotStateList<Order> =
    mutableStateListOf(
        Order(1, date(), time, amount(), "Toño"),
        Order(2, date(), time, amount(), "Camarero 1"),
        Order(3, date(), time, amount(), "Toño"),
        Order(4, date(), time, amount(), "Camarero 2"),
        Order(5, date(), time, amount(), "Toño"),
        Order(6, date(), time, amount(), "Toño"),
        Order(7, date(), time, amount(), "Camarero 3"),
        Order(8, date(), time, amount(), "Camarero 2"),
        Order(9, date(), time, amount(), "Camarero 2"),
        Order(10, date(), time, amount(), "Camarero 3"),
        Order(11, date(), time, amount(), "Camarero 1"),
    )

data class Filter(
    val orderNumber: IntRange = (0..orders.maxBy { it.number }.number),
    val dateRange: ClosedRange<LocalDate> = LocalDate.of(2022, 8, 10)..LocalDate.now(),
    val waiter: String = "Todos",
    val amountRange: ClosedFloatingPointRange<Float> = (0.0f..1000.00f)
)

//For Debug
fun amount(): Double {
    return (1..200).random().toDouble()
}

//For Debug
fun date(): LocalDate {
    val year = 2024
    val month = (1..6).random()
    val day = (1..28).random()
    return LocalDate.of(year, month, day)
}