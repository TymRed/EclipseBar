package gui

import androidx.compose.runtime.Composable


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import graphs.pie.PieChart
import graphs.pie.PieChartData
import graphs.pie.render.SimpleSliceDrawer
import graphs.simpleChartAnimation
import structure.Colores
import kotlin.random.Random

@Composable
fun Estadisticas() {


    Surface(color = Colores.color1) {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(20.dp)
        ) {
            PieChartView()
        }
    }

}

@Composable
fun PieChartView() {
    PieChart(
        pieChartData = PieChartData(
            slices = listOf(
                PieChartData.Slice(
                    randomLength(), randomColor()
                ), PieChartData.Slice(randomLength(), randomColor()), PieChartData.Slice(randomLength(), randomColor())
            )
        ),
        // Optional properties.
        modifier = Modifier.fillMaxSize(), animation = simpleChartAnimation(), sliceDrawer = SimpleSliceDrawer()
    )
}

private fun randomLength(): Float = Random.Default.nextInt(25, 125).toFloat()
private fun randomColor(): Color {
    val idx = Random.Default.nextInt(colors.size)
    return colors[idx]
}

private var colors = mutableListOf(
    Color(0XFFF44336),
    Color(0XFFE91E63),
    Color(0XFF9C27B0),
    Color(0XFF673AB7),
    Color(0XFF3F51B5),
    Color(0XFF03A9F4),
    Color(0XFF009688),
    Color(0XFFCDDC39),
    Color(0XFFFFC107),
    Color(0XFFFF5722),
    Color(0XFF795548),
    Color(0XFF9E9E9E),
    Color(0XFF607D8B)
)