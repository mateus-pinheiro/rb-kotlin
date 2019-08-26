package br.com.packapps.rbcoletorandroid.model.body

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.sql.Timestamp


class FinalizeBody() : Parcelable {

    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("trackableItemIds")
    @Expose
    var trackableItemIds: MutableList<String>? = mutableListOf()

    @SerializedName("timestamp")
    @Expose
    var timestamp: Long? = null

    @SerializedName("timezoneOffset")
    @Expose
    var timezoneOffset: Int? = null

    @SerializedName("docs")
    @Expose
    var docs: MutableList<Doc>? = null

    @SerializedName("info")
    @Expose
    var info: String? = null

    @SerializedName("latitude")
    @Expose
    var latitude: Double? = null
    @SerializedName("longitude")
    @Expose
    var longitude: Double? = null
    @SerializedName("finInfo")
    @Expose
    var finInfo: FinInfo? = null

    constructor(parcel: Parcel) : this() {
        code = parcel.readValue(Int::class.java.classLoader) as? Int
        timezoneOffset = parcel.readValue(Int::class.java.classLoader) as? Int
        latitude = parcel.readValue(Double::class.java.classLoader) as? Double
        longitude = parcel.readValue(Double::class.java.classLoader) as? Double
        finInfo = parcel.readParcelable(FinInfo::class.java.classLoader)
        info = parcel.readString()
    }

    class FinInfo() : Parcelable {

        @SerializedName("cid")
        @Expose
        var cid: String? = null

        @SerializedName("cps")
        @Expose
        var cps: String? = null

        @SerializedName("cpfBuyer")
        @Expose
        var cpfBuyer: String? = null

        @SerializedName("cpfPatient")
        @Expose
        var cpfPatient: String? = null

        @SerializedName("prescriptionDate")
        @Expose
        var prescriptionDate: Long? = null

        constructor(parcel: Parcel) : this() {
            cid = parcel.readString()
            cps = parcel.readString()
            cpfBuyer = parcel.readString()
            cpfPatient = parcel.readString()
            prescriptionDate = parcel.readValue(Long::class.java.classLoader) as? Long
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(cid)
            parcel.writeString(cps)
            parcel.writeString(cpfBuyer)
            parcel.writeString(cpfPatient)
            parcel.writeValue(prescriptionDate)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<FinInfo> {
            override fun createFromParcel(parcel: Parcel): FinInfo {
                return FinInfo(parcel)
            }

            override fun newArray(size: Int): Array<FinInfo?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(code)
        parcel.writeValue(timezoneOffset)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        parcel.writeParcelable(finInfo, flags)
        parcel.writeString(info)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FinalizeBody> {
        override fun createFromParcel(parcel: Parcel): FinalizeBody {
            return FinalizeBody(parcel)
        }

        override fun newArray(size: Int): Array<FinalizeBody?> {
            return arrayOfNulls(size)
        }
    }

    class Doc(

        @SerializedName("type")
        @Expose
        var type: String,
        @SerializedName("id")
        @Expose
        var id: String
    )
}


//json a ser enviado
//{
//    "code":1017,
//    "trackableItemIds":["003443400434343","00737433494342"],
//    "timestamp":1604828336,
//    "timezoneOffset":-3,
//    "docs": [{"type": "PO", "id": "36374343"}, {"type": "NF", "id": "89223.2323"}],
//    "latitude":-22.9033175,
//    "longitude":-43.177173599999996,
//    "finInfo": {
//    "cid": "label",
//    "cps": "label",
//    "cpfBuyer": "label",
//    "cpfPatient": "label",
//    "prescriptionDate": "Date"
//     }
//}