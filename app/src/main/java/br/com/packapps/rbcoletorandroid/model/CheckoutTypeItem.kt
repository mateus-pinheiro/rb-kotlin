package br.com.packapps.rbcoletorandroid.model

object CheckoutTypeItem {

    enum class Items(val code : Int, val description : String){
        TRANSFERENCIA(4002, "Transferência"),
        DOACAO(4003, "Doação"),
        COMERCIALIZACAO(4001, "Comercialização"),
        DEVOLUCAO(4004, "Devolução"),
        RECOLHIMENTO(4005, "Recolhimento"),
        AMOSTRA_GRATIS(4006, "Amostra Grátis"),
        AVARIA(4007, "Avaria"),
        EXPIRADO(4008, "Expirado")

    }

    enum class ItemsIndustry(val code : Int, val description : String){
        TRANSFERENCIA(4002, "Transferência"),
        DOACAO(4003, "Doação"),
        COMERCIALIZACAO(4001, "Comercialização"),
    }

}
