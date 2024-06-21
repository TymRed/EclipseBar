package structure

import androidx.compose.runtime.mutableStateListOf
import java.time.LocalDate
import java.time.LocalTime

data class Producto(
    var nombre: String,
    var coste: Double,
    var pvp: Double,
    var stock: Int,
    var foto: Photo,
    var tipo: String
)

data class Photo(
    var ruta: String,
    var descripcion: String
)

val listaProductos: MutableList<Producto> = mutableStateListOf(
    Producto("Coca-Cola", 1.5, 2.2, 5, Photo("prodImgs/Fanta.png", "sfsdf"), "Refrescos"),
    Producto("Fanta", 2.5, 3.0, 1, Photo("prodImgs/Fanta.png", "sfsdf"), "Cocteles"),
    Producto("Aquarius", 1.9, 2.5, 3, Photo("prodImgs/Fanta.png", "sfsdf"), "Comida"),
    Producto("Cerveza", 2.2, 2.5, 4, Photo("prodImgs/Fanta.png", "sfsdf"), "Refrescos"),
    Producto("Vino", 3.5, 4.0, 6, Photo("prodImgs/Fanta.png", "sfsdf"), "Refrescos"),
    Producto("Café", 3.1, 3.2, 7, Photo("prodImgs/Fanta.png", "sfsdf"), "Cocteles"),
    Producto("Té", 2.0, 3.2, 8, Photo("prodImgs/Fanta.png", "sfsdf"), "Cocteles"),
    Producto("Zumo", 1.3, 3.2, 9,  Photo("prodImgs/Fanta.png", "sfsdf"), "Comida"),
    Producto("Leche", 1.7, 3.2, 1, Photo("prodImgs/Fanta.png", "sfsdf"), "Comida"),
    Producto("Agua", 2.4, 3.2, 3, Photo("prodImgs/Fanta.png", "sfsdf"),"Comida"),
    Producto("Sprite", 1.1, 3.2, 4, Photo("prodImgs/Fanta.png", "sfsdf"),"Comida"),
    Producto("7up", 2.1, 3.2, 1, Photo("prodImgs/Fanta.png", "sfsdf"),"Comida"),
    Producto("Pepsi", 2.7, 3.2, 1, Photo("prodImgs/Fanta.png", "sfsdf"),"Comida"),
)

data class ProdInPed(
    var producto: Producto,
    var cantidad: Int
)

data class Pedido(
    val numero: Int,
    val fecha: LocalDate,
    val hora: LocalTime,
    val importe: Double,
    val camarero: String
)

data class Filter(
    val numPedidos: IntRange = (0..1000),
    val fechas: ClosedRange<LocalDate> = LocalDate.of(2022, 8, 10)..LocalDate.now(),
    val camarero: String = "Todos",
    val importeRange: ClosedFloatingPointRange<Float> = (0.0f..1000.00f)
)