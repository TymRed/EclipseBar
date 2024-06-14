/*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application


@Composable
@Preview
fun second() {
    var nombre by remember { mutableStateOf("usuario") }
    var contrasena by remember { mutableStateOf("contraseña") }

    val verif: Boolean = nombre.contains("a")
    var text by remember { mutableStateOf("iniciar seccion") }

    var offset by remember { mutableStateOf(0f) }

    */
/* Surface (color = Color.Yellow, modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5F)){
        Column (verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ){
            for (i in 1..10) {
                MyRow()
            }
        }
    }*//*

    Surface(color = Color.Yellow, modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5F)) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            for (i in 1..10) {
                MyRow()
            }
        }
//        LazyScrollable()
    }
}

@Composable
fun MyRow() {
    Row (horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().background(Color.Magenta)){
        Text("Coca-Cola")
        Text("Refresco")
        Text("2u")
        Button (onClick = {},
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.height(20.dp))
        {
            Text("M")
        }
        Button (onClick = {},
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            shape = RoundedCornerShape(8.dp))
        {
            Text("X")
        }
    }
}


@Composable
fun LazyScrollable() {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = Color(180, 20, 180))
            .padding(10.dp)
    ) {

        val state = rememberLazyListState()

        LazyColumn(verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxSize().padding(end = 12.dp), state = state) {
            items(100) { x ->
                MyRow()
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = state
            )
        )
    }
}

@Composable
fun TextBox(text: String = "Item") {
    Box(
        modifier = Modifier.height(32.dp)
            .fillMaxWidth()
            .background(color = Color(0, 0, 0, 20))
            .padding(start = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = text)
    }
}


*/
/*hay.cantidad++
hay.remove()
pedidoItems[index]*/

/*                pedidoItems.updateElement(
                    { it.producto.name == card.name },
                    { it.apply { cantidad++ } }
                )*/



/*DropdownMenu(
        expanded = visible, onDismissRequest = { visible = false }, modifier = Modifier.width(IntrinsicSize.Min)
    ) {
        DropdownMenuItem(onClick = { visible = false; camarero = "Toño" }) {
            Text("Toño")
        }
        Divider()
        DropdownMenuItem(onClick = { visible = false; camarero = "Camarero 1" }) {
            Text("Camarero 1")
        }
        Divider()
        DropdownMenuItem(onClick = { visible = false; camarero = "Camarero 1" }) {
            Text("Camarero 1")
        }
        Divider()
        DropdownMenuItem(onClick = { visible = false; camarero = "Camarero 1" }) {
            Text("Camarero 1")
        }
        Divider()
        DropdownMenuItem(onClick = { visible = false; camarero = "Camarero 1" }) {
            Text("Camarero 1")
        }
    }*/