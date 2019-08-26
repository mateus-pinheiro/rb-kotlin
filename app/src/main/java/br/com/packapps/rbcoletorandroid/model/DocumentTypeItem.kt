package br.com.packapps.rbcoletorandroid.model

object DocumentTypeItem {

    enum class Items(var prefix: String, var description: String){
        NOTA_FISCAL("NF", "Nota Fiscal"),
        ORDER_COMPRA("PO", "Ordem De Compra"),
        COMERCIALIZACAO("PO", "Comercializacao"),
        BOL("BOL", "Bill Of Landing"),
        INV("INV", "Invoice"),
        RMA("RMA", "Return Merchandise Authorization"),
        PDG("PDG", "Pedigree"),
        DAV("DAV", "Dispatch Advice"),
        RAV("RAV", "Receiving Advice")
    }

}
