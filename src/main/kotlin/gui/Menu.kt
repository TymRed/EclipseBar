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
import androidx.compose.ui.window.Dialog
import structure.*
import java.io.File

@Composable
fun Menu() {
    var amount: String by remember { mutableStateOf("") }
    val pattern = remember { Regex("^\\d*\\.?\\d*\$") }
    val changeAmount: (String) -> Unit = {
        if (it.isEmpty() || it.matches(pattern) && it.length <= 6) amount = it
    }
    val orderProducts: SnapshotStateList<ProdInOrder> = remember { mutableStateListOf() }
    val eraseItems: () -> Unit = {
        orderProducts.removeAll(orderProducts)
        amount = ""
    }

    Surface(color = Colores.color1) {
        Row(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                modifier = Modifier.fillMaxHeight().fillMaxWidth(0.3F), verticalArrangement = Arrangement.SpaceBetween
            ) {
                Orders(orderProducts, amount, changeAmount)
                Row(
                    modifier = Modifier.fillMaxHeight(0.9F).fillMaxWidth(),
                ) {
                    Boton(
                        "Borrar",
                        modifier = Modifier.fillMaxHeight().fillMaxWidth().weight(1F),
                        function = eraseItems
                    )
                    Spacer(modifier = Modifier.fillMaxWidth().weight(0.5F))
                    Boton(
                        "Cobrar",
                        modifier = Modifier.fillMaxHeight().fillMaxWidth().weight(1F),
                        function = eraseItems
                    )
                }
            }
            CuadradoGrande(orderProducts)
        }
    }
}

@Composable
fun Orders(
    orderProducts: MutableList<ProdInOrder>,
    amount: String,
    changeAmount: (String) -> Unit
) {
    val amountDouble: Double = if (amount.isEmpty()) 0.0 else amount.toDouble()

    Box(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9F)
            .background(color = Color.White, shape = RoundedCornerShape(20.dp))

    ) {
        val state = rememberLazyListState()

        LazyColumn(
            modifier = Modifier.fillMaxHeight(0.92F).padding(10.dp), state = state
        ) {
            items(orderProducts) { x ->
                PedidoProduct(x, orderProducts)
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
        VerticalScrollbar(
            modifier = Modifier.offset(y = 10.dp).fillMaxHeight(0.9F).align(Alignment.TopEnd),
            adapter = rememberScrollbarAdapter(
                scrollState = state
            )
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.align(alignment = Alignment.BottomCenter).fillMaxWidth().height(50.dp).background(
                color = Colores.color5, shape = RoundedCornerShape(
                    bottomStart = 20.dp, bottomEnd = 20.dp
                )
            ).padding(10.dp, 0.dp, 10.dp, 0.dp),
        ) {

            val sum = orderProducts.sumOf { it.product.price * it.quantity }
            Text(
                text = String.format("Total: %.2f€", sum), Modifier.weight(1F), color = Colores.color1
            )
            CustomTextField(
                amount,
                changeAmount,
                modifier = Modifier.fillMaxHeight(0.7F)
                    .background(color = Colores.color1, shape = RoundedCornerShape(10.dp))
            )
            Text(
                text = String.format("Cambio: %.2f€", amountDouble - sum),
                textAlign = TextAlign.Right,
                modifier = Modifier.weight(1F),
                color = Colores.color1
            )
        }
    }
}

@Composable
fun PedidoProduct(
    order: ProdInOrder,
    orderItems: MutableList<ProdInOrder>
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().background(Colores.color2, shape = RoundedCornerShape(10.dp)).padding(7.dp)
    ) {

        Text(order.product.name, modifier = Modifier.fillMaxWidth().weight(3F))
        Text(order.quantity.toString() + "u", modifier = Modifier.fillMaxWidth().weight(1F))
        Text(
            String.format("%.2f€", order.product.price * order.quantity),
            modifier = Modifier.fillMaxWidth().weight(1F)
        )
        Button(
            onClick = {
                val index = orderItems.indexOf(order)
                val c = order.quantity - 1
                if (c > 0) orderItems[index] = order.copy(quantity = c)
                else orderItems.remove(order)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(IntrinsicSize.Max).weight(0.8F)
        ) {
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
fun CuadradoGrande(pedidoItems: MutableList<ProdInOrder>) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.98F)
            .background(Color.White, shape = RoundedCornerShape(20.dp))
    ) {
        var activeType by remember { mutableStateOf(1) }
        val typeText = when (activeType) {
            1 -> "Todos"
            2 -> "Refrescos"
            3 -> "Cocteles"
            4 -> "Comida"
            else -> ""
        }
        val changeType: (Int) -> Unit = { index ->
            if (activeType != index) activeType = index
        }

        ChooseType(changeType, activeType)

        val cardsSelected: MutableList<Product> = mutableListOf()

        for (product in productList) {
            if (typeText == "Todos") {
                cardsSelected.add(product)
            } else if (typeText == product.type) {
                cardsSelected.add(product)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(5.dp)
        ) {
            items(
                count = cardsSelected.size,
                key = { index -> index },
                itemContent = { index ->
                    val cartItemData = cardsSelected[index]
                    var maxStock by remember { mutableStateOf(false) }
                    val changeStock = { maxStock = !maxStock }
                    if (typeText == "Todos" || cartItemData.type == typeText) {
                        MenuItem(cartItemData, pedidoItems, changeStock)
                    }
                    if (maxStock) {
                        TextDialog("No hay más stock", changeStock)
                    }
                }
            )
        }
    }
}

@Composable
fun TextDialog(
    text: String,
    changeStock: () -> Unit
) {
    Dialog(
        onDismissRequest = changeStock
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clip(RoundedCornerShape(16.dp)).width(300.dp).height(150.dp)
                .background(Colores.color1, RoundedCornerShape(16.dp)).background(Color.Red.copy(alpha = 0.1f))
                .clickable(onClick = changeStock).padding(horizontal = 30.dp)
        ) {
            Icon(Icons.Filled.Close, contentDescription = "Cerrar", tint = Color.Red)
            Text(text, color = Color.Red, fontSize = 20.sp)
        }
    }
}

@Composable
fun ChooseType(
    cambiarTipo: (Int) -> Unit,
    tipoEleg: Int
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxHeight(0.12F).fillMaxWidth().padding(5.dp, 15.dp, 5.dp, 10.dp)
    ) {
        BotonFiltro(
            "TODOS", RoundedCornerShape(topStart = 20.dp), Modifier.weight(1F), tipoEleg == 1
        ) { cambiarTipo(1) }
        BotonFiltro(
            "REFRESCOS", modifier = Modifier.weight(1F), active = tipoEleg == 2
        ) { cambiarTipo(2) }
        BotonFiltro(
            "CÓCTELES", modifier = Modifier.weight(1F), active = tipoEleg == 3
        ) { cambiarTipo(3) }
        BotonFiltro(
            "COMIDA", RoundedCornerShape(topEnd = 20.dp), Modifier.weight(1F), tipoEleg == 4
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
        modifier = modifier.fillMaxWidth(0.1F).fillMaxHeight().padding(horizontal = 10.dp)
    ) {
        Text(texto)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuItem(
    card: Product,
    pedidoItems: MutableList<ProdInOrder>,
    changeStock: () -> Unit
) {
    Card(backgroundColor = Colores.color1,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.padding(10.dp).fillMaxWidth(0.23F).height(250.dp),
        onClick = {
            val prodPed = pedidoItems.find { it.product.name == card.name }
            if (prodPed == null) {
                if (card.stock == 0) {
                    changeStock()
                } else {
                    pedidoItems.add(ProdInOrder(card, 1))
                }
            } else if (prodPed.quantity == card.stock) {
                changeStock()
            } else {
                val index = pedidoItems.indexOf(prodPed)
                pedidoItems[index] = prodPed.copy(quantity = prodPed.quantity + 1)
            }

        }
    ) {
        Column(modifier = Modifier.padding(15.dp)) {

            if (card.photo.ruta.contains(":")) {
                Image(
                    bitmap = loadImageBitmap(File(card.photo.ruta).inputStream()),
                    contentDescription = card.photo.desctiption,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().fillMaxSize(0.7F).clip(RoundedCornerShape(10.dp))
                )
            } else {
                Image(
                    painter = painterResource(card.photo.ruta),
                    contentDescription = card.photo.desctiption,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().fillMaxSize(0.7F).clip(RoundedCornerShape(10.dp))
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
            Text(card.price.toString())
            Text(card.name)
        }
    }
}


@Composable
fun Boton(
    texto: String,
    shape: Shape = RoundedCornerShape(10.dp),
    modifier: Modifier = Modifier,
    function: () -> Unit,
    color: Color = Colores.color4
) {
    Button(
        onClick = function,
        colors = ButtonDefaults.buttonColors(backgroundColor = color, contentColor = Colores.color1),
        shape = shape,
        modifier = modifier
    ) {
        Text(texto)
    }
}

@Composable
fun CustomTextField(
    text: String,
    valueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = "0.0€",
    centered: Boolean = true
) {
    val fontSize = 16.sp
    val align = if (centered) TextAlign.Center else TextAlign.Start

    BasicTextField(value = text,
        onValueChange = valueChange,
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colors.onSurface, fontSize = fontSize, textAlign = align
        ),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = modifier.fillMaxWidth(0.15F)
            ) {
                if (leadingIcon != null) leadingIcon()
                Box {
                    if (text.isEmpty()) Text(
                        placeholderText, style = LocalTextStyle.current.copy(
                            color = Colores.color2,
                            fontSize = fontSize,
                            textAlign = align,
                        ), modifier = Modifier.fillMaxWidth()
                    )
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        })
}