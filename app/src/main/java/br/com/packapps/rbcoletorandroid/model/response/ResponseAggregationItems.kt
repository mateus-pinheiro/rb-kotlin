package br.com.packapps.rbcoletorandroid.model.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseAggregationItems(): Parcelable {

    @SerializedName("data")
    @Expose
    var data: Data? = null

    constructor(parcel: Parcel) : this() {
        data = parcel.readParcelable(Data::class.java.classLoader)
    }

    class Data() : Parcelable {

        @SerializedName("itemsByParent")
        @Expose
        var itemsByParent: List<ItemsByParent>? = null

        constructor(parcel: Parcel) : this() {
            itemsByParent = parcel.createTypedArrayList(ItemsByParent)
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeTypedList(itemsByParent)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Data> {
            override fun createFromParcel(parcel: Parcel): Data {
                return Data(parcel)
            }

            override fun newArray(size: Int): Array<Data?> {
                return arrayOfNulls(size)
            }
        }

    }

    class ItemsByParent() : Parcelable {

        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("aggrId")
        @Expose
        var aggrId: String? = null

        @SerializedName("parent")
        @Expose
        var parent: Parent? = null

        @SerializedName("company")
        @Expose
        var company: Company? = null

        constructor(parcel: Parcel) : this() {
            id = parcel.readString()
            aggrId = parcel.readString()
            company = parcel.readParcelable(Company::class.java.classLoader)
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(aggrId)
            parcel.writeParcelable(company, flags)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ItemsByParent> {
            override fun createFromParcel(parcel: Parcel): ItemsByParent {
                return ItemsByParent(parcel)
            }

            override fun newArray(size: Int): Array<ItemsByParent?> {
                return arrayOfNulls(size)
            }
        }

    }

    class Company() : Parcelable {

        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        constructor(parcel: Parcel) : this() {
            id = parcel.readValue(Int::class.java.classLoader) as? Int
            name = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(id)
            parcel.writeString(name)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Company> {
            override fun createFromParcel(parcel: Parcel): Company {
                return Company(parcel)
            }

            override fun newArray(size: Int): Array<Company?> {
                return arrayOfNulls(size)
            }
        }
    }

    class Parent() : Parcelable {

        @SerializedName("lastEvent")
        @Expose
        var lastEvent: LastEvent? = null


        constructor(parcel: Parcel) : this() {
            lastEvent = parcel.readParcelable(Company::class.java.classLoader)
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(lastEvent)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Company> {
            override fun createFromParcel(parcel: Parcel): Company {
                return Company(parcel)
            }

            override fun newArray(size: Int): Array<Company?> {
                return arrayOfNulls(size)
            }
        }
    }


    class LastEvent() : Parcelable {

        @SerializedName("volumeTypeCode")
        @Expose
        var volumeTypeCode: VolumeTypeCode? = null


        constructor(parcel: Parcel) : this() {
            volumeTypeCode = parcel.readParcelable(Company::class.java.classLoader)
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(volumeTypeCode)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Company> {
            override fun createFromParcel(parcel: Parcel): Company {
                return Company(parcel)
            }

            override fun newArray(size: Int): Array<Company?> {
                return arrayOfNulls(size)
            }
        }
    }

    class VolumeTypeCode() : Parcelable {

        @SerializedName("code")
        @Expose
        var code: Int? = null

        constructor(parcel: Parcel) : this() {
            code = parcel.readValue(Int::class.java.classLoader) as? Int
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(code)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Company> {
            override fun createFromParcel(parcel: Parcel): Company {
                return Company(parcel)
            }

            override fun newArray(size: Int): Array<Company?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(data, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResponseAggregationItems> {
        override fun createFromParcel(parcel: Parcel): ResponseAggregationItems {
            return ResponseAggregationItems(parcel)
        }

        override fun newArray(size: Int): Array<ResponseAggregationItems?> {
            return arrayOfNulls(size)
        }
    }
}