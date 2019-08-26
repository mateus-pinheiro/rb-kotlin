package br.com.packapps.rbcoletorandroid.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCompanyType(

    @SerializedName("data")
    @Expose
    var data: Data?

) {

    inner class Data(

        @SerializedName("company")
        @Expose
        var company: Company?

    )

    inner class Company(
        @SerializedName("id")
        @Expose
        var id: Int?,

        @SerializedName("identifier")
        @Expose
        var identifier: String?,

        @SerializedName("name")
        @Expose
        var name: String?,

        @SerializedName("typePrefixCode")
        @Expose
        var typePrefixCode: String?

    )

}