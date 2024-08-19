package gui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import db.User
import structure.Colores
import structure.DbSetup
import structure.getString
import structure.userQueries

@Composable
fun Application() {
    var wind by remember { mutableStateOf(1) }
    val windChange: (Int) -> Unit = { index ->
        wind = index
    }
    var admin by remember { mutableStateOf(false) }
    var username: String by remember { mutableStateOf("") }
    val logInto: (User) -> Unit = {
        username = it.username
        admin = it.username == "Toño"
    }
    when (wind) {
        1 -> LogIn(logInto) { windChange(2) }
        2 -> App(username, admin) { windChange(1) }
    }
}

@Composable
fun LogIn(logInto: (User) -> Unit, windChange: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var errorText by remember { mutableStateOf("") }

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
                        contentDescription = "Logo",
                        modifier = Modifier.fillMaxWidth(0.5F)
                    )
                    Text(getString("POS App"))


                    MyTextField(name, getString("Name")) { name = it }
                    MyTextField(password, getString("Password")) { password = it }


                    Button(
                        onClick = {
                            val usr = userQueries.findByUsername(name).executeAsOneOrNull()
                            if (usr?.password == password) {
                                logInto(usr)
                                windChange()
                            } else {
                                errorText = getString("Incorrect username or password")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Colores.color4),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.shadow(elevation = 10.dp, shape = RoundedCornerShape(8.dp)).height(50.dp)
                    ) {
                        Text(getString("Login").uppercase(), color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                }
                Image(
                    painter = painterResource("Planeta 1.png"),
                    contentDescription = "Planet img",
                    modifier = Modifier.fillMaxHeight(0.75F)
                )
            }
            Row(
                modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth().offset(y = (-40).dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(getString("Software developed by Tymur Kulivar and Javier Redondo"), color = Colores.color6)
                Spacer(modifier = Modifier.fillMaxWidth(0.9F))
                Text("Ver. 1.4", color = Colores.color6)
            }
        }
    }
    if (errorText != "") {
        TextDialog(errorText) { errorText = "" }
    }
}

@Composable
fun App(
    username: String,
    admin: Boolean,
    windChange: () -> Unit
) {
    var active by remember { mutableStateOf(1) }
    val changeActive: (Int) -> Unit = { index ->
        active = index
    }

    Column {
        MenuBar(
            admin,
            active,
            changeActive,
            windChange,
            modifier = Modifier.background(Colores.color5),
        )
        when (active) {
            1 -> Menu(username)
            2 -> Stock()
            3 -> Historial()
        }
    }
}

@Composable
fun MenuBar(
    admin: Boolean,
    active: Int,
    activeChange: (Int) -> Unit,
    windChange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().fillMaxHeight(0.1F)
    ) {
        MenuBarText(getString("Main menu"), active == 1) { activeChange(1) }
        MenuBarText(getString("Stock"), active == 2, usable = admin) { activeChange(2) }
        MenuBarText(getString("History"), active == 3, usable = admin) { activeChange(3) }
        Image(
            painterResource("Logo.svg"),
            "Return to login",
            modifier = Modifier.clip(CircleShape).clickable(onClick = windChange).size(40.dp)
        )
    }
}

@Composable
fun MenuBarText(
    text: String,
    active: Boolean,
    usable: Boolean = true,
    changePanel: () -> Unit
) {
    val color = if (!usable) Color.Gray
    else if (active) Colores.color3
    else Colores.color1

    Text(
        text,
        color = color,
        fontSize = 16.sp,
        modifier = Modifier.clickable(enabled = usable, onClick = changePanel).padding(13.dp)
    )
}


@Composable
fun MyTextField(
    name: String,
    placeholder: String,
    change: (String) -> Unit
) {
    val fontSize = if (placeholder == getString("Password")) 20.sp else 16.sp
    TextField(
        value = name,
        textStyle = TextStyle(color = Colores.color3, fontSize = fontSize),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White, focusedIndicatorColor = Colores.color2, cursorColor = Colores.color3
        ),
        onValueChange = change,
        shape = RoundedCornerShape(20.dp, 20.dp),
        placeholder = { Text(placeholder, color = Colores.color2) },
        singleLine = true,
        visualTransformation = if (placeholder == getString("Password")) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(0.4F)
    )
}

@Composable
fun Boton(
    text: String,
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
        Text(text)
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
    val source = remember { MutableInteractionSource() }
    val isFocused = source.collectIsFocusedAsState()
    val borderWidth = if (isFocused.value) 2.dp else 0.dp
    val borderColor = if (isFocused.value) Colores.color3 else Color.Transparent
    BasicTextField(
        value = text,
        onValueChange = valueChange,
        singleLine = true,
        cursorBrush = SolidColor(Colores.color3),
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colors.onSurface, fontSize = fontSize, textAlign = align
        ),
        interactionSource = source,
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth(0.15F)
                    .border(borderWidth, borderColor, RoundedCornerShape(10.dp))
                    .padding(horizontal = 5.dp)
            ) {
                if (leadingIcon != null) leadingIcon()
                Box {
                    if (text.isEmpty()) {
                        Text(
                            placeholderText,
                            style = LocalTextStyle.current.copy(
                                color = Colores.color2,
                                fontSize = fontSize,
                                textAlign = align
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ComboBox(
    text: String,
    list: List<String>,
    change: (String) -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = false, onExpandedChange = {}) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(10.dp))
                .padding(start = 5.dp)
        ) {
            Text(getString(text))
            IconButton(onClick = { visible = true }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Open list")
            }
        }
        ExposedDropdownMenu(
            expanded = visible,
            onDismissRequest = { visible = false },
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            for (item in list) {
                DropdownMenuItem(onClick = { visible = false; change(item) }) {
                    Text(getString(item))
                }
                Divider()
            }
        }
    }
}

@Composable
fun TextDialog(
    text: String,
    eraseText: () -> Unit
) {
    val dialogWidth = (text.length * 13 + 40).dp
    Dialog(
        onDismissRequest = eraseText
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .width(dialogWidth)
                .height(150.dp)
                .background(Colores.color1, RoundedCornerShape(16.dp))
                .background(Color.Red.copy(alpha = 0.1f))
                .clickable(onClick = eraseText).padding(horizontal = 30.dp)
        ) {
            Icon(Icons.Filled.Close, contentDescription = getString("Close"), tint = Color.Red)
            Text(text, color = Color.Red, fontSize = 20.sp)
        }
    }
}


fun main() = application {
    val state = rememberWindowState(placement = WindowPlacement.Maximized)

    Window(
        onCloseRequest = ::exitApplication,
        state = state,
        title = "Eclipse",
        icon = painterResource("Logo.svg"),
    ) {
        Application()
    }
    DbSetup().setUp()
}
