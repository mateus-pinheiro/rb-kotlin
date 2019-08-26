package br.com.packapps.rbcoletorandroid.model

import br.com.packapps.rbcoletorandroid.R

class FinalizeReasonItem(var title: String, var color : Int, var code : Int)

object SupplierFinalizeReason {

    val items = listOf<FinalizeReasonItem>(

        //9000 export
        FinalizeReasonItem("Exportação", R.color.colorReason7, 9000),

        //5005 discard
        FinalizeReasonItem("Descarte", R.color.colorReason7, 5005),

        //5011 disappearence
        FinalizeReasonItem("Desaparecimento", R.color.colorReason7, 5011),

        //Avaria
        FinalizeReasonItem("Avaria", R.color.colorReason7, 5007),

        //5008 theft
        FinalizeReasonItem("Roubo", R.color.colorReason7, 5008),

        //5012 Confiscation_or_retention
        FinalizeReasonItem("Confisco", R.color.colorReason7, 5012)
    )

    val items1 = listOf<FinalizeReasonItem>(

        //5001 dispensation
        FinalizeReasonItem("Dispensação", R.color.colorReason7, 5001),


        //5011 disappearence
        FinalizeReasonItem("Desaparecimento", R.color.colorReason7, 5011),

        //5008 theft
        FinalizeReasonItem("Roubo", R.color.colorReason7, 5008),

        //1019 Retention
        FinalizeReasonItem("Avaria", R.color.colorReason7, 5007),

        //5012 Confiscation_or_retention
        FinalizeReasonItem("Confisco", R.color.colorReason7, 5012),

        //5006 deplete
        FinalizeReasonItem("Deslacre", R.color.colorReason7, 5006)
    )

}

