package structure

var lang = "ru"

val langPack = mapOf(
    "es" to mapOf(
        "Charge" to "Cobrar",
        "Erase" to "Borrar",
        "No products" to "No hay productos",
        "Enter the amount" to "Introduce el importe",
        "Not enough money" to "Dinero insuficiente",
        "Change" to "Cambio",
        "unit" to "unidad",
        "All" to "Todos",
        "Sodas" to "Refrescos",
        "Cocktails" to "Cócteles",
        "Food" to "Comida",
        "Not in stock" to "No hay en stock",
        "Total" to "Total",
        "POS App" to "Aplicacion TPV",
        "Name" to "Nombre",
        "Password" to "Contraseña",
        "Incorrect user or password" to "Usuario o contraseña incorrectos",
        "Software developed by Tymur Kulivar and Javier Redondo" to "Sofware desarrollado por Tymur Kulivar y Javier Redondo",
        "History" to "Historial",
        "Stock" to "Stock",
        "Main menu" to "Menú principal",
        "Close" to "Cerrar",
        "Number" to "Número",
        "Date" to "Fecha",
        "Time" to "Hora",
        "Waiter" to "Camarero",
        "Filters" to "Filtros",
        "Order number" to "Número pedido",
        "Min" to "Min",
        "Max" to "Max",
        "Amount" to "Importe",
        "Apply" to "Aplicar",
        "Reset" to "Resetear",
        "Start date" to "Fecha inicio",
        "End date" to "Fecha fin",
        "Login" to "Iniciar sesión",
        "Add product" to "Añadir producto",
        "RRP" to "PVP",
        "Cancel" to "Cancelar",
        "Save" to "Guardar",
        "Confirm" to "Confirmar",
        "Category" to "Categoría",
        "Price" to "Precio",
        "Search" to "Buscar",
        "Are you sure?" to "¿Estás seguro?",
    )
)

fun getString(key: String): String {
    return langPack[lang]?.get(key) ?: key
}