package br.com.packapps.rbcoletorandroid.model

import com.google.gson.annotations.Expose

class MyErrorBody(
    @Expose
    var httpCode : Int ,
    @Expose
    var name: String,
    @Expose
    var number : String)