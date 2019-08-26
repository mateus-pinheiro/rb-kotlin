package br.com.packapps.rbcoletorandroid.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import android.content.ClipData.Item


class ResponseExitMovement(
    @SerializedName("data")
    @Expose
    val data: Data? = null
) {

    class Data {

        @SerializedName("nfCount")
        @Expose
        val nfCount: List<NfCount>? = null

        @SerializedName("item")
        @Expose
        val item: Item? = null


    }

    class NfCount {

        @SerializedName("batchObject")
        @Expose
        val batchObject: BatchObject? = null
        @SerializedName("amount")
        @Expose
        val amount: Int? = null

    }

    class BatchObject {

        @SerializedName("identifier")
        @Expose
        val identifier: String? = null
        @SerializedName("identifierType")
        @Expose
        val identifierType: String? = null
        @SerializedName("batch")
        @Expose
        val batch: String? = null
        @SerializedName("presentation")
        @Expose
        val presentation: String? = null
        @SerializedName("presentationType")
        @Expose
        val presentationType: String? = null
    }

    class Item {
        @SerializedName("id")
        @Expose
        val id: String? = null
        @SerializedName("serialNumber")
        @Expose
        val serialNumber: String? = null
        @SerializedName("aggrId")
        @Expose
        val aggrId: String? = null
        @SerializedName("batchObject")
        @Expose
        val batchObjectItem: BatchObjectItem? = null
        @SerializedName("product")
        @Expose
        val product: Product? = null
        @SerializedName("company")
        @Expose
        val company: Company? = null
        @SerializedName("owner")
        @Expose
        val owner: Owner? = null
        @SerializedName("currentParents")
        @Expose
        val currentParents: List<Any>? = null
        @SerializedName("lastEvent")
        @Expose
        val lastEvent: LastEvent? = null
    }

    class BatchObjectItem {
        @SerializedName("expirationDate")
        @Expose
        val expirationDate: String? = null
        @SerializedName("productionDate")
        @Expose
        val productionDate: String? = null
    }

    class Product {
        @SerializedName("name")
        @Expose
        val name: String? = null

    }

    class Owner {
        @SerializedName("name")
        @Expose
        val name: String? = null
        @SerializedName("identifier")
        @Expose
        val identifier: String? = null

    }

    class ToCompany {
        @SerializedName("name")
        @Expose
        val name: String? = null

    }

    class LastEvent {

        @SerializedName("eventId")
        @Expose
        val eventId: String? = null
        @SerializedName("code")
        @Expose
        val code: Code? = null

        @SerializedName("toCompany")
        @Expose
        val toCompany: ToCompany? = null
    }

    class Code {
        @SerializedName("type")
        @Expose
        val type: String? = null
    }

    class Company {
        @SerializedName("id")
        @Expose
        val id: Int? = null
        @SerializedName("name")
        @Expose
        val name: String? = null
    }

}