package gui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import structure.Colores

@Composable
fun Stock() {
    var isOpen by remember { mutableStateOf(false) }
    val changeDialog = { isOpen = !isOpen }

    Surface(color = Colores.color1) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
                    .fillMaxWidth()
                    .fillMaxHeight(0.9F)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(text = "Nombre", color = Colores.color4, fontSize = 17.sp)
                    Text(text = "Categoría", color = Colores.color4, fontSize = 17.sp)
                    Text(text = "Stock", color = Colores.color4, fontSize = 17.sp)
                    Text(text = "Precio", color = Colores.color4, fontSize = 17.sp)
                    Spacer(
                        modifier = Modifier.fillMaxWidth(0.2F)
                    )
                }
                LazyColumn {
                    items(100) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(10.dp, 5.dp, 10.dp, 5.dp)
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Colores.color2, shape = RoundedCornerShape(10.dp))
                        ) {
                            Image(
                                painter = painterResource("Fanta.png"),
                                contentDescription = "Logo producto",
                                modifier = Modifier
                                    .fillMaxWidth(0.05F)
                                    .fillMaxHeight(0.8F)
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth(0.4F)
                            ) {
                                Text(text = "Fanta")
                                Text(text = "Refrescos")
                                Text(text = "2u")
                                Text(text = "2.20€")
                            }
                            Spacer(
                                modifier = Modifier.width(200.dp)
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth(0.25F)
                            ) {
                                Button(onClick = {}) {}
                                Button(onClick = {}) {}
                            }
                            Spacer(
                                modifier = Modifier.width(50.dp)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Text("Añadir producto", color = Colores.color1)
            }
        }
    }
    if (isOpen) {
        AddProducto(close = changeDialog)
    }
}


@Composable
fun AddProducto(close: () -> Unit) {
    Dialog(onDismissRequest = close) {
        Column(
            modifier = Modifier
                .width(550.dp)
                .height(300.dp)
                .background(Color.White)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8F)
                    .background(Colores.color1),
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth(0.3F)
                        .fillMaxHeight()
                ) {
                    Image(
                        painter = painterResource("Fanta.png"),
                        contentDescription = "Logo producto",
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5F)
                            .background(Color.White)
                    )
                    EligCamarero()
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 40.dp)
                ) {
                    Text("Nombre")
                    Text("Stock")
                    Text("Precio")
                }
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.7F)
                ) {
                    CustomTextField(
                        "", {},
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                    )
                    CustomTextField(
                        "", {},
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                    )
                    CustomTextField(
                        "", {},
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .padding(end = 10.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Boton("Cancelar", funcionLista = close)
                Boton("Añadir", funcionLista = close)
            }
        }
    }
}