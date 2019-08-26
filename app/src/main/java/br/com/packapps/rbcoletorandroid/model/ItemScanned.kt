package br.com.packapps.rbcoletorandroid.model

import android.os.Parcel
import android.os.Parcelable

class ItemScanned(var barCode: String? = null, var type: Int?) : Parcelable{



    constructor(parcel: Parcel) : this(null, null) {
        barCode = parcel.readString()
        type = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(barCode)
        parcel.writeValue(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemScanned> {
        override fun createFromParcel(parcel: Parcel): ItemScanned {
            return ItemScanned(parcel)
        }

        override fun newArray(size: Int): Array<ItemScanned?> {
            return arrayOfNulls(size)
        }
    }



    enum class BarcodeType(val code : Int){
        BARCODE_NORMAL(0),
        BARCODE_DATAMATRIX(1),
        BARCODE_NF(2)
    }
}
