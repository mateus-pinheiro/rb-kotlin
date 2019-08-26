package br.com.packapps.rbcoletorandroid.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ResponseCompanyId (

    @SerializedName("companies")
    @Expose
    var companies: List<Long>?,

    @SerializedName("is_super")
    @Expose
    var isSuper: Boolean?

    )