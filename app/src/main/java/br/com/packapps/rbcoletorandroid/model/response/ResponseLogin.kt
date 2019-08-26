package br.com.packapps.rbcoletorandroid.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseLogin (
    @Expose
    @SerializedName("Auth-Token") var authToken : String,

    @Expose
    @SerializedName("Login") var login : String,

    @Expose
    @SerializedName("jwt") var jwt : String

){

    override fun toString(): String {
        return "ResponseLogin(authToken='$authToken', login='$login', jwt='$jwt')"
    }
}
