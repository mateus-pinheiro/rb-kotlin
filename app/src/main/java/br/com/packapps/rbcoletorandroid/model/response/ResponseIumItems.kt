package br.com.packapps.rbcoletorandroid.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseIumItems(

    @SerializedName("data")
    @Expose
    var data: Data?

) {
    inner class Data(

        @SerializedName("itemsByEventId")
        @Expose
        var itemsByEventId: MutableList<ItemsByEventId>?
    )

    inner class ItemsByEventId(

        @SerializedName("id")
        @Expose
        var id: String?,

        @SerializedName("aggrId")
        @Expose
        var aggrId: String?,

        @SerializedName("presentation")
        @Expose
        var presentation: String?, //foreign key com batche

        @SerializedName("identifier")
        @Expose
        var identifier: String?,

        @SerializedName("batchObject")
        @Expose
        var batchObject: BatchObject?

    )

    inner class BatchObject (
        @SerializedName("batch")
        @Expose
        var batch: String?
    )
}