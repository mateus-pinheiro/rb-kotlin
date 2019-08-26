package br.com.packapps.rbcoletorandroid.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ResponseEntryListNFs(

    @SerializedName("data")
    @Expose
    var data: Data?

) {

    inner class Data(
        @SerializedName("checkouts")
        @Expose
        var checkouts: MutableList<Checkout>?
    )

    inner class Checkout(
        @SerializedName("eventId")
        @Expose
        var eventId: String?,

        @SerializedName("code")
        @Expose
        var code: Code?,

        @SerializedName("docs")
        @Expose
        var docs: MutableList<Docs>?,

        @SerializedName("items")
        @Expose
        var items: MutableList<Items>?,

        @SerializedName("date")
        @Expose
        var date: String?,

        @SerializedName("insertionTs")
        @Expose
        var insertionTs: String?,

        @SerializedName("affectedItems")
        @Expose
        var affectedItems: Int?


    )

    inner class Docs(

        @SerializedName("id")
        @Expose
        var id: String?,

        @SerializedName("type")
        @Expose
        var type: String?,

        @SerializedName("key")
        @Expose
        var key: String?

    )

    inner class Items(

        @SerializedName("id")
        @Expose
        var id: String?,

        @SerializedName("aggrId")
        @Expose
        var aggrId: String?

    )

    inner class Code(

        @SerializedName("id")
        @Expose
        var id: Long?,

        @SerializedName("code")
        @Expose
        var code: String?,

        @SerializedName("type")
        @Expose
        var type: String?

    )
}


//{
//    "data": {
//    "checkouts": [
//    {
//        "eventId": "61ac5010-022c-11e9-ac66-a375704f10e2",
//        "sourceGroupId": null,
//        "code": {
//        "id": 4001,
//        "code": "SALE",
//        "type": "OTHER"
//    },
//        "docs": [
//        {
//            "id": "000140014009",
//            "type": "NF",
//            "key": "35160625918684000136550090001400141104878121"
//        }
//        ],
//        "cancelled": false,
//        "date": "Mon Dec 17 2018 18:47:00 GMT+0000 (UTC)",
//        "insertionTs": "Mon Dec 17 2018 18:48:45 GMT+0000 (UTC)",
//        "affectedItems": 2
//    },
//    {
//        "eventId": "11ed6930-fa41-11e8-8564-b94ba84765a7",
//        "sourceGroupId": null,
//        "code": {
//        "id": 4001,
//        "code": "SALE",
//        "type": "OTHER"
//    },
//        "docs": [
//        {
//            "id": "000172584004",
//            "type": "NF",
//            "key": "35160625918684000136550040001725841104878121"
//        }
//        ],
//        "cancelled": false,
//        "date": "Fri Dec 07 2018 16:55:00 GMT+0000 (UTC)",
//        "insertionTs": "Fri Dec 07 2018 16:56:42 GMT+0000 (UTC)",
//        "affectedItems": 10
//    }
//    ]
//}
//}