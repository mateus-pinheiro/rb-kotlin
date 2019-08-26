package br.com.packapps.rbcoletorandroid.model.body

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class AggregationMessageBody (

    @SerializedName("code")
    @Expose
    val code: Int? = null,

    @SerializedName("parentId")
    @Expose
    val parentId: String? = null,

    @SerializedName("childrenIds")
    @Expose
    val childrenIds: List<String>? = null,

    @SerializedName("volumeTypeCode")
    @Expose
    val volumeTypeCode: Int? = null,

    @SerializedName("info")
    @Expose
    val info: String? = null,

    @SerializedName("newAggr")
    @Expose
    val newAggr: Boolean? = null,

    @SerializedName("unpack")
    @Expose
    val unpack: Boolean? = false,

    @SerializedName("timestamp")
    @Expose
    val timestamp: Long? = null,

    @SerializedName("timezoneOffset")
    @Expose
    val timezoneOffset: Int? = null

)