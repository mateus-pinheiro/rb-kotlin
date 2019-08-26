package br.com.packapps.rbcoletorandroid.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ResponseEntryFinishAction(
    @SerializedName("token")
    @Expose
    var token: String?,
    @SerializedName("httpCode")
    @Expose
    var httpCode: Int?,
    @SerializedName("number")
    @Expose
    var number: Int?,
    @SerializedName("name")
    @Expose
    var name: String?,
    @SerializedName("items")
    @Expose
    var items: List<String>?
) {
    override fun toString(): String {
        return "ResponseFinalize(token=$token, httpCode=$httpCode, number=$number, name=$name, items=$items)"
    }
}
