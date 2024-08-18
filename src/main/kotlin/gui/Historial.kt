package gui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import datePicker.DialogDatePicker
import db.OrderDB
import structure.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

@Composable
fun Historial() {
    Surface(
        color = Colores.color1
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(20.dp)
                .background(color = Color.White, shape = RoundedCornerShape(20.dp)),
        ) {
            var filter by remember { mutableStateOf(Filter()) }
            val changeFilter: (Filter) -> Unit = {
                filter = it
            }

            Filters(changeFilter)
            OrderList(filter)
        }
    }
}

@Composable
fun OrderList(fil: Filter) {
    val passOrderNumber: (OrderDB) -> Boolean = { pedido ->
        pedido.id in fil.orderNumber
    }
    val passAmount: (OrderDB) -> Boolean = { ord ->
        ord.amount in fil.amountRange || (fil.amountRange.endInclusive == 100f && ord.amount >= 100f)
    }
    val passWaiter: (OrderDB) -> Boolean = { ord ->
        ord.waiter == fil.waiter || fil.waiter == "Todos"
    }
    val passDate: (OrderDB) -> Boolean = { ord ->
        ord.date in fil.dateRange
    }

    var typeSort by remember { mutableStateOf(1) }
    var asc by remember { mutableStateOf(true) }
    val changeSort: (Int) -> Unit = { type ->
        if (typeSort != type) {
            typeSort = type
            asc = true
        } else asc = !asc
    }
    val orders = orderQueries.selectAll().executeAsList()
    val filterList = orders.filter { pedido ->
        passOrderNumber(pedido) && passWaiter(pedido) && passAmount(pedido) && passDate(pedido)
    }.mySort(typeSort, asc)

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp).fillMaxWidth().fillMaxHeight(0.05F)
                .background(Color.Transparent, RoundedCornerShape(10.dp))
        ) {
            TextSort(
                text = getString("Number"),
                { changeSort(1) },
                active = typeSort == 1,
                asc = asc,
                Modifier.weight(1F)
            )
            TextSort(
                text = getString("Date"),
                { changeSort(2) },
                active = typeSort == 2,
                asc = asc,
                Modifier.weight(1F)
            )
            TextSort(
                text = getString("Time"),
                { changeSort(3) },
                active = typeSort == 3,
                asc = asc,
                Modifier.weight(1F)
            )
            TextSort(
                text = getString("Total"),
                { changeSort(4) },
                active = typeSort == 4,
                asc = asc,
                Modifier.weight(1F)
            )
            TextSort(
                text = getString("Waiter"),
                { changeSort(5) },
                active = typeSort == 5,
                asc = asc,
                Modifier.weight(1F)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        ) {
            items(filterList.size) { index ->
                OrderRow(filterList[index])
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun TextSort(
    text: String,
    changeSort: () -> Unit,
    active: Boolean,
    asc: Boolean,
    modifier: Modifier = Modifier
) {
    val arrow = if (active) {
        if (asc) " \uD83E\uDC61"
        else " \uD83E\uDC6B"
    } else ""

    Text(
        text = text + arrow,
        textAlign = TextAlign.Center,
        color = Colores.color4,
        modifier = modifier.clickable(onClick = changeSort).fillMaxHeight().wrapContentHeight()
    )
}

private fun List<OrderDB>.mySort(type: Int, asc: Boolean): List<OrderDB> {
    return when (type) {
        1 -> if (asc) this.sortedBy { it.id } else this.sortedByDescending { it.id }
        2 -> if (asc) this.sortedBy { it.date } else this.sortedByDescending { it.date }
        3 -> if (asc) this.sortedBy { it.time } else this.sortedByDescending { it.time }
        4 -> if (asc) this.sortedBy { it.amount } else this.sortedByDescending { it.amount }
        5 -> if (asc) this.sortedBy { it.waiter } else this.sortedByDescending { it.waiter }
        else -> this
    }
}

@Composable
fun OrderRow(order: OrderDB) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().background(Colores.color2, RoundedCornerShape(10.dp)).padding(10.dp)
    ) {
        Text(order.id.toString(), textAlign = TextAlign.Center, modifier = Modifier.weight(1F))
        Text(order.date.toString(), textAlign = TextAlign.Center, modifier = Modifier.weight(1F))
        Text(
            order.time.format(DateTimeFormatter.ofPattern("HH:mm")),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1F)
        )
        Text(order.amount.toString(), textAlign = TextAlign.Center, modifier = Modifier.weight(1F))
        Text(order.waiter, textAlign = TextAlign.Center, modifier = Modifier.weight(1F))
    }
}

@Composable
fun Filters(
    filChange: (Filter) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxHeight()
            .fillMaxWidth(0.25F)
            .background(color = Colores.color2, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp)
    ) {
        Text(
            text = "${getString("Filters").uppercase()}:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        val max by remember { mutableStateOf((orderQueries.maxId().executeAsOneOrNull()?.max ?: 0).toInt()) }
        var minOrderNumber = 0
        var maxOrderNumber = max

        var slider1Position by remember { mutableStateOf(0f..max.toFloat()) }
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(10.dp))
                .padding(horizontal = 5.dp)
        ) {
            val changeRange: (ClosedFloatingPointRange<Float>) -> Unit = { range ->
                slider1Position = range.start..range.endInclusive
            }
            minOrderNumber = slider1Position.start.roundToInt()
            maxOrderNumber = slider1Position.endInclusive.roundToInt()

            Text(
                text = "${getString("Order number")}: $minOrderNumber - $maxOrderNumber",
                modifier = Modifier.offset(y = 7.dp, x = 6.dp)
            )
            RangeSliderFloat(slider1Position, changeRange, max.toFloat())
        }

        var waiter by remember { mutableStateOf(getString("All")) }
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(10.dp),
        ) {
            val waiters = remember { listOf(getString("All")) + userQueries.selectNames().executeAsList() }
            val changeWaiter: (String) -> Unit = { waiter = it }
            val text = "${getString("Waiter")}: $waiter"
            ComboBox(text, waiters, changeWaiter)
        }

        var minAmount = 0f
        var maxAmount = 100f

        val rangeMax = 100f
        var slider2Position by remember { mutableStateOf(0f..rangeMax) }
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(10.dp))
                .padding(horizontal = 5.dp)
        ) {
            val changeRange: (ClosedFloatingPointRange<Float>) -> Unit = { range ->
                if (range.start <= range.endInclusive) {
                    slider2Position = range
                }
            }
            val steps = (rangeMax / 5).toInt() - 1

            minAmount = ((slider2Position.start * 100.0).toInt() / 100.0).toFloat()
            maxAmount = ((slider2Position.endInclusive * 100.0).toInt() / 100.0).toFloat()
            Text(
                text = "${getString("Amount")}: ${getString("Min")}: $minAmount  ${getString("Max")}: $maxAmount",
                modifier = Modifier.offset(y = 7.dp, x = 6.dp)
            )
            RangeSliderFloat(slider2Position, changeRange, rangeMax, steps)
        }


        var firstDate by remember { mutableStateOf(LocalDate.of(2024, 1, 1)) }
        var secondDate by remember { mutableStateOf(LocalDate.now()) }
        val changeFirstDate: (LocalDate) -> Unit = { firstDate = it }
        val changeSecondDate: (LocalDate) -> Unit = { secondDate = it }
        Dates(true, firstDate, changeFirstDate)
        Dates(false, secondDate, changeSecondDate)

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Boton(
                text = getString("Apply"),
                modifier = Modifier.weight(1F),
                function = {
                    filChange(
                        Filter(
                            orderNumber = minOrderNumber..maxOrderNumber,
                            dateRange = (firstDate..secondDate),
                            waiter = waiter,
                            amountRange = minAmount..maxAmount
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.width(15.dp))
            Boton(
                text = getString("Reset"),
                modifier = Modifier.weight(1F),
                function = {
                    slider1Position = 0f..max.toFloat()
                    waiter = getString("All")
                    slider2Position = 0f..rangeMax
                    firstDate = LocalDate.of(2024, 1, 1)
                    secondDate = LocalDate.now()
                    filChange(Filter())
                }
            )
        }
    }
}

@Composable
fun Dates(
    start: Boolean,
    locDate: LocalDate,
    changeDateLoc: (LocalDate) -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }
    val closeCalendar = { isOpen = false }

    val loc = Locale.Builder().setRegion(lang.uppercase()).setLanguage(lang).build()
    val formatterDate = locDate.format(DateTimeFormatter.ofPattern("EEE, d MMM (YYYY)", loc))

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(start = 5.dp)
    ) {
        Text("${if (start) getString("Start date") else "End date"} : $formatterDate")
        IconButton(onClick = { isOpen = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Open calendar")
        }
    }

    if (isOpen) {
        DialogDatePicker(closeCalendar, changeDateLoc)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RangeSliderFloat(
    sliderPosition: ClosedFloatingPointRange<Float>,
    changeRange: (ClosedFloatingPointRange<Float>) -> Unit,
    rangeMax: Float,
    steps: Int = 0
) {

    Column {
        RangeSlider(
            value = sliderPosition,
            steps = steps,
            onValueChange = changeRange,
            valueRange = 0f..rangeMax,
            colors = SliderDefaults.colors(
                thumbColor = Colores.color3,
                activeTrackColor = Colores.color3,
                activeTickColor = Colores.color1,
                inactiveTrackColor = Colores.color3.copy(alpha = 0.3f)
            ),
            onValueChangeFinished = {
                if (steps == 0) {
                    changeRange(
                        sliderPosition.start.roundToInt().toFloat()..sliderPosition.endInclusive.roundToInt().toFloat()
                    )
                }
            },
        )
    }
}
