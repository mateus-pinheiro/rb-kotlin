package br.com.packapps.rbcoletorandroid.model.body

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

class EntryFinishActionBody() : Parcelable {

    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("trackableItemIds")
    @Expose
    var trackableItemIds: List<String?> = listOf()

    @SerializedName("checkNFContent")
    @Expose
    var checkNFContent: Boolean? = null

    @SerializedName("checkoutEventId")
    @Expose
    var checkoutEventId: String? = null

    @SerializedName("timestamp")
    @Expose
    var timestamp: Long? = null

    @SerializedName("timezoneOffset")
    @Expose
    var timezoneOffset: Int? = null

    @SerializedName("docs")
    @Expose
    var docs: MutableList<Doc>? = null

    @SerializedName("latitude")
    @Expose
    var latitude: Double? = null

    @SerializedName("longitude")
    @Expose
    var longitude: Double? = null

    @SerializedName("toUniversalCompanyId")
    @Expose
    var toUniversalCompanyId: String? = null

    @SerializedName("toCpRegType")
    @Expose
    var toCpRegType: Int? = null

    @SerializedName("toFiscalCpId")
    @Expose
    var toFiscalCpId: String? = null

    @SerializedName("toFiscalRegType")
    @Expose
    var toFiscalRegType: Int? = null

    @SerializedName("fromUniversalCompanyId")
    @Expose
    var fromUniversalCompanyId: String? = null

    @SerializedName("fromCpRegType")
    @Expose
    var fromCpRegType: Int? = null

    @SerializedName("fromFiscalCpId")
    @Expose
    var fromFiscalCpId: String? = null

    @SerializedName("fromFiscalRegType")
    @Expose
    var fromFiscalRegType: Int? = null

    @SerializedName("byUniversalCompanyId")
    @Expose
    var byUniversalCompanyId: String? = null

    @SerializedName("byCpRegType")
    @Expose
    var byCpRegType: Int? = null

    @SerializedName("unpack")
    @Expose
    var unpack: Boolean? = false

    constructor(parcel: Parcel) : this() {
        code = parcel.readValue(Int::class.java.classLoader) as? Int
        trackableItemIds = parcel.createStringArrayList()
        timestamp = parcel.readValue(Long::class.java.classLoader) as? Long
        timezoneOffset = parcel.readValue(Int::class.java.classLoader) as? Int
        docs = parcel.createTypedArrayList(Doc)
        latitude = parcel.readValue(Double::class.java.classLoader) as? Double
        longitude = parcel.readValue(Double::class.java.classLoader) as? Double
        toUniversalCompanyId = parcel.readString()
        toCpRegType = parcel.readValue(Int::class.java.classLoader) as? Int
        toFiscalCpId = parcel.readString()
        toFiscalRegType = parcel.readValue(Int::class.java.classLoader) as? Int
        fromUniversalCompanyId = parcel.readString()
        fromCpRegType = parcel.readValue(Int::class.java.classLoader) as? Int
        fromFiscalCpId = parcel.readString()
        fromFiscalRegType = parcel.readValue(Int::class.java.classLoader) as? Int
        byUniversalCompanyId = parcel.readString()
        byCpRegType = parcel.readValue(Int::class.java.classLoader) as? Int
        unpack = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    }


    class Doc() : Parcelable {


        @SerializedName("type")
        @Expose
        var type: String? = null
        @SerializedName("key")
        @Expose
        var key: String? = null
        @SerializedName("id")
        @Expose
        var id: String? = null
        constructor(parcel: Parcel) : this() {
            type = parcel.readString()
            key = parcel.readString()
            id = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(type)
            parcel.writeString(key)
            parcel.writeString(id)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Doc> {
            override fun createFromParcel(parcel: Parcel): Doc {
                return Doc(parcel)
            }

            override fun newArray(size: Int): Array<Doc?> {
                return arrayOfNulls(size)
            }
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(code)
        parcel.writeStringList(trackableItemIds)
        parcel.writeValue(timestamp)
        parcel.writeValue(timezoneOffset)
        parcel.writeTypedList(docs)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        parcel.writeString(toUniversalCompanyId)
        parcel.writeValue(toCpRegType)
        parcel.writeString(toFiscalCpId)
        parcel.writeValue(toFiscalRegType)
        parcel.writeString(fromUniversalCompanyId)
        parcel.writeValue(fromCpRegType)
        parcel.writeString(fromFiscalCpId)
        parcel.writeValue(fromFiscalRegType)
        parcel.writeString(byUniversalCompanyId)
        parcel.writeValue(byCpRegType)
        parcel.writeValue(unpack)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EntryFinishActionBody> {
        override fun createFromParcel(parcel: Parcel): EntryFinishActionBody {
            return EntryFinishActionBody(parcel)
        }

        override fun newArray(size: Int): Array<EntryFinishActionBody?> {
            return arrayOfNulls(size)
        }
    }
}

//{
//    "code":3001,
//    "trackableItemId":"003443400434343",
//    "timestamp":1604828336,
//    "timezoneOffset":-3,
//    "docs": [{"type": "PO", "id": "36374343"}, {"type": "NF", "id": "89223.2323"}],
//    "latitude":-22.9033175,
//    "longitude":-43.177173599999996,
//    "toUniversalCompanyId":"00000000000000",
//    "toCpRegType":1,
//    "toFiscalCpId":"00000000000000",
//    "toFiscalRegType":1,
//    "fromUniversalCompanyId":"00000000000000",
//    "fromCpRegType":1,
//    "fromFiscalCpId":"00000000000000",
//    "fromFiscalRegType":1,
//    "byUniversalCompanyId":"89281688000139",
//    "byCpRegType":1,
//    "unpack":true
//}
