package br.com.packapps.rbcoletorandroid.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ResponseCompaniesByUser(

    @SerializedName("data")
    @Expose
    val data: Data? = null
) {
    class Data {
        @SerializedName("companies")
        @Expose
        val companies: MutableList<Company>? = null
    }

    class Company {
        @SerializedName("id")
        @Expose
        val id: Int? = null
        @SerializedName("identifier")
        @Expose
        val identifier: String? = null
        @SerializedName("name")
        @Expose
        val name: String? = null
    }

}