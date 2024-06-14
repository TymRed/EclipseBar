import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

@Composable
fun DialogDatePicker(gestionCalendar: () -> Unit, cambiarFecha: (String) -> Unit) {
    Dialog(onDismissRequest = gestionCalendar) {
        Column(modifier = Modifier.width(IntrinsicSize.Min).background(Color.White)) {
            var selected = DatePicker()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                TextButton(
                    onClick = gestionCalendar
                ) {
                    Text("Cerrar")
                }
                TextButton(
                    onClick = {
                        if (selected != null) {
                            cambiarFecha(selected)
                        }
                    },
                ) {
                    Text("Confirmar")
                }
            }

        }
    }
}


val loc = Locale("es", "Es")
val weeks = arrayOf("L", "M", "M", "J", "V", "S", "D")
val cellSize = 35.dp

@Composable
@Preview
fun DatePicker(): String? {
    var showingMonth by remember { mutableStateOf(Calendar.getInstance()) }
    var selected by remember { mutableStateOf(Calendar.getInstance().apply { time = showingMonth.time }) }
    var list by remember { mutableStateOf(getDatesList(showingMonth)) }

    Column(
        modifier = Modifier.background(Colores.color1).width(IntrinsicSize.Min)
    ) {

        Box(modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 12.dp)) {
            Icon(
                Icons.Outlined.ArrowDropDown,
                "",
                tint = Color.Black,
                modifier = Modifier.size((16 * 3).dp).align(Alignment.CenterStart).clickable {
                    showingMonth = Calendar.getInstance()
                        .apply { timeInMillis = showingMonth.timeInMillis;add(Calendar.MONTH, -1) }
                    list = getDatesList(showingMonth)
                }.padding(16.dp)
            )
            Text(
                SimpleDateFormat("MMM yyyy", loc).format(showingMonth.time),
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
            Icon(
                Icons.Outlined.ArrowDropDown,
                "",
                tint = Color.Black,
                modifier = Modifier.size((16 * 3).dp).align(Alignment.CenterEnd).clickable {
                    showingMonth = Calendar.getInstance()
                        .apply { timeInMillis = showingMonth.timeInMillis;add(Calendar.MONTH, +1) }
                    list = getDatesList(showingMonth)
                }.padding(16.dp)
            )

        }


        Row(modifier = Modifier.padding(start = 15.dp, end = 10.dp)) {
            weeks.forEach {
                Text(
                    it,
                    color = Color.Black.copy(alpha = 0.5f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(
                        cellSize
                    )
                )
            }
        }


        list.chunked(7).forEach {
            Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                it.forEach {
                    Box(contentAlignment = Alignment.Center,
                        modifier = Modifier.size(cellSize).clickable(enabled = it?.second == true) {
                            if (isSameMonth(it?.first, showingMonth)) {
                                selected = Calendar.getInstance().apply { timeInMillis = it!!.first }
                            }
                        }.drawBehind {
                            drawCircle(
                                color = if (it?.second == true && isSelected(
                                        it.first, selected, showingMonth
                                    )
                                ) Color.Blue else Color.Transparent, radius = cellSize.toPx() / 2
                            )
                        }) {
                        Text(
                            toDate(it?.first),
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = if (it?.second == true) {
                                if (isSelected(it.first, selected, showingMonth)) Color.White
                                else Color.Black

                            } else if (it?.second == false) Color.Transparent else Color.Red,
                            modifier = Modifier.offset(y = (-1).dp, x = (-1).dp)
                        )
                    }
                }
            }
        }
    }
    return SimpleDateFormat("EEE, d MMM", loc).format(selected.time)
}

private fun getDatesList(calIncoming: Calendar): ArrayList<Pair<Long, Boolean>?> {

    val cal = Calendar.getInstance().apply { timeInMillis = calIncoming.timeInMillis }
    val list = arrayListOf<Pair<Long, Boolean>?>()
    cal.set(Calendar.DATE, 1)
    val currentMont = cal.get(Calendar.MONTH)
    while (cal.get(Calendar.MONTH) == currentMont) {
        list.add(cal.timeInMillis to true)
        cal.add(Calendar.DATE, 1)
    }
    while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
        list.add(cal.timeInMillis to false)
        cal.add(Calendar.DATE, 1)
    }
    cal.set(Calendar.DATE, 1)
    cal.add(Calendar.MONTH, -1)
    cal.add(Calendar.DATE, -1)
    while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
        list.add(0, cal.timeInMillis to false)
        cal.add(Calendar.DATE, -1)
    }

    if (list.size < 7 * 6) {
        while (list.size < (7 * 6)) {
            list.add(null)
        }
    }

    return list
}


private fun isSelected(first: Long, selected: Calendar, showing: Calendar): Boolean {
    return (selected.get(Calendar.YEAR) == showing.get(Calendar.YEAR) && selected.get(Calendar.MONTH) == showing.get(
        Calendar.MONTH
    ) && selected.get(Calendar.DATE) == Calendar.getInstance().apply {
        timeInMillis = first
    }.get(Calendar.DATE))

}

private fun isSameMonth(first: Long?, showing: Calendar): Boolean {
    return if (first == null) false
    else {
        val cal = Calendar.getInstance().apply {
            timeInMillis = first
        }
        cal.get(Calendar.MONTH) == showing.get(Calendar.MONTH) && cal.get(Calendar.YEAR) == showing.get(Calendar.YEAR)
    }
}

private fun toDate(first: Long?): String {
    if (first == null) return ""
    return SimpleDateFormat("d", loc).format(Date(first))
}