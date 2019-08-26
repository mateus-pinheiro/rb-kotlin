package br.com.packapps.rbcoletorandroid.model.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseNfeCheckout() : Parcelable {

    @SerializedName("_id")
    @Expose
    var id: String? = null
    @SerializedName("products")
    @Expose
    var products: MutableList<Product>? = null
    @SerializedName("number")
    @Expose
    var number: String? = null
    @SerializedName("series")
    @Expose
    var series: String? = null
    @SerializedName("destinationCompany")
    @Expose
    var destinationCompany: DestinationCompany? = null
    @SerializedName("sourceCompany")
    @Expose
    var sourceCompany: SourceCompany? = null
    @SerializedName("transportationCompany")
    @Expose
    var transportationCompany: TransportationCompany? = null
    @SerializedName("emails")
    @Expose
    var emails: List<Email>? = null
    @SerializedName("insertionTs")
    @Expose
    var insertionTs: Int? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        products = parcel.createTypedArrayList(Product)
        number = parcel.readString()
        series = parcel.readString()
        destinationCompany = parcel.readParcelable(DestinationCompany::class.java.classLoader)
        sourceCompany = parcel.readParcelable(SourceCompany::class.java.classLoader)
        transportationCompany = parcel.readParcelable(TransportationCompany::class.java.classLoader)
        emails = parcel.createTypedArrayList(Email)
        insertionTs = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    class DestinationCompany() : Parcelable {

        @SerializedName("cnpj")
        @Expose
        var cnpj: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("address")
        @Expose
        var address: String? = null

        @SerializedName("city")
        @Expose
        var city: String? = null

        @SerializedName("cityCode")
        @Expose
        var cityCode: String? = null

        @SerializedName("country")
        @Expose
        var country: String? = null

        @SerializedName("countryCode")
        @Expose
        var countryCode: String? = null

        @SerializedName("district")
        @Expose
        var district: String? = null

        @SerializedName("number")
        @Expose
        var number: String? = null

        @SerializedName("phone")
        @Expose
        var phone: String? = null

        @SerializedName("state")
        @Expose
        var state: String? = null

        @SerializedName("zipCode")
        @Expose
        var zipCode: String? = null

        constructor(parcel: Parcel) : this() {
            cnpj = parcel.readString()
            name = parcel.readString()
            address = parcel.readString()
            city = parcel.readString()
            cityCode = parcel.readString()
            country = parcel.readString()
            countryCode = parcel.readString()
            district = parcel.readString()
            number = parcel.readString()
            phone = parcel.readString()
            state = parcel.readString()
            zipCode = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(cnpj)
            parcel.writeString(name)
            parcel.writeString(address)
            parcel.writeString(city)
            parcel.writeString(cityCode)
            parcel.writeString(country)
            parcel.writeString(countryCode)
            parcel.writeString(district)
            parcel.writeString(number)
            parcel.writeString(phone)
            parcel.writeString(state)
            parcel.writeString(zipCode)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<DestinationCompany> {
            override fun createFromParcel(parcel: Parcel): DestinationCompany {
                return DestinationCompany(parcel)
            }

            override fun newArray(size: Int): Array<DestinationCompany?> {
                return arrayOfNulls(size)
            }
        }

    }

    class Email() : Parcelable {

        @SerializedName("email")
        @Expose
        var email: String? = null
        @SerializedName("file")
        @Expose
        var file: String? = null
        @SerializedName("date")
        @Expose
        var date: String? = null

        constructor(parcel: Parcel) : this() {
            email = parcel.readString()
            file = parcel.readString()
            date = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(email)
            parcel.writeString(file)
            parcel.writeString(date)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Email> {
            override fun createFromParcel(parcel: Parcel): Email {
                return Email(parcel)
            }

            override fun newArray(size: Int): Array<Email?> {
                return arrayOfNulls(size)
            }
        }

    }

    class Product() : Parcelable {

        var dataMatrixReaded: Boolean = false

        @SerializedName("CFOP")
        @Expose
        var cFOP: String? = null
        @SerializedName("EANcomercial")
        @Expose
        var eANcomercial: String? = null
        @SerializedName("EANfiscal")
        @Expose
        var eANfiscal: String? = null
        @SerializedName("NCMcode")
        @Expose
        var nCMcode: String? = null
        @SerializedName("commercialUnit")
        @Expose
        var commercialUnit: String? = null
        @SerializedName("commercialUnitVarue")
        @Expose
        var commercialUnitVarue: String? = null
        @SerializedName("fiscalUnitVarue")
        @Expose
        var fiscalUnitVarue: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("productCode")
        @Expose
        var productCode: String? = null
        @SerializedName("purchaseOrderNumber")
        @Expose
        var purchaseOrderNumber: String? = null
        @SerializedName("value")
        @Expose
        var value: Int? = null
        @SerializedName("batchCode")
        @Expose
        var batchCode: String? = null
        @SerializedName("batchCodeQuantity")
        @Expose
        var batchCodeQuantity: String? = null
        @SerializedName("expirationDate")
        @Expose
        var expirationDate: String? = null
        @SerializedName("productionDate")
        @Expose
        var productionDate: String? = null
        @SerializedName("quant")
        @Expose
        var quant: String? = null

        constructor(parcel: Parcel) : this() {
            cFOP = parcel.readString()
            eANcomercial = parcel.readString()
            eANfiscal = parcel.readString()
            nCMcode = parcel.readString()
            commercialUnit = parcel.readString()
            commercialUnitVarue = parcel.readString()
            fiscalUnitVarue = parcel.readString()
            name = parcel.readString()
            productCode = parcel.readString()
            purchaseOrderNumber = parcel.readString()
            value = parcel.readValue(Int::class.java.classLoader) as? Int
            batchCode = parcel.readString()
            batchCodeQuantity = parcel.readString()
            expirationDate = parcel.readString()
            productionDate = parcel.readString()
            quant = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(cFOP)
            parcel.writeString(eANcomercial)
            parcel.writeString(eANfiscal)
            parcel.writeString(nCMcode)
            parcel.writeString(commercialUnit)
            parcel.writeString(commercialUnitVarue)
            parcel.writeString(fiscalUnitVarue)
            parcel.writeString(name)
            parcel.writeString(productCode)
            parcel.writeString(purchaseOrderNumber)
            parcel.writeValue(value)
            parcel.writeString(batchCode)
            parcel.writeString(batchCodeQuantity)
            parcel.writeString(expirationDate)
            parcel.writeString(productionDate)
            parcel.writeString(quant)
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

    class SourceCompany() : Parcelable {

        @SerializedName("cnpj")
        @Expose
        var cnpj: String? = null
        @SerializedName("fantasyName")
        @Expose
        var fantasyName: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("stateInscription")
        @Expose
        var stateInscription: String? = null
        @SerializedName("address")
        @Expose
        var address: String? = null
        @SerializedName("city")
        @Expose
        var city: String? = null
        @SerializedName("cityCode")
        @Expose
        var cityCode: String? = null
        @SerializedName("country")
        @Expose
        var country: String? = null
        @SerializedName("countryCode")
        @Expose
        var countryCode: String? = null
        @SerializedName("district")
        @Expose
        var district: String? = null
        @SerializedName("number")
        @Expose
        var number: String? = null
        @SerializedName("phone")
        @Expose
        var phone: String? = null
        @SerializedName("state")
        @Expose
        var state: String? = null
        @SerializedName("zipCode")
        @Expose
        var zipCode: String? = null

        constructor(parcel: Parcel) : this() {
            cnpj = parcel.readString()
            fantasyName = parcel.readString()
            name = parcel.readString()
            stateInscription = parcel.readString()
            address = parcel.readString()
            city = parcel.readString()
            cityCode = parcel.readString()
            country = parcel.readString()
            countryCode = parcel.readString()
            district = parcel.readString()
            number = parcel.readString()
            phone = parcel.readString()
            state = parcel.readString()
            zipCode = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(cnpj)
            parcel.writeString(fantasyName)
            parcel.writeString(name)
            parcel.writeString(stateInscription)
            parcel.writeString(address)
            parcel.writeString(city)
            parcel.writeString(cityCode)
            parcel.writeString(country)
            parcel.writeString(countryCode)
            parcel.writeString(district)
            parcel.writeString(number)
            parcel.writeString(phone)
            parcel.writeString(state)
            parcel.writeString(zipCode)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<SourceCompany> {
            override fun createFromParcel(parcel: Parcel): SourceCompany {
                return SourceCompany(parcel)
            }

            override fun newArray(size: Int): Array<SourceCompany?> {
                return arrayOfNulls(size)
            }
        }
    }

    class TransportationCompany() : Parcelable {

        @SerializedName("cnpj")
        @Expose
        var cnpj: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("stateInscription")
        @Expose
        var stateInscription: String? = null
        @SerializedName("city")
        @Expose
        var city: String? = null
        @SerializedName("state")
        @Expose
        var state: String? = null
        @SerializedName("address")
        @Expose
        var address: String? = null

        constructor(parcel: Parcel) : this() {
            cnpj = parcel.readString()
            name = parcel.readString()
            stateInscription = parcel.readString()
            city = parcel.readString()
            state = parcel.readString()
            address = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(cnpj)
            parcel.writeString(name)
            parcel.writeString(stateInscription)
            parcel.writeString(city)
            parcel.writeString(state)
            parcel.writeString(address)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<TransportationCompany> {
            override fun createFromParcel(parcel: Parcel): TransportationCompany {
                return TransportationCompany(parcel)
            }

            override fun newArray(size: Int): Array<TransportationCompany?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeTypedList(products)
        parcel.writeString(number)
        parcel.writeString(series)
        parcel.writeParcelable(destinationCompany, flags)
        parcel.writeParcelable(sourceCompany, flags)
        parcel.writeParcelable(transportationCompany, flags)
        parcel.writeTypedList(emails)
        parcel.writeValue(insertionTs)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResponseNfeCheckout> {
        override fun createFromParcel(parcel: Parcel): ResponseNfeCheckout {
            return ResponseNfeCheckout(parcel)
        }

        override fun newArray(size: Int): Array<ResponseNfeCheckout?> {
            return arrayOfNulls(size)
        }
    }


}