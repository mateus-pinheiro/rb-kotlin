package br.com.packapps.rbcoletorandroid.model.body

import br.com.packapps.rbcoletorandroid.core.SingletonApp

class AggregationItemsBody(var query: String) {

    object SupplierAggregation {

        val queryValue = AggregationItemsBody(
            "{itemsByParent(parentId: \"${SingletonApp.getInstance().paramBody}\"){ id aggrId company { id name }}}"
        )
    }

}