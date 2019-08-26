package br.com.packapps.rbcoletorandroid.model.body

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginBody(
    @Expose
    var login : String,

    @Expose
    var password : String

)