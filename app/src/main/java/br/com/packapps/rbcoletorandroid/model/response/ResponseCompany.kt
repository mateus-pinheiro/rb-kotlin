package br.com.packapps.rbcoletorandroid.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCompany(
    @Expose
    @SerializedName("company_name")
    var companyName : String,

    @Expose
    @SerializedName("cnpj")
    var cnpj : String){

    override fun toString(): String {
        return "ResponseCompany(companyName='$companyName', cnpj='$cnpj')"
    }
}
