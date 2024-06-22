package datePicker

import gui.Boton
import structure.Colores
import androidx.compose.foundation.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
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
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

@Composable
fun DialogDatePicker(
    changeCalendarState: () -> Unit,
    changeDate1: (String?) -> Unit,
    changeDate2: (LocalDate) -> Unit
) {
    Dialog(onDismissRequest = changeCalendarState) {
        Column(modifier = Modifier.width(IntrinsicSize.Min).background(Color.White)) {
            val pair = DatePicker()
            val fechaSelected = pair.first
            val selected = pair.second
            Divider(thickness = 2.dp)
            Row(
                modifier = Modifier.fillMaxWidth().background(Colores.color1),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Boton(
                    "Cerrar",
                    modifier = Modifier.width(120.dp),
                    function = changeCalendarState)
                Spacer(modifier = Modifier.width(10.dp))
                Boton("Confirmar", modifier = Modifier.width(120.dp),
                    function = {
                        changeDate1(fechaSelected)

                        val year = selected[Calendar.YEAR]
                        val month = if (selected.get(Calendar.MONTH) + 1 < 10) {
                            "0" + (selected[Calendar.MONTH] + 1)
                        } else selected[Calendar.MONTH] + 1
                        val date = if (selected[Calendar.DATE] < 10) {
                            "0" + selected.get(Calendar.DATE)
                        } else selected.get(Calendar.DATE)

                        changeDate2(LocalDate.parse("$year-$month-$date"))
                    }
                )
            }

        }
    }
}


val loc: Locale = Locale.Builder().setRegion("ES").setLanguage("es").build()
val weeks = arrayOf("L", "M", "M", "J", "V", "S", "D")
val cellSize = 45.dp

@Composable
fun DatePicker(): Pair<String, Calendar> {
    var showingMonth by remember { mutableStateOf(Calendar.getInstance()) }
    var selected by remember { mutableStateOf(Calendar.getInstance().apply { time = showingMonth.time }) }
    var list by remember { mutableStateOf(getDatesList(showingMonth)) }

    Column(
        modifier = Modifier.background(Colores.color1).width(IntrinsicSize.Min)
    ) {

        Box(modifier = Modifier.background(Colores.color5).fillMaxWidth().padding(start = 16.dp, end = 16.dp)) {
            Icon(
                Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                "Mes anterior",
                tint = Colores.color1,
                modifier = Modifier.size((16 * 4).dp).align(Alignment.CenterStart).clickable {
                    showingMonth = Calendar.getInstance()
                        .apply { timeInMillis = showingMonth.timeInMillis;add(Calendar.MONTH, -1) }
                    list = getDatesList(showingMonth)
                }.padding(16.dp)
            )
            Text(
                SimpleDateFormat("MMM yyyy", loc).format(showingMonth.time),
                color = Colores.color1,
                modifier = Modifier.align(Alignment.Center)
            )
            Icon(
                Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                "Siguiente mes",
                tint = Colores.color1,
                modifier = Modifier.size((16 * 4).dp).align(Alignment.CenterEnd).clickable {
                    showingMonth = Calendar.getInstance()
                        .apply { timeInMillis = showingMonth.timeInMillis;add(Calendar.MONTH, +1) }
                    list = getDatesList(showingMonth)
                }.padding(16.dp)
            )

        }


        Row(modifier = Modifier.padding(start = 15.dp, end = 10.dp, top = 12.dp)) {
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
                                ) Colores.color3 else Color.Transparent, radius = cellSize.toPx() / 2
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
    return Pair(SimpleDateFormat("EEE, d MMM (YYYY)", loc).format(selected.time), selected)
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