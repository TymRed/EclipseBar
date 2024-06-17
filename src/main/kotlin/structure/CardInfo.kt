package structure

import java.time.LocalDate
import java.time.LocalTime

data class Producto (
    var name: String,
    var price: Double,
    var photo: Photo,
    var tipo: String? = null
)


data class Photo (var ruta: String,
             var descripcion: String){
}

data class ProdInPed (var producto: Producto,
                      var cantidad: Int){

//    constructor(pedido: ProdInPed) : this(pedido.producto, pedido.cantidad){}
}

data class Pedido(
    val numero: Int,
    val fecha: LocalDate,
    val hora: LocalTime,
    val importe: Double,
    val camarero: String
)

data class Filter(
    val numPedidos: IntRange = (0..1000),
    val fechas: ClosedRange<LocalDate> = LocalDate.of(2022,8,10)..LocalDate.now(),
    val camarero: String = "Todos",
    val importeRange: ClosedFloatingPointRange<Float> = (0.0f..1000.00f)
)