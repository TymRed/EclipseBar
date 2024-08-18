package gui

import androidx.compose.foundation.Image
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import db.Product
import structure.*
import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun Menu(username: String) {
    var amount: String by remember { mutableStateOf("") }
    val pattern = remember { Regex("^\\d*\\.?\\d*\$") }
    val changeAmount: (String) -> Unit = {
        if (it.isEmpty() || it.matches(pattern) && it.length <= 6) amount = it
    }
    val orderProducts: SnapshotStateList<ProdInOrder> = remember { mutableStateListOf() }
    val eraseOrder: () -> Unit = {
        orderProducts.removeAll(orderProducts)
        amount = ""
    }

    var productList = remember { database.productQueries.selectAll().executeAsList().toMutableStateList() }
    val updatePL: (List<Product>) -> Unit = {
        productList = it.toMutableStateList()
    }

    Surface(color = Colores.color1) {
        Row(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                modifier = Modifier.fillMaxHeight().fillMaxWidth(0.3F), verticalArrangement = Arrangement.SpaceBetween
            ) {
                val sum = orderProducts.sumOf { it.product.pvp * it.quantity }
                val amountDouble: Double = if (amount.isEmpty()) 0.0 else amount.toDouble()

                var errorText by remember { mutableStateOf("") }

                Orders(orderProducts, amount, sum, amountDouble, changeAmount)
                Row(
                    modifier = Modifier.fillMaxHeight(0.9F).fillMaxWidth(),
                ) {
                    Boton(
                        getString("Erase"),
                        modifier = Modifier.fillMaxHeight().fillMaxWidth().weight(1F),
                        function = eraseOrder
                    )
                    Spacer(modifier = Modifier.fillMaxWidth().weight(0.5F))
                    Boton(
                        getString("Charge"),
                        modifier = Modifier.fillMaxHeight().fillMaxWidth().weight(1F),
                        function = {
                            if (sum == 0.0) {
                                errorText = getString("No products")
                            } else if (amount.isEmpty()) {
                                errorText = getString("Enter the amount")
                            } else if (amountDouble < sum) {
                                errorText = getString("Not enough money")
                            } else {
                                subtractStock(orderProducts, productList, updatePL)
                                orderQueries.insert(
                                    id = (orderQueries.maxId().executeAsOneOrNull()?.max ?: -1L) + 1,
                                    date = LocalDate.now(),
                                    time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).toString(),
                                    amount = "%.2f".format(sum).replace(",", ".").toDouble(),
                                    waiter = username
                                )
                                eraseOrder()
                                amount = ""
                            }
                        }
                    )
                }
                if (errorText.isNotEmpty()) {
                    TextDialog(errorText) { errorText = "" }
                }
            }
            ProductsArea(orderProducts, productList)
        }
    }
}

fun subtractStock(
    orderProducts: SnapshotStateList<ProdInOrder>,
    productList: SnapshotStateList<Product>,
    updatePL: (List<Product>) -> Unit
) {
    for (prodOrd in orderProducts) {
        productQueries.subtractStock(prodOrd.product.stock - prodOrd.quantity, prodOrd.product.name)
        updatePL(productList.map { item ->
            if (item.name == prodOrd.product.name)
                item.copy(stock = prodOrd.product.stock - prodOrd.quantity)
            else
                item
        })
    }
}

@Composable
fun Orders(
    orderProducts: MutableList<ProdInOrder>,
    amount: String,
    sum: Double,
    amountDouble: Double,
    changeAmount: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9F)
            .background(Color.White, shape = RoundedCornerShape(20.dp))
    ) {
        val state = rememberLazyListState()

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(0.92F)
                .padding(10.dp),
            state = state
        ) {
            items(orderProducts) { x ->
                OrderRow(x, orderProducts)
                Spacer(modifier = Modifier.height(7.dp))
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

            Text(
                text = "${getString("Total")}: %.2f€".format(sum), Modifier.weight(1F), color = Colores.color1
            )
            CustomTextField(
                amount,
                changeAmount,
                modifier = Modifier.fillMaxHeight(0.7F)
                    .background(color = Colores.color1, shape = RoundedCornerShape(10.dp))
            )
            Text(
                text = "${getString("Change")}: %.2f€".format(amountDouble - sum),
                textAlign = TextAlign.Right,
                modifier = Modifier.weight(1F),
                color = Colores.color1
            )
        }
    }
}

@Composable
fun OrderRow(
    order: ProdInOrder,
    orderItems: MutableList<ProdInOrder>
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Colores.color2, shape = RoundedCornerShape(10.dp))
            .padding(7.dp)
    ) {

        Text(
            order.product.name,
            modifier = Modifier.fillMaxWidth().weight(3F)
        )
        Text(
            order.quantity.toString() + "u",
            modifier = Modifier.fillMaxWidth().weight(1F)
        )
        Text(
            "%.2f€".format(order.product.cost * order.quantity),
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
                contentDescription = "-1 ${getString("unit")}",
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}

@Composable
fun ProductsArea(orderItems: MutableList<ProdInOrder>, productList: SnapshotStateList<Product>) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.98F)
            .background(Color.White, shape = RoundedCornerShape(20.dp))
    ) {
        var activeType by remember { mutableStateOf(1) }
        val typeText = when (activeType) {
            1 -> "All"
            2 -> "Sodas"
            3 -> "Cocktails"
            4 -> "Food"
            else -> ""
        }
        val changeType: (Int) -> Unit = { index ->
            if (activeType != index) activeType = index
        }

        ChooseType(changeType, activeType)

        val cardsSelected: MutableList<Product> = mutableListOf()

        for (product in productList) {
            if (typeText == product.type || typeText == "All") {
                cardsSelected.add(product)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(5.dp)
        ) {
            items(
                count = cardsSelected.size,
                key = { index -> cardsSelected[index].name },
                itemContent = { index ->
                    val cartItemData = cardsSelected[index]
                    var maxStock by remember { mutableStateOf(false) }
                    val changeStock = { maxStock = !maxStock }
                    MenuItem(cartItemData, orderItems, changeStock)
                    if (maxStock) {
                        TextDialog(getString("Not in stock"), changeStock)
                    }
                }
            )
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
            getString("All").uppercase(),
            RoundedCornerShape(topStart = 20.dp),
            Modifier.weight(1F),
            active = tipoEleg == 1
        ) { cambiarTipo(1) }
        BotonFiltro(
            getString("Sodas").uppercase(),
            modifier = Modifier.weight(1F),
            active = tipoEleg == 2
        ) { cambiarTipo(2) }
        BotonFiltro(
            getString("Cocktails").uppercase(),
            modifier = Modifier.weight(1F),
            active = tipoEleg == 3
        ) { cambiarTipo(3) }
        BotonFiltro(
            getString("Food").uppercase(),
            RoundedCornerShape(topEnd = 20.dp),
            Modifier.weight(1F),
            active = tipoEleg == 4
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
    orderItems: MutableList<ProdInOrder>,
    changeStock: () -> Unit
) {
    val hasStock = card.stock != 0L
    val color = if (!hasStock) Color.LightGray else Colores.color1
    Card(
        backgroundColor = color,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.padding(10.dp).fillMaxWidth(0.23F).height(250.dp),
        enabled = hasStock,
        onClick = {
            val orderProduct = orderItems.find { it.product.name == card.name }
            if (orderProduct == null) {
                if (!hasStock) {
                    changeStock()
                } else {
                    orderItems.add(ProdInOrder(card, 1))
                }
            } else if (orderProduct.quantity.toLong() == card.stock)
                changeStock()
            else {
                val index = orderItems.indexOf(orderProduct)
                orderItems[index] = orderProduct.copy(quantity = orderProduct.quantity + 1)
            }
        }
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            if (card.imgPath.contains(":")) {
                Image(
                    bitmap = loadImageBitmap(File(card.imgPath).inputStream()),
                    contentDescription = "product image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().fillMaxSize(0.7F).clip(RoundedCornerShape(10.dp))
                )
            } else {
                Image(
                    painter = painterResource(card.imgPath),
                    contentDescription = "product image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().fillMaxSize(0.7F).clip(RoundedCornerShape(10.dp))
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
            Text("${card.pvp}€")
            Text(card.name)
        }
    }
}