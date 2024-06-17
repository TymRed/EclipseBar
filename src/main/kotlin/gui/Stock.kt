package gui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import structure.Colores

@Composable
fun Stock() {

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
                onClick = {},
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

}