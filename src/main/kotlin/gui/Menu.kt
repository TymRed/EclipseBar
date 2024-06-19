package gui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import structure.*
import java.io.File

@Composable
fun PanelPrincipal() {
    var importe: String by remember { mutableStateOf("") }
    val pattern = remember { Regex("^\\d*\\.?\\d*\$") }
    val cambiarImporte: (String) -> Unit = {
        if (it.isEmpty() || it.matches(pattern) && it.length <= 6) importe = it
    }
    val pedidoItems: SnapshotStateList<ProdInPed> = remember { mutableStateListOf() }
    val borrarItems: () -> Unit = {
        pedidoItems.removeAll(pedidoItems)
        importe = ""
    }

    Surface(color = Colores.color1) {
        Row(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                modifier = Modifier.fillMaxHeight().fillMaxWidth(0.3F),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Pedidos(pedidoItems, importe, cambiarImporte)
                Row(
                    modifier = Modifier.fillMaxHeight(0.9F).fillMaxWidth(),
                ) {
                    Boton(
                        "Borrar",
                        modifier = Modifier.fillMaxHeight().fillMaxWidth().weight(1F),
                        funcionLista = borrarItems
                    )
                    Spacer(modifier = Modifier.fillMaxWidth().weight(0.5F))
                    Boton(
                        "Cobrar",
                        modifier = Modifier.fillMaxHeight().fillMaxWidth().weight(1F),
                        funcionLista = borrarItems
                    )
                }
            }
            CuadradoGrande(pedidoItems)
        }
    }
}

@Composable
fun Pedidos(pedidoItems: MutableList<ProdInPed>, importe: String, cambiarImporte: (String) -> Unit) {
    val importeDouble: Double = if (importe.isEmpty()) 0.0 else importe.toDouble()

    Box(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9F)
            .background(color = Color.White, shape = RoundedCornerShape(20.dp))

    ) {

        val state = rememberLazyListState()

        LazyColumn(
            modifier = Modifier.fillMaxHeight(0.92F).padding(10.dp), state = state
        ) {
            items(pedidoItems) { x ->
                PedidoProduct(x, pedidoItems)
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
        VerticalScrollbar(
            modifier = Modifier.offset(y = 10.dp).fillMaxHeight(0.9F).align(Alignment.TopEnd),
            adapter = rememberScrollbarAdapter(
                scrollState = state
            )
//            ,style = ScrollbarStyle(hoverColor = Color.blue)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    color = Colores.color5,
                    shape = RoundedCornerShape(
                        bottomStart = 20.dp,
                        bottomEnd = 20.dp
                    )
                )
                .padding(10.dp, 0.dp, 10.dp, 0.dp),
        ) {

            val suma = pedidoItems.sumOf { it.producto.precio * it.cantidad }
            Text(
                text = String.format("Total: %.2f€", suma),
                Modifier.weight(1F),
                color = Colores.color1
            )
            CustomTextField(
                importe,
                cambiarImporte,
                modifier = Modifier.fillMaxHeight(0.7F)
                    .background(color = Colores.color1, shape = RoundedCornerShape(10.dp))
            )
            Text(
                text = String.format("Cambio: %.2f€", importeDouble - suma),
                textAlign = TextAlign.Right, modifier = Modifier.weight(1F),
                color = Colores.color1
            )
        }
    }
}

@Composable
fun PedidoProduct(pedido: ProdInPed, pedidoItems: MutableList<ProdInPed>) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().background(Colores.color2, shape = RoundedCornerShape(10.dp)).padding(7.dp)
    ) {

        Text(pedido.producto.nombre, modifier = Modifier.fillMaxWidth().weight(3F))
        Text(pedido.cantidad.toString() + "u", modifier = Modifier.fillMaxWidth().weight(1F))
        Text(
            String.format("%.2f€", pedido.producto.precio * pedido.cantidad),
            modifier = Modifier.fillMaxWidth().weight(1F)
        )

        Button(
            onClick = {
                val index = pedidoItems.indexOf(pedido)
                val c = pedido.cantidad - 1
                if (c > 0) pedidoItems[index] = pedido.copy(cantidad = c)
                else pedidoItems.remove(pedido)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(IntrinsicSize.Max).weight(0.8F)
        )
        {
            Icon(
                Icons.Filled.Close,
                contentDescription = "Abrir calendario",
                tint = Color.White,
                modifier = Modifier.size(27.dp)
            )
        }
    }
}

@Composable
fun CuadradoGrande(pedidoItems: MutableList<ProdInPed>) {
    Column(
        modifier = Modifier.fillMaxHeight().fillMaxWidth(0.98F)
            .background(Color.White, shape = RoundedCornerShape(20.dp))
    ) {
        var tipoEleg by remember { mutableStateOf(1) }
        val textoTipo = when (tipoEleg) {
            1 -> "Todos"
            2 -> "Refrescos"
            3 -> "Cocteles"
            4 -> "Comida"
            else -> ""
        }
        val cambiarTipo: (Int) -> Unit = { index ->
            if (tipoEleg != index) tipoEleg = index
        }

        EligTipo(cambiarTipo, tipoEleg)

        val cardsSelected: MutableList<Producto> = mutableListOf()

        for (producto in listaProductos) {
            if (textoTipo == "Todos") {
                cardsSelected.add(producto)
            } else if (textoTipo == producto.tipo) {
                cardsSelected.add(producto)
            }
        }

        LazyVerticalGrid(columns = GridCells.Fixed(4), contentPadding = PaddingValues(5.dp)) {
            items(
                count = cardsSelected.size,
                key = { index ->
                    index
                },
                itemContent = { index ->
                    val cartItemData = cardsSelected[index]
                    if (textoTipo == "Todos") {
                        MenuItem(cartItemData, pedidoItems)
                    } else if (cartItemData.tipo == textoTipo) {
                        MenuItem(cartItemData, pedidoItems)
                    }
                }
            )
        }
    }
}

@Composable
fun EligTipo(cambiarTipo: (Int) -> Unit, tipoEleg: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxHeight(0.12F).fillMaxWidth().padding(5.dp, 15.dp, 5.dp, 10.dp)
    ) {
        BotonFiltro(
            "TODOS",
            RoundedCornerShape(topStart = 20.dp),
            Modifier.weight(1F),
            tipoEleg == 1
        ) { cambiarTipo(1) }
        BotonFiltro(
            "REFRESCOS",
            modifier = Modifier.weight(1F),
            active = tipoEleg == 2
        ) { cambiarTipo(2) }
        BotonFiltro(
            "CÓCTELES",
            modifier = Modifier.weight(1F),
            active = tipoEleg == 3
        ) { cambiarTipo(3) }
        BotonFiltro(
            "COMIDA",
            RoundedCornerShape(topEnd = 20.dp),
            Modifier.weight(1F),
            tipoEleg == 4
        ) { cambiarTipo(4) }
    }
}

@Composable
fun BotonFiltro(
    texto: String,
    shape: Shape = RoundedCornerShape(0.dp),
    modifier: Modifier,
    active: Boolean,
    activeChange: () -> Unit
) {
    val colorFondo = if (active) Colores.color4 else Colores.color2
    val colorTexto = if (active) Colores.color1 else Colores.color6

    Button(
        onClick = activeChange,
        colors = ButtonDefaults.buttonColors(backgroundColor = colorFondo, contentColor = colorTexto),
        shape = shape,
        modifier = modifier
            .fillMaxWidth(0.1F)
            .fillMaxHeight()
            .padding(horizontal = 10.dp)
    ) {
        Text(texto)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuItem(card: Producto, pedidoItems: MutableList<ProdInPed>) {
    Card(
        backgroundColor = Colores.color1,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.padding(10.dp).fillMaxWidth(0.23F).height(250.dp),
        onClick = {
            val hay = pedidoItems.find { it.producto.nombre == card.nombre }
            if (hay == null) {
                pedidoItems.add(ProdInPed(card, 1))
            } else {
                val index = pedidoItems.indexOf(hay)
                pedidoItems[index] = hay.copy(cantidad = hay.cantidad + 1)
            }
        }
    ) {
        Column(modifier = Modifier.padding(15.dp)) {

            if (card.foto.ruta.startsWith("C:")){
                Image(
                    bitmap = loadImageBitmap(File(card.foto.ruta).inputStream()),
                    contentDescription = card.foto.descripcion,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize(0.7F)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
            else{
                Image(
                    painter = painterResource(card.foto.ruta),
                    contentDescription = card.foto.descripcion,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize(0.7F)
                        .clip(RoundedCornerShape(10.dp))
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
            Text(card.precio.toString())
            Text(card.nombre)
        }
    }
}


@Composable
fun Boton(
    texto: String,
    shape: Shape = RoundedCornerShape(10.dp),
    modifier: Modifier = Modifier,
    funcionLista: () -> Unit
) {
    Button(
        onClick = funcionLista,
        colors = ButtonDefaults.buttonColors(backgroundColor = Colores.color4, contentColor = Colores.color1),
        shape = shape,
        modifier = modifier
    ) {
        Text(texto)
    }
}

@Composable
fun CustomTextField(
    text: String,
    cambio: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = "0.0€",
    centrado: Boolean = true
) {
    val fontSize = 16.sp
    val align = if (centrado) TextAlign.Center else TextAlign.Start

    BasicTextField(
        value = text,
        onValueChange = cambio,
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colors.onSurface,
            fontSize = fontSize,
            textAlign = align
        ),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth(0.15F)
//                    .border(0.dp, Colores.color2, RoundedCornerShape(10.dp))
            ) {
                if (leadingIcon != null) leadingIcon()
                Box {
                    if (text.isEmpty())
                        Text(
                            placeholderText,
                            style = LocalTextStyle.current.copy(
//                            color = structure.Colores.color2.copy(alpha = 0.3f),
                                color = Colores.color2,
                                fontSize = fontSize,
                                textAlign = align,
                                ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        }
    )
}