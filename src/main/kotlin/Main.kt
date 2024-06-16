import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

@Composable
fun Aplicacion() {
    var wind by remember { mutableStateOf(1) }
    val windChange: () -> Unit = {
        wind = if (wind == 1) 2
        else 1
    }

    when (wind) {
        1 -> LogIn(windChange)
        2 -> App(windChange)
    }
}

@Composable
fun LogIn(windChange: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    val comprobarNombre = comprobarNombre(nombre)
    val comprobarCont = comprobarContrasena(contrasena)

    Surface(color = Colores.color1) {
        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxHeight().fillMaxWidth(0.5F),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Spacer(modifier = Modifier.height(100.dp))

                    Image(
                        painter = painterResource("Vector.svg"),
                        contentDescription = "Imagen de la aplicacion",
                        modifier = Modifier.fillMaxWidth(0.5F)
                    )
                    Text("Aplicacion TPV")


                    MyTextField(nombre, "Nombre", comprobarNombre) { nombre = it }
                    MyTextField(contrasena, "Contraseña", comprobarCont) { contrasena = it }


                    Button(
                        onClick = windChange,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Colores.color4),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.shadow(elevation = 10.dp, shape = RoundedCornerShape(8.dp)).height(50.dp)
                    ) {
                        Text("iniciar sesión".uppercase(), color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(100.dp))

                }
                Image(
                    painter = painterResource("Planeta 1.png"),
                    contentDescription = "Imagen de la aplicacion",
                    modifier = Modifier.fillMaxHeight(0.75F)
                )
            }
            Row(
                modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth().offset (y = (-40).dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                Text("Sofware desarrollado por Tymur Kulivar y Javier Redondo", color = Colores.color6)
                Spacer(modifier = Modifier.fillMaxWidth(0.9F))
                Text("Ver. 1.0", color = Colores.color6)
            }
        }
    }

}

@Composable
fun App(windChange: () -> Unit) {
    var active by remember { mutableStateOf(1) }

    val activeChange: (Int) -> Unit = { index ->
        active = index
    }

    Column {
        MenuBar(active, activeChange, modifier = Modifier.background(Colores.color5), windChange)
        when (active) {
            1 -> PanelPrincipal()
            2 -> Stock()
            3 -> Historial()
            /*            3 -> Prueb3()
                        4 -> Prueb4()*/
        }
    }
}

@Composable
fun MenuBar(
    active: Int,
    activeChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    windChange: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().fillMaxHeight(0.1F)
    ) {
        MenuBarText("Panel Principal", active == 1) { activeChange(1) }
        MenuBarText("Stock", active == 2) { activeChange(2) }
        MenuBarText("Historial", active == 3) { activeChange(3) }
        MenuBarText("Estadísticas", active == 4) { activeChange(4) }
        Image(
            painterResource("Logo.svg"), "Logo atrás",
            modifier = Modifier.clip(CircleShape).clickable(onClick = windChange)
                .size(40.dp)
        )
    }
}

@Composable
fun MenuBarText(text: String, active: Boolean, prueba: () -> Unit) {
    Text(
        text,
        color = if (active) Colores.color3 else Colores.color1,
        modifier = Modifier.clickable(onClick = prueba).padding(10.dp)
    )
}


@Composable
fun MyTextField(nombre: String, placeholder: String, verif: Boolean, change: (String) -> Unit) {
    val fontSize = if (placeholder == "Contraseña") 20.sp else 16.sp
    TextField(
        value = nombre,
        textStyle = TextStyle(color = Colores.color3, fontSize = fontSize),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White, focusedIndicatorColor = Colores.color3, cursorColor = Colores.color3
        ),
        onValueChange = change,
        shape = RoundedCornerShape(20.dp, 20.dp),
        placeholder = { Text(placeholder, color = Colores.color2) },
        isError = !verif,
        singleLine = true,
        visualTransformation = if (placeholder == "Contraseña") PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(0.4F))
}

fun comprobarNombre(nombreUsuario: String): Boolean {
    val longitudAdecuada = nombreUsuario.length in 4..16

    if (nombreUsuario.isEmpty() || !longitudAdecuada) {
        return false
    }

    val may1 = nombreUsuario[0] in 'A'..'Z'
    val adecuado = nombreUsuario.substring(1).matches("[a-z]*".toRegex())

    return may1 && adecuado
}

fun comprobarContrasena(contrasena: String): Boolean {
    val longitudAdecuada = contrasena.length in 4..16

    if (contrasena.isEmpty() || !longitudAdecuada) {
        return false
    }

    val hayMin = contrasena.matches(".*[a-z].*".toRegex())
    val hayMay = contrasena.matches(".*[A-Z].*".toRegex())
    val hayNum = contrasena.matches(".*[1-9].*".toRegex())

    return hayMin && hayMay && hayNum
}

fun main() = application {

    val state = rememberWindowState(placement = WindowPlacement.Maximized)

    Window(
        onCloseRequest = ::exitApplication,
        state = state,
        title = "Eclipse",
        icon = painterResource("Logo.svg"),


    ) {
        Aplicacion()
    }
}