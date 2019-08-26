package br.com.packapps.rbcoletorandroid.model.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import br.com.packapps.rbcoletorandroid.model.response.ResponseFinalizeCheck.ItemsById
import br.com.packapps.rbcoletorandroid.model.response.ResponseFinalizeCheck.Product


class ResponseFinalizeCheck() : Parcelable{

    @SerializedName("data")
    @Expose
    var data: Data? = null

//    var type: Int? = null

    var barCode: String? = null

    var justBarcode: Boolean = false

    constructor(parcel: Parcel) : this() {
        data = parcel.readParcelable(Data::class.java.classLoader)
//        type = parcel.readValue(Int::class.java.classLoader) as? Int
        barCode = parcel.readString()
    }


    class Product() : Parcelable{

        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("company")
        @Expose
        var company: Company? = null

        constructor(parcel: Parcel) : this() {
            name = parcel.readString()
            company = parcel.readParcelable(Company::class.java.classLoader)
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(name)
            parcel.writeParcelable(company, flags)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Product> {
            override fun createFromParcel(parcel: Parcel): Product {
                return Product(parcel)
            }

            override fun newArray(size: Int): Array<Product?> {
                return arrayOfNulls(size)
            }
        }

    }


    class ItemsById() : Parcelable{

        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("aggrId")
        @Expose
        var aggrId: String? = null
        @SerializedName("serialNumber")
        @Expose
        var serialNumber: String? = null
        @SerializedName("content")
        @Expose
        var content: MutableList<Content>? = null

        constructor(parcel: Parcel) : this() {
            id = parcel.readString()
            aggrId = parcel.readString()
            serialNumber = parcel.readString()
            content = parcel.createTypedArrayList(Content)
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(aggrId)
            parcel.writeString(serialNumber)
            parcel.writeTypedList(content)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ItemsById> {
            override fun createFromParcel(parcel: Parcel): ItemsById {
                return ItemsById(parcel)
            }

            override fun newArray(size: Int): Array<ItemsById?> {
                return arrayOfNulls(size)
            }
        }

    }


    class Data() : Parcelable{

        @SerializedName("itemsById")
        @Expose
        var itemsById: MutableList<ItemsById>? = null

        constructor(parcel: Parcel) : this() {
            itemsById = parcel.createTypedArrayList(ItemsById)
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeTypedList(itemsById)
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


    class Content() : Parcelable{

        @SerializedName("quantity")
        @Expose
        var quantity: Int? = null
        @SerializedName("batchObject")
        @Expose
        var batchObject: BatchObject? = null

        constructor(parcel: Parcel) : this() {
            quantity = parcel.readValue(Int::class.java.classLoader) as? Int
            batchObject = parcel.readParcelable(BatchObject::class.java.classLoader)
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(quantity)
            parcel.writeParcelable(batchObject, flags)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Content> {
            override fun createFromParcel(parcel: Parcel): Content {
                return Content(parcel)
            }

            override fun newArray(size: Int): Array<Content?> {
                return arrayOfNulls(size)
            }
        }

    }


    class Company() : Parcelable{

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


    class BatchObject() : Parcelable{
        @SerializedName("batch")
        @Expose
        var batch: String? = null

        @SerializedName("expirationDate")
        @Expose
        var expirationDate: String? = null

        @SerializedName("productionDate")
        @Expose
        var productionDate: String? = null

        @SerializedName("product")
        @Expose
        var product: Product? = null

        constructor(parcel: Parcel) : this() {
            batch = parcel.readString()
            expirationDate = parcel.readString()
            productionDate = parcel.readString()
            product = parcel.readParcelable(Product::class.java.classLoader)
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(batch)
            parcel.writeString(expirationDate)
            parcel.writeString(productionDate)
            parcel.writeParcelable(product, flags)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<BatchObject> {
            override fun createFromParcel(parcel: Parcel): BatchObject {
                return BatchObject(parcel)
            }

            override fun newArray(size: Int): Array<BatchObject?> {
                return arrayOfNulls(size)
            }
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(data, flags)
//        parcel.writeValue(type)
        parcel.writeString(barCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResponseFinalizeCheck> {
        override fun createFromParcel(parcel: Parcel): ResponseFinalizeCheck {
            return ResponseFinalizeCheck(parcel)
        }

        override fun newArray(size: Int): Array<ResponseFinalizeCheck?> {
            return arrayOfNulls(size)
        }
    }


}


//json de retorno {
//    "data": {
//        "itemsById": [
//            {
//                "id": "6-317135286163660",
//                "aggrId": "317135286163660",
//                "content": [
//                    {
//                        "quantity": 250,
//                        "batchObject": {
//                            "product": {
//                                "name": "VENZER",
//                                "company": {
//                                    "id": 6,
//                                    "name": "LIBBS FARMACÊUTICA LTDA - Barra Funda"
//                                }
//                            }
//                        }
//                    }
//                ]
//            }
//        ]
//    }
//}