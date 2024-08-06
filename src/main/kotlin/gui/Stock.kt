package gui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import db.Product
import structure.Colores
import structure.database
import java.io.File


@Composable
fun Stock()
{
    val productList = database.productQueries.selectAll().executeAsList()
    var isOpen by remember { mutableStateOf(false) }
    var product: Product? by remember { mutableStateOf(null) }
    val saveObject: (Product) -> Unit = { productoPasado ->
        product = productoPasado
        isOpen = !isOpen
    }
    val changeDialog = {
        isOpen = !isOpen
        product = null
    }
    var filterText by remember { mutableStateOf("") }

    Surface(color = Colores.color1) {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(20.dp)
        ) {
            Column(
                modifier = Modifier.background(Color.White, shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
                    .fillMaxWidth().fillMaxHeight(0.9F)
            ) {
                SearchBar(filterText){  filterText = it }
                LazyColumn {
                    items(
                        productList.filter{it.name.lowercase().contains(filterText.lowercase())}
                    ) { prod -> ProductCard(prod, saveObject)
                    }
                }
            }
            Button(
                onClick = changeDialog,
                colors = ButtonDefaults.buttonColors(Colores.color4),
                shape = RoundedCornerShape(0.dp, 0.dp, 30.dp, 30.dp),
                elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
            ) {
                Text("Añadir producto", color = Colores.color1)
            }
        }
    }
    if (isOpen) {
        if (product != null) {
            ModifyProducto(close = changeDialog, product = product!!)
        } else {
            AddProduct(close = changeDialog)
        }
    }
}

@Composable
fun ProductCard(
    prod: Product,
    saveObject: (Product) -> Unit)
{
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(10.dp, 5.dp, 10.dp, 5.dp)
            .fillMaxWidth()
            .height(50.dp)
            .background(Colores.color2, shape = RoundedCornerShape(10.dp))
            .padding(start = 10.dp)
    ) {
        if (prod.imgPath.contains(":")) {
            Image(
                bitmap = loadImageBitmap(File(prod.imgPath).inputStream()),
                contentDescription = "product image",
                modifier = Modifier.fillMaxWidth(0.04F).fillMaxHeight(0.8F),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(prod.imgPath),
                contentDescription = "product image",
                modifier = Modifier.fillMaxWidth(0.04F).fillMaxHeight(0.8F),
                contentScale = ContentScale.Crop
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(0.7F)
        ) {
            Text(text = prod.name, textAlign = TextAlign.Center, modifier = Modifier.weight(1F))
            Text(
                text = prod.type, textAlign = TextAlign.Center, modifier = Modifier.weight(1F)
            )
            Text(
                text = prod.stock.toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F)
            )
            Text(
                text = "${prod.price}€",
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F)
            )
            Text(
                text = "${prod.pvp}€",
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F)
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(0.6F)
        ) {
            Boton(
                text = "M",
                function = { saveObject(prod) },
                color = Color.Blue.copy(alpha = 0.9f),
                modifier = Modifier.weight(1F)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Boton(
                text = "X",
                function = { database.productQueries.delete(prod.name) },
                color = Color.Red.copy(alpha = 0.9f),
                modifier = Modifier.weight(1F)
            )
        }
        Spacer(
            modifier = Modifier.width(0.dp)
        )
    }
}

@Composable
fun SearchBar(
    filterText: String,
    changeFilterText: (String) -> Unit)
{
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 5.dp).fillMaxWidth().height(50.dp)

    ) {
        Spacer(modifier = Modifier.fillMaxWidth(0.05F))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(0.7F)
        ) {
            Text(
                text = "Nombre",
                color = Colores.color4,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F)
            )
            Text(
                text = "Categoría",
                color = Colores.color4,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F)
            )
            Text(
                text = "Stock",
                color = Colores.color4,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F)
            )
            Text(
                text = "Coste",
                color = Colores.color4,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F)
            )
            Text(
                text = "PVP",
                color = Colores.color4,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F)
            )
        }

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(0.6F)
        ) {

            CustomTextField(
                text = filterText,
                valueChange = changeFilterText,
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
                    .background(color = Colores.color1, shape = RoundedCornerShape(10.dp)),
                centered = false,
                placeholderText = "  Buscar ⌕"
            )
        }
        Spacer(modifier = Modifier.width(0.dp))
    }
}

@Composable
fun AddProduct(
    close: () -> Unit
) {
    var filePath by remember { mutableStateOf("") }
    var imageBitmap: ImageBitmap? = null
    if (filePath != "") {
        val file = File(filePath)
        imageBitmap = remember(file) {
            loadImageBitmap(file.inputStream())
        }
    }
    var showFilePicker by remember { mutableStateOf(false) }


    Dialog(onDismissRequest = close) {
        val pattern = remember { Regex("^\\d*\\.?\\d*\$") }
        var name by remember { mutableStateOf("") }
        var stock by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }
        var pvp by remember { mutableStateOf("") }

        val typeList = remember { listOf("Refrescos", "Cocteles", "Comida") }
        var type by remember { mutableStateOf("Refrescos") }
        val changeType: (String) -> Unit = { type = it }

        Column(
            modifier = Modifier.clip(RoundedCornerShape(20.dp)).width(600.dp).height(350.dp)
                .background(Colores.color1, shape = RoundedCornerShape(15.dp)).padding(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85F)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth(0.3F).fillMaxHeight()
                ) {

                    imageBitmap?.let {
                        Image(
                            painter = BitmapPainter(image = imageBitmap),
                            contentDescription = "Logo product",
                            modifier = Modifier.clip(RoundedCornerShape(10.dp))
                                .clickable(onClick = { showFilePicker = true })
                                .fillMaxWidth()
                                .fillMaxHeight(0.5F)
                                .background(Color.White)
                        )
                    } ?: run {
                        Image(
                            painter = painterResource("prodImgs/noImage.png"),
                            contentDescription = "Logo producto",
                            alpha = 0.5F,
                            modifier = Modifier.clip(RoundedCornerShape(10.dp))
                                .clickable(onClick = { showFilePicker = true })
                                .fillMaxWidth().fillMaxHeight(0.5F)
                                .background(Color.White)
                        )
                    }

                    ComboBox(type, typeList, changeType)
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxHeight(0.9F).padding(start = 40.dp)
                ) {
                    Text("Nombre")
                    Text("Stock")
                    Text("Coste")
                    Text("PVP")
                }

                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxHeight(0.9F).fillMaxWidth(0.7F)
                ) {
                    CustomTextField(
                        text = name,
                        valueChange = { name = it },
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                        centered = false,
                        placeholderText = "Nombre"
                    )
                    CustomTextField(
                        text = stock,
                        valueChange = { if (it.matches(Regex("^[0-9]{0,6}?"))) stock = it },
                        modifier = Modifier.height(50.dp)
                            .fillMaxWidth()
                            .padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                        centered = false,
                        placeholderText = "0u"
                    )
                    CustomTextField(
                        text = price,
                        valueChange = { if (it.isEmpty() || it.matches(pattern) && it.length <= 6) price = it },
                        modifier = Modifier.height(50.dp)
                            .fillMaxWidth()
                            .padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                        centered = false
                    )
                    CustomTextField(
                        text = pvp,
                        valueChange = { if (it.isEmpty() || it.matches(pattern) && it.length <= 6) pvp = it },
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                        centered = false
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().fillMaxHeight()
            ) {
                Boton("Cancelar", function = close)
                Boton("Añadir", function = {
                    if (!(name.isEmpty() || price.isEmpty() || pvp.isEmpty() || stock.isEmpty() || filePath.isEmpty())) {
                        database.productQueries.insert(
                            name,
                            price.toDouble(),
                            pvp.toDouble(),
                            stock.toLong(),
                            filePath,
                            type
                        )
                        close()
                    }
                })
            }
        }
    }

    val fileType = listOf("jpg", "png")
    FilePicker(
        show = showFilePicker,
        fileExtensions = fileType
    ) { platformFile ->
        showFilePicker = false
        filePath = platformFile?.path ?: filePath
    }
}

@Composable
fun ModifyProducto(
    close: () -> Unit,
    product: Product
) {
    var filePath by remember { mutableStateOf(product.imgPath) }
    var imageBitmap: ImageBitmap? = null
    if (!filePath.startsWith("prodImgs")) {
        val file = File(filePath)
        imageBitmap = remember(file) {
            loadImageBitmap(file.inputStream())
        }
    }
    var showFilePicker by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = close) {
        val pattern = remember { Regex("^\\d*\\.?\\d*\$") }
        var name by remember { mutableStateOf(product.name) }
        var stock by remember { mutableStateOf(product.stock.toString()) }
        var price by remember { mutableStateOf(product.price.toString()) }
        var pvp by remember { mutableStateOf(product.pvp.toString()) }

        val typeList = remember { listOf("Refrescos", "Cocteles", "Comida") }
        var type by remember { mutableStateOf(product.type) }
        val changeType: (String) -> Unit = { type = it }

        Column(
            modifier = Modifier.clip(RoundedCornerShape(20.dp)).width(600.dp).height(350.dp)
                .background(Colores.color1, shape = RoundedCornerShape(15.dp)).padding(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85F)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth(0.3F).fillMaxHeight()
                ) {

                    imageBitmap?.let {
                        Image(
                            painter = BitmapPainter(image = imageBitmap),
                            contentDescription = "Logo producto",
                            modifier = Modifier.clip(RoundedCornerShape(10.dp))
                                .clickable(onClick = { showFilePicker = true }).fillMaxWidth().fillMaxHeight(0.5F)
                                .background(Color.White)
                        )
                    } ?: run {
                        Image(
                            painter = painterResource(product.imgPath),
                            contentDescription = "Logo producto",
                            modifier = Modifier.clip(RoundedCornerShape(10.dp))
                                .clickable(onClick = { showFilePicker = true }).fillMaxWidth().fillMaxHeight(0.5F)
                                .background(Color.White)
                        )
                    }

                    ComboBox(type, typeList, changeType)
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxHeight(0.9F).padding(start = 40.dp)
                ) {
                    Text("Nombre")
                    Text("Stock")
                    Text("Coste")
                    Text("PVP")
                }

                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxHeight(0.9F).fillMaxWidth(0.7F)
                ) {
                    CustomTextField(
                        text = name,
                        valueChange = { name = it },
                        modifier = Modifier.height(50.dp).fillMaxWidth().padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                        centered = false,
                        placeholderText = "Nombre"
                    )
                    CustomTextField(
                        text = stock,
                        valueChange = { if (it.matches(Regex("^[0-9]{0,6}?"))) stock = it },
                        modifier = Modifier.height(50.dp).fillMaxWidth().padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                        centered = false,
                        placeholderText = "0u"
                    )
                    CustomTextField(
                        text = price,
                        valueChange = { if (it.isEmpty() || it.matches(pattern) && it.length <= 6) price = it },
                        modifier = Modifier.height(50.dp).fillMaxWidth().padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                        centered = false
                    )
                    CustomTextField(
                        text = pvp,
                        valueChange = { if (it.isEmpty() || it.matches(pattern) && it.length <= 6) pvp = it },
                        modifier = Modifier.height(50.dp).fillMaxWidth().padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                        centered = false
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().fillMaxHeight()
            ) {
                Boton("Cancelar", function = close)
                Boton("Guardar", function = {
                    if (!(name.isEmpty() || price.isEmpty() || pvp.isEmpty() || stock.isEmpty() || filePath.isEmpty())) {
                        database.productQueries.update(
                            product.name,
                            price.toDouble(),
                            pvp.toDouble(),
                            stock.toLong(),
                            filePath,
                            type,
                            product.name
                        )
                        close()
                    }
                })
            }
        }
    }

    val fileType = listOf("jpg", "png")
    FilePicker(
        show = showFilePicker,
        fileExtensions = fileType
    ) { platformFile ->
        showFilePicker = false
        filePath = platformFile?.path ?: filePath
    }
}
