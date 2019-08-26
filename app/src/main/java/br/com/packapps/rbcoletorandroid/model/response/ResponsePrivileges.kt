package br.com.packapps.rbcoletorandroid.model.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponsePrivileges() : Parcelable {

    @SerializedName("permissions")
    @Expose
    var permissions: List<String>? = null

    @SerializedName("is_super")
    @Expose
    var isSuper: Boolean? = null

    constructor(parcel: Parcel) : this() {
        permissions = parcel.createStringArrayList()
        isSuper = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(permissions)
        parcel.writeValue(isSuper)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResponsePrivileges> {
        override fun createFromParcel(parcel: Parcel): ResponsePrivileges {
            return ResponsePrivileges(parcel)
        }

        override fun newArray(size: Int): Array<ResponsePrivileges?> {
            return arrayOfNulls(size)
        }
    }
}
