import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
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
            var fil by remember { mutableStateOf(Filter()) }
            val filChange: (Filter) -> Unit = {
                fil = it
            }

            Filtros(filChange)


            val hora = LocalTime.now()
            val pedidos: SnapshotStateList<Pedido> = remember {
                mutableStateListOf(
                    Pedido(1, fecha(), hora, importe(), "Toño"),
                    Pedido(367, fecha(), hora, importe(), "Camarero 1"),
                    Pedido(23, fecha(), hora, importe(), "Toño"),
                    Pedido(500, fecha(), hora, importe(), "Camarero 2"),
                    Pedido(85, fecha(), hora, importe(), "Toño"),
                    Pedido(75, fecha(), hora, importe(), "Toño"),
                    Pedido(457, fecha(), hora, importe(), "Camarero 3"),
                    Pedido(125, fecha(), hora, importe(), "Camarero 2"),
                    Pedido(824, fecha(), hora, importe(), "Camarero 2"),
                    Pedido(1002, fecha(), hora, importe(), "Camarero 3"),
                    Pedido(253, fecha(), hora, importe(), "Camarero 1"),
                )
            }

            val passImporte: (Pedido) -> Boolean = { pedido ->
                pedido.importe in fil.importeRange || (fil.importeRange.endInclusive == 100f && pedido.importe >= 100f)
            }
            val passCamarero: (Pedido) -> Boolean = { pedido ->
                pedido.camarero == fil.camarero || fil.camarero == "Todos"
            }
            val passFecha: (Pedido) -> Boolean = { pedido ->
                println(fil.fechas)
                pedido.fecha in fil.fechas
            }

            var typeSort by remember { mutableStateOf(1) }
            var asc by remember { mutableStateOf(true) }
            val changeSort: (Int) -> Unit = { type ->
                if (typeSort != type) {
                    typeSort = type
                    asc = true
                } else asc = !asc
            }

            val filterList = pedidos.filter { pedido ->
                pedido.numero in fil.numPedidos && passCamarero(pedido) && passImporte(pedido) && passFecha(pedido)
            }.mySort(typeSort, asc)

            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp).fillMaxWidth().fillMaxHeight(0.05F)
                        .background(Color.Transparent, RoundedCornerShape(10.dp))
                ) {
                    //TextSort("Numero", { changeSort(1) }, typeSort==1, asc, Modifier.weight(1F))
                    TextSort("Numero", { changeSort(1) }, Modifier.weight(1F))
                    TextSort("Fecha", { changeSort(2) }, Modifier.weight(1F))
                    TextSort("Hora", { changeSort(3) }, Modifier.weight(1F))
                    TextSort("Importe", { changeSort(4) }, Modifier.weight(1F))
                    TextSort("Camarero", { changeSort(5) }, Modifier.weight(1F))
                }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight()
                ) {
                    items(filterList.size) { index ->
                        PedidoRow(filterList[index])
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

// " \uD83E\uDC61 \uD83E\uDC6B"
@Composable
fun TextSort(
    text: String, changeSort: () -> Unit, modifier: Modifier = Modifier
) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        color = Colores.color4,
        modifier = modifier.clickable(onClick = changeSort)
    )
}

private fun List<Pedido>.mySort(type: Int, asc: Boolean): List<Pedido> {
    return when (type) {
        1 -> if (asc) this.sortedBy { it.numero } else this.sortedByDescending { it.numero }
        2 -> if (asc) this.sortedBy { it.fecha } else this.sortedByDescending { it.fecha }
        3 -> if (asc) this.sortedBy { it.hora } else this.sortedByDescending { it.hora }
        4 -> if (asc) this.sortedBy { it.importe } else this.sortedByDescending { it.importe }
        5 -> if (asc) this.sortedBy { it.camarero } else this.sortedByDescending { it.camarero }
        else -> this
    }
}


//For Debug
fun importe(): Double {
    return (1..200).random().toDouble()
}

//For Debug
fun fecha(): LocalDate {
    val year = 2024
    val month = (1..6).random()
    val day = (1..28).random()
    return LocalDate.of(year, month, day)
}

@Composable
fun PedidoRow(pedido: Pedido) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().background(Colores.color2, RoundedCornerShape(10.dp)).padding(10.dp)
    ) {
        Text(pedido.numero.toString(), textAlign = TextAlign.Center, modifier = Modifier.weight(1F))
        Text(pedido.fecha.toString(), textAlign = TextAlign.Center, modifier = Modifier.weight(1F))
        Text(
            pedido.hora.format(DateTimeFormatter.ofPattern("HH:mm")),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1F)
        )
        Text(pedido.importe.toString(), textAlign = TextAlign.Center, modifier = Modifier.weight(1F))
        Text(pedido.camarero, textAlign = TextAlign.Center, modifier = Modifier.weight(1F))
    }
}

@Composable
fun Filtros(filChange: (Filter) -> Unit) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.padding(10.dp).fillMaxHeight().fillMaxWidth(0.25F)
            .background(color = Colores.color2, shape = RoundedCornerShape(20.dp)).padding(horizontal = 10.dp)
    ) {
        Text(
            text = "FILTROS:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        var numPedidoMin = 0
        var numPedidoMax = 1000

        Column(
            modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
        ) {
            var sliderPosition by remember { mutableStateOf(0f..1000f) }
            val ajustarRango: (ClosedFloatingPointRange<Float>) -> Unit = { range ->
                sliderPosition = range.start.toInt().toFloat()..range.endInclusive.toInt().toFloat()
            }

            println(String.format("%.2f", 0.0f))
            numPedidoMin = sliderPosition.start.toInt()
            numPedidoMax = sliderPosition.endInclusive.toInt()

            Text(
                text = "Numero pedido: $numPedidoMin - $numPedidoMax", modifier = Modifier.offset(y = 7.dp, x = 6.dp)
            )
            RangeSliderFloat(sliderPosition, ajustarRango, 1000f)
        }

        var camarero = "Todos"

        Surface(color = Color.White, shape = RoundedCornerShape(10.dp)) {
            camarero = EligCamarero()
        }

        var importeMin = 0f
        var importeMax = 100f

        Column(
            modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
        ) {
            val rangeMax = 100f
            var sliderPosition by remember { mutableStateOf(0f..rangeMax) }
            val ajustarRango: (ClosedFloatingPointRange<Float>) -> Unit = { range ->
                if (range.start <= range.endInclusive) {
                    sliderPosition = range
                }
            }
            val steps = (rangeMax / 5).toInt() - 1

            importeMin = ((sliderPosition.start * 100.0).roundToInt() / 100.0).toFloat()
            importeMax = ((sliderPosition.endInclusive * 100.0).roundToInt() / 100.0).toFloat()
            Text(
                text = "Importe: Minimo: $importeMin  Maximo: $importeMax",
                modifier = Modifier.offset(y = 7.dp, x = 6.dp)
            )
            RangeSliderFloat(sliderPosition, ajustarRango, rangeMax, steps)
        }


        var fechaInicio by remember { mutableStateOf(LocalDate.of(2024, 1, 1)) }
        var fechaFinal by remember { mutableStateOf(LocalDate.now()) }
        val cambiarFechaIn: (LocalDate) -> Unit = { fechaInicio = it }
        val cambiarFechaFin: (LocalDate) -> Unit = { fechaFinal = it }
        println("$fechaInicio  $fechaFinal")
        Fechas("Inicio", cambiarFechaIn)
        Fechas("Final", cambiarFechaFin)

        Boton("AplicarFiltros", funcionLista = {
            filChange(
                Filter(
                    numPedidos = numPedidoMin..numPedidoMax,
                    fechas = (fechaInicio..fechaFinal),
                    camarero = camarero,
                    importeRange = importeMin..importeMax
                ),
            )
        })
    }
}


@Composable
fun Fechas(tipo: String, cambiarFechaDate: (LocalDate) -> Unit) {
    var abierto by remember { mutableStateOf(false) }
    val cerrarCalendario = { abierto = false }

    val date: Date = if (tipo == "Inicio") {
        SimpleDateFormat("dd/mm/yyyy").parse("01/01/2024")
    } else Date()

    val loc = Locale.Builder().setRegion("ES").setLanguage("es").build()

    var fecha by remember {
        mutableStateOf(
            SimpleDateFormat("EEE, d MMM (YYYY)", loc).format(date)
        )
    }

    val cambiarFecha: (String?) -> Unit = { abierto = false; fecha = if (it.isNullOrEmpty()) "" else it }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(10.dp))
    ) {
        Text("Fecha $tipo : $fecha")
        IconButton(onClick = { abierto = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Abrir calendario")
        }
    }

    if (abierto) {
        DialogDatePicker(cerrarCalendario, cambiarFecha, cambiarFechaDate)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EligCamarero(): String {
    var visible by remember { mutableStateOf(false) }
    var camarero by remember { mutableStateOf("Todos") }

    ExposedDropdownMenuBox(expanded = false, onExpandedChange = {}) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(10.dp))
        ) {
            Text("Camarero: $camarero")
            IconButton(onClick = { visible = true }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Abrir lista camareros")
            }
        }
        ExposedDropdownMenu(
            expanded = visible, onDismissRequest = { visible = false }, modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            DropdownMenuItem(onClick = { visible = false; camarero = "Todos" }) {
                Text("Todos")
            }
            Divider()
            DropdownMenuItem(onClick = { visible = false; camarero = "Toño" }) {
                Text("Toño")
            }
            Divider()
            DropdownMenuItem(onClick = { visible = false; camarero = "Camarero 2" }) {
                Text("Camarero 2")
            }
            Divider()
            DropdownMenuItem(onClick = { visible = false; camarero = "Camarero 3" }) {
                Text("Camarero 3")
            }
        }
    }
    return camarero
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RangeSliderFloat(
    sliderPosition: ClosedFloatingPointRange<Float>,
    ajustarRango: (ClosedFloatingPointRange<Float>) -> Unit,
    rangeMax: Float,
    steps: Int = 0
) {

    Column {
        RangeSlider(
            value = sliderPosition,
            steps = steps,
            onValueChange = ajustarRango,
            valueRange = 0f..rangeMax,
            colors = SliderDefaults.colors(
                thumbColor = Colores.color3,
                activeTrackColor = Colores.color3,
                activeTickColor = Colores.color1,
                inactiveTrackColor = Colores.color3.copy(alpha = 0.3f)
            ),
//            onValueChangeFinished = {
//            },
        )
    }
}
