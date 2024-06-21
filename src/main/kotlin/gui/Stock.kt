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
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import structure.Colores
import structure.Photo
import structure.Producto
import structure.listaProductos
import java.io.File


@Composable
fun Stock() {
    var isOpen by remember { mutableStateOf(false) }
    var producto: Producto? by remember { mutableStateOf(null) }
    val saveObject: (Producto) -> Unit = { productoPasado ->
        producto = productoPasado
        isOpen = !isOpen
    }
    val addProduct: (Producto) -> Unit = { p ->
        listaProductos.add(p)
        isOpen = !isOpen
    }
    val changeProduct: (Producto) -> Unit = { p ->
        listaProductos[listaProductos.indexOf(producto!!)] = p
        producto = null
        isOpen = !isOpen
    }

    val changeDialog = {
        isOpen = !isOpen
        producto = null
    }


    Surface(color = Colores.color1) {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(20.dp)
        ) {
            Column(
                modifier = Modifier.background(Color.White, shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
                    .fillMaxWidth().fillMaxHeight(0.9F)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(0.774F).padding(134.dp, 20.dp, 40.dp, 20.dp)
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
                LazyColumn {
                    items(listaProductos) { prod ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 5.dp).fillMaxWidth().height(50.dp)
                                .background(
                                    Colores.color2, shape = RoundedCornerShape(10.dp)
                                )
                        ) {
                            if (prod.foto.ruta.startsWith("C:")) {
                                Image(
                                    bitmap = loadImageBitmap(File(prod.foto.ruta).inputStream()),
                                    contentDescription = prod.foto.descripcion,
                                    modifier = Modifier.fillMaxWidth(0.05F).fillMaxHeight(0.8F)
                                )
                            } else {
                                Image(
                                    painter = painterResource(prod.foto.ruta),
                                    contentDescription = prod.foto.descripcion,
                                    modifier = Modifier.fillMaxWidth(0.05F).fillMaxHeight(0.8F)
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(0.7F)
                            ) {
                                Text(text = prod.nombre, textAlign = TextAlign.Center, modifier = Modifier.weight(1F))
                                Text(
                                    text = prod.tipo, textAlign = TextAlign.Center, modifier = Modifier.weight(1F)
                                )
                                Text(
                                    text = prod.stock.toString(),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1F)
                                )
                                Text(
                                    text = prod.coste.toString(),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1F)
                                )
                                Text(
                                    text = prod.pvp.toString(),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1F)
                                )
                            }
                            Spacer(
                                modifier = Modifier.width(80.dp)
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(0.4F)
                            ) {
                                Boton(
                                    "M", funcion = { saveObject(prod) }, color = Color.Blue.copy(alpha = 0.9f)
                                )
                                Boton(
                                    "X", funcion = {}, color = Color.Red.copy(alpha = 0.9f)
                                )
                            }
                            Spacer(
                                modifier = Modifier.width(0.dp)
                            )
                        }
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
        if (producto != null) {
            ModifyProducto(close = changeDialog, changeProduct = changeProduct, productoPasado = producto!!)
        } else {
            AddProducto(close = changeDialog, addCard = addProduct)
        }
    }
}


@Composable
fun AddProducto(close: () -> Unit, addCard: (Producto) -> Unit) {
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
        var nombre by remember { mutableStateOf("") }
        var stock by remember { mutableStateOf("") }
        var precio1 by remember { mutableStateOf("") }
        var precio2 by remember { mutableStateOf("") }

        val tipos = remember { listOf("Refrescos", "Cocteles", "Comida") }
        var tipo by remember { mutableStateOf("Refrescos") }
        val cambiarTipo: (String) -> Unit = { tipo = it }

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
                        Surface(
                            modifier = Modifier.clip(RoundedCornerShape(10.dp))
                                .clickable(onClick = { showFilePicker = true }).fillMaxWidth().fillMaxHeight(0.5F)
                                .background(Color.White)
                        ) { }
                    }

                    ComboBox(tipo, tipos, cambiarTipo)
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
                        text = nombre,
                        cambio = { nombre = it },
                        modifier = Modifier.height(50.dp).fillMaxWidth().padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                        centrado = false,
                        placeholderText = "Nombre"
                    )
                    CustomTextField(
                        text = stock,
                        cambio = { stock = it },
                        modifier = Modifier.height(50.dp).fillMaxWidth().padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                        centrado = false,
                        placeholderText = "0u"
                    )
                    CustomTextField(
                        text = precio1,
                        cambio = { if (it.isEmpty() || it.matches(pattern) && it.length <= 6) precio1 = it },
                        modifier = Modifier.height(50.dp).fillMaxWidth().padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                        centrado = false
                    )
                    CustomTextField(
                        text = precio2,
                        cambio = { if (it.isEmpty() || it.matches(pattern) && it.length <= 6) precio2 = it },
                        modifier = Modifier.height(50.dp).fillMaxWidth().padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                        centrado = false
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().fillMaxHeight()
            ) {
                Boton("Cancelar", funcion = close)
                Boton("Añadir", funcion = {
                    if (!(nombre.isEmpty() || precio1.isEmpty() || precio2.isEmpty() || stock.isEmpty() || filePath.isEmpty())) {
                        addCard(
                            Producto(
                                nombre,
                                precio1.toDouble(),
                                precio2.toDouble(),
                                stock.toInt(),
                                Photo(filePath, "imagen producto"),
                                tipo
                            )
                        )
                    }
                })
            }
        }
    }

    val fileType = listOf("jpg", "png")
    FilePicker(show = showFilePicker, fileExtensions = fileType) { platformFile ->
        showFilePicker = false
//        val nomTemp = platformFile?.path?.split("\\")?.last()
//        println(nomTemp)
//        File(platformFile?.path ?: "").copyTo(File("C:/Images/$nomTemp"), true)
//        nombreF = "prodImgs/$nomTemp"
        filePath = platformFile?.path ?: ""
    }
}

@Composable
fun ModifyProducto(close: () -> Unit, changeProduct: (Producto) -> Unit, productoPasado: Producto) {
    var filePath by remember { mutableStateOf(productoPasado.foto.ruta) }
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
        var nombre by remember { mutableStateOf(productoPasado.nombre) }
        var stock by remember { mutableStateOf(productoPasado.stock.toString()) }
        var precio1 by remember { mutableStateOf(productoPasado.coste.toString()) }
        var precio2 by remember { mutableStateOf(productoPasado.pvp.toString()) }
        var tipo by remember { mutableStateOf(productoPasado.tipo) }

        val tipos = remember { listOf("Refrescos", "Cocteles", "Comida") }
        val cambiarTipo: (String) -> Unit = { tipo = it }

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
                            painter = painterResource(productoPasado.foto.ruta),
                            contentDescription = "Logo producto",
                            modifier = Modifier.clip(RoundedCornerShape(10.dp))
                                .clickable(onClick = { showFilePicker = true }).fillMaxWidth().fillMaxHeight(0.5F)
                                .background(Color.White)
                        )
                    }

                    ComboBox(tipo, tipos, cambiarTipo)
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
                        text = nombre,
                        cambio = { nombre = it },
                        modifier = Modifier.height(50.dp).fillMaxWidth().padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                        centrado = false,
                        placeholderText = "Nombre"
                    )
                    CustomTextField(
                        text = stock,
                        cambio = { stock = it },
                        modifier = Modifier.height(50.dp).fillMaxWidth().padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                        centrado = false,
                        placeholderText = "0u"
                    )
                    CustomTextField(
                        text = precio1,
                        cambio = { if (it.isEmpty() || it.matches(pattern) && it.length <= 6) precio1 = it },
                        modifier = Modifier.height(50.dp).fillMaxWidth().padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                        centrado = false
                    )
                    CustomTextField(
                        text = precio2,
                        cambio = { if (it.isEmpty() || it.matches(pattern) && it.length <= 6) precio2 = it },
                        modifier = Modifier.height(50.dp).fillMaxWidth().padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                        centrado = false
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().fillMaxHeight()
            ) {
                Boton("Cancelar", funcion = close)
                Boton("Añadir", funcion = {
                    if (!(nombre.isEmpty() || precio1.isEmpty() || precio2.isEmpty() || stock.isEmpty() || filePath.isEmpty())) {
                        changeProduct(
                            Producto(
                                nombre,
                                precio1.toDouble(),
                                precio2.toDouble(),
                                stock.toInt(),
                                Photo(filePath, "imagen producto"),
                                tipo
                            )
                        )
                    }
                })
            }
        }
    }

    val fileType = listOf("jpg", "png")
    FilePicker(show = showFilePicker, fileExtensions = fileType) { platformFile ->
        showFilePicker = false
//        val nomTemp = platformFile?.path?.split("\\")?.last()
//        println(nomTemp)
//        File(platformFile?.path ?: "").copyTo(File("C:/Images/$nomTemp"), true)
//        nombreF = "prodImgs/$nomTemp"
        filePath = platformFile?.path ?: ""
    }
}
