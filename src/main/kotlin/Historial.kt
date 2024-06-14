import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

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
            Filtros()
        }
    }
}

@Composable
fun Filtros() {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxHeight()
            .fillMaxWidth(0.25F)
            .background(color = Colores.color2, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp)
    ) {

        Surface(color = Color.White, shape = RoundedCornerShape(10.dp)) {
            val rangeMax = 1000f
            var sliderPosition by remember { mutableStateOf(0f..rangeMax) }
            val ajustarRango: (ClosedFloatingPointRange<Float>) -> Unit =
                { range ->
                    sliderPosition =
                        range.start.toInt().toFloat()..range.endInclusive.toInt().toFloat()
                }

            val min = String.format("%.2f", sliderPosition.start)
            val max = String.format("%.2f", sliderPosition.endInclusive)
            Text(text = "Numero pedido: ${min} - ${max}")
            Spacer(modifier = Modifier.height(10.dp))
            RangeSliderFloat(sliderPosition, ajustarRango, rangeMax)
        }

        Surface(color = Color.White, shape = RoundedCornerShape(10.dp)) {
            EligCamarero()
        }

        Surface(color = Color.White, shape = RoundedCornerShape(10.dp)) {
            val rangeMax = 100f
            var sliderPosition by remember { mutableStateOf(0f..rangeMax) }
            val ajustarRango: (ClosedFloatingPointRange<Float>) -> Unit = { range -> sliderPosition = range }
            val steps = (rangeMax / 5).toInt() - 1

            val min = String.format("%.2f", sliderPosition.start)
            val max = String.format("%.2f", sliderPosition.endInclusive)
            Text(text = "Importe: Minimo: $min  Maximo: $max")
            Spacer(modifier = Modifier.height(10.dp))
            RangeSliderFloat(sliderPosition, ajustarRango, rangeMax, steps)
        }

        fechas("Inicio")
        fechas("Final")

        Boton("AplicarFiltros", funcionLista = {})
    }
}


@Composable
fun fechas(tipo: String){
    var abierto by remember { mutableStateOf(false) }
    val cerrarCalendario = { abierto = false }

    var fecha by remember { mutableStateOf(SimpleDateFormat("EEE, d MMM", Locale("es","Es")).format(Date())) }
    val cambiarFecha: (String) -> Unit = { abierto = false; fecha = it }

    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(10.dp))
    ){
        Text("Fecha $tipo : $fecha")
        IconButton(onClick = { abierto = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Abrir calendario")
        }
    }

    if (abierto) {
        DialogDatePicker(cerrarCalendario, cambiarFecha)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EligCamarero() {
    var visible by remember { mutableStateOf(false) }
    var camarero by remember { mutableStateOf("Toño") }

    ExposedDropdownMenuBox(expanded = false, onExpandedChange = {}) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(10.dp))
        ) {
            Text("Camarero: $camarero")
            IconButton(onClick = { visible = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Abrir lista camareros")
            }
        }
        ExposedDropdownMenu(
            expanded = visible,
            onDismissRequest = { visible = false },
            modifier = Modifier.width(IntrinsicSize.Min)
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
//            onValueChangeFinished = {
//            },
        )
    }
}
