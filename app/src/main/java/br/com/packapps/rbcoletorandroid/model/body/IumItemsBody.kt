package br.com.packapps.rbcoletorandroid.model.body

import br.com.packapps.rbcoletorandroid.core.SingletonApp

class IumItemsBody(var query: String = "") {

    object SupplierIum {
        val queryValue = IumItemsBody(
            "{\n" +
                    "  itemsByEventId(eventId: \"${SingletonApp.getInstance().paramBody}\") {\n" +
                    "    id\n" +
                    "    aggrId\n" +
                    "    presentation\n" +
                    "    identifier\n" +
                    "    batchObject {\n" +
                    "       batch\n" +
                    "    }\n" +
                    "  }\n" +
                    "}"
        )
    }

}
