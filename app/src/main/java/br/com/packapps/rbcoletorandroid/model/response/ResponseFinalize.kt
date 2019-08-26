package br.com.packapps.rbcoletorandroid.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ResponseFinalize (

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
//success message
//{
//    "token": "0000O5BQGJ0W",
//    "httpCode": 200
//}

//single error
//{
//    "number": 2000,
//    "name": "USER_NOT_LOGGED",
//    "httpCode": 401
//}

//single error with items
// {
//  "number": 7000,
//  "name": "DUPLICATED_IUM",
//  "httpCode": 400,
//  "items": [
//    "100330154001214454349586051014H2Z8ZV66",
//    "100330154001214454349586061014H2Z8ZV66",
//    "100330154001214454349586071014H2Z8ZV66"
//  ],
//  "token": "000O5BQLN01C"
//}




