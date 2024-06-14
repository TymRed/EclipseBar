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

@Composable
fun Historial() {
    Surface (color = Colores.color1
    ){
        Row (modifier = Modifier.fillMaxSize().padding(20.dp).background(color = Color.White, shape = RoundedCornerShape(20.dp)), horizontalArrangement = Arrangement.SpaceBetween,
        ){
            Column(modifier = Modifier.padding(10.dp).fillMaxHeight().fillMaxWidth(0.25F).background(color = Colores.color2, shape = RoundedCornerShape(20.dp))
            ) {
                RangeSliderExample()

                EligCamarero()

                RangeSliderExample()


                var abierto by remember { mutableStateOf(false) }
                val gestionCalendar = {
                    abierto = false
                }

                IconButton(
                    onClick = { abierto = true }
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
                }

                if (abierto) {
                    DialogDatePicker(gestionCalendar)
                }
            }
        }
    }
}

@Composable
fun EligCamarero() {
    var visible by remember { mutableStateOf(false) }
    var camarero by remember { mutableStateOf("Toño") }

    Box(modifier = Modifier.fillMaxWidth().background(color = Colores.color6, shape = RoundedCornerShape(20.dp)))
    {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Camarero: $camarero")
            IconButton(
                onClick = { visible = true }
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
            }
        }

        DropdownMenu(
            expanded = visible,
            onDismissRequest = { visible = false },
            modifier = Modifier.align(Alignment.TopEnd).fillMaxWidth(0.3F)
        ) {
            DropdownMenuItem(onClick = { visible = false; camarero = "Toño" }
            ) {
                Text("Toño")
            }
            Divider()
            DropdownMenuItem(onClick = { visible = false;  camarero = "Camarero 1" }
            ) {
                Text("Camarero 1")
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RangeSliderExample() {
    var sliderPosition by remember { mutableStateOf(0f..100f) }
    Column {
        RangeSlider(
            value = sliderPosition,
            steps = 9,
            onValueChange = { range -> sliderPosition = range },
            valueRange = 0f..100f,
            onValueChangeFinished = {
                // launch some business logic update with the state you hold
                // viewModel.updateSelectedSliderValue(sliderPosition)
            },
        )

        val min = String.format("%.2f", sliderPosition.start)
        val max = String.format("%.2f", sliderPosition.endInclusive)
        Text(text = "$min  $max")
    }
}
