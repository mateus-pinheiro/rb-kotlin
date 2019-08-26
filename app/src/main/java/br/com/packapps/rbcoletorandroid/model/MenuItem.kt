package br.com.packapps.rbcoletorandroid.model

import br.com.packapps.rbcoletorandroid.R

class MenuItem(var title: String, var color: Int)

object SupplierMenu {

    val entryItem = MenuItem(
        "Entrada", R.color.colorMenuEntrada
    )

    val checkoutItem = MenuItem(
        "Saída", R.color.colorReason2
    )

    val aggregationItem = MenuItem(
        "Agregação", R.color.colorMenuSaida
    )

    val finalizeItem = MenuItem(
        "Finalização", R.color.colorReason1
    )


    val items = listOf<MenuItem>(
        MenuItem("Entrada", R.color.colorMenuEntrada),
        MenuItem("Saída", R.color.colorReason2),
        MenuItem("Finalização", R.color.colorReason1),
        MenuItem("Agregação", R.color.colorMenuSaida)
    )

}