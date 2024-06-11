class Producto {
    var name: String
    var price: Double
    var photo: Photo
    var tipo: String = "no hay"

    constructor(name: String, price: Double, photo: Photo) {
        this.name = name
        this.price = price
        this.photo = photo
    }

    constructor(name: String, price: Double, photo: Photo, tipo:String) {
        this.name = name
        this.price = price
        this.photo = photo
        this.tipo = tipo
    }
}

class Photo (var ruta: String,
             var descripcion: String){
}

class Pedido (var producto: Producto,
             var cantidad: Int){

    constructor(pedido: Pedido) : this(pedido.producto, pedido.cantidad){}
}
