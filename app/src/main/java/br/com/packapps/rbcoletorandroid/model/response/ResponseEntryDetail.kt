package br.com.packapps.rbcoletorandroid.model.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseEntryDetail() : Parcelable {

    @SerializedName("data")
    @Expose
    var data: Data? = null

    constructor(parcel: Parcel) : this() {
        data = parcel.readParcelable(Data::class.java.classLoader)
    }


    class Data() : Parcelable {

        @SerializedName("event")
        @Expose
        var event: Event? = null

        constructor(parcel: Parcel) : this() {
            event = parcel.readParcelable(Event::class.java.classLoader)
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeParcelable(event, flags)
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

    class Event() : Parcelable {

        @SerializedName("eventId")
        @Expose
        var eventId: String? = null

        @SerializedName("docs")
        @Expose
        var docs: MutableList<Docs>? = null

        @SerializedName("items")
        @Expose
        var items: MutableList<Items>? = null

        @SerializedName("code")
        @Expose
        var code: Code? = null

        @SerializedName("toCompany")
        @Expose
        //Enviado para
        var toCompany: ToCompany? = null

        @SerializedName("byCompany")
        @Expose
        //Transportado por
        var byCompany: ByCompany? = null

        @SerializedName("fromCompany")
        @Expose
        //Enviado por
        var fromCompany: FromCompany? = null

        @SerializedName("involvedBatches")
        @Expose
        var involvedBatches: MutableList<InvolvedBatches>? = null

        constructor(parcel: Parcel) : this() {
            eventId = parcel.readString()
            code = parcel.readParcelable(Code::class.java.classLoader)
            toCompany = parcel.readParcelable(ToCompany::class.java.classLoader)
            byCompany = parcel.readParcelable(ByCompany::class.java.classLoader)
            fromCompany = parcel.readParcelable(FromCompany::class.java.classLoader)
            involvedBatches = parcel.createTypedArrayList(InvolvedBatches)
            docs = parcel.createTypedArrayList(Docs)
            items = parcel.createTypedArrayList(Items)
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(eventId)
            parcel.writeParcelable(code, flags)
            parcel.writeParcelable(toCompany, flags)
            parcel.writeParcelable(byCompany, flags)
            parcel.writeParcelable(fromCompany, flags)
            parcel.writeTypedList(involvedBatches)
            parcel.writeTypedList(docs)
            parcel.writeTypedList(items)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Event> {
            override fun createFromParcel(parcel: Parcel): Event {
                return Event(parcel)
            }

            override fun newArray(size: Int): Array<Event?> {
                return arrayOfNulls(size)
            }
        }


    }

    class Docs() : Parcelable {


        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("type")
        @Expose
        var type: String? = null

        @SerializedName("key")
        @Expose
        var key: String? = null

        constructor(parcel: Parcel) : this() {
            id = parcel.readString()
            type = parcel.readString()
            key = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(type)
            parcel.writeString(key)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Docs> {
            override fun createFromParcel(parcel: Parcel): Docs {
                return Docs(parcel)
            }

            override fun newArray(size: Int): Array<Docs?> {
                return arrayOfNulls(size)
            }
        }

    }

    class Items() : Parcelable {


        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("aggrId")
        @Expose
        var aggrId: String? = null

        constructor(parcel: Parcel) : this() {
            id = parcel.readString()
            aggrId = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(aggrId)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Items> {
            override fun createFromParcel(parcel: Parcel): Items {
                return Items(parcel)
            }

            override fun newArray(size: Int): Array<Items?> {
                return arrayOfNulls(size)
            }
        }


    }

    class Code() : Parcelable {

        @SerializedName("code")
        @Expose
        var code: String? = null

        @SerializedName("id")
        @Expose
        var id: Long? = null

        constructor(parcel: Parcel) : this() {
            code = parcel.readString()
            id = parcel.readValue(Long::class.java.classLoader) as? Long
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(code)
            parcel.writeValue(id)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Code> {
            override fun createFromParcel(parcel: Parcel): Code {
                return Code(parcel)
            }

            override fun newArray(size: Int): Array<Code?> {
                return arrayOfNulls(size)
            }
        }

    }

    class ToCompany() : Parcelable {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("identifier")
        @Expose
        var identifier: String? = null

        @SerializedName("registryType")
        @Expose
        var registryType: RegistryType? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        constructor(parcel: Parcel) : this() {
            id = parcel.readValue(Int::class.java.classLoader) as? Int
            identifier = parcel.readString()
            registryType = parcel.readParcelable(RegistryType::class.java.classLoader)
            name = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(id)
            parcel.writeString(identifier)
            parcel.writeParcelable(registryType, flags)
            parcel.writeString(name)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ToCompany> {
            override fun createFromParcel(parcel: Parcel): ToCompany {
                return ToCompany(parcel)
            }

            override fun newArray(size: Int): Array<ToCompany?> {
                return arrayOfNulls(size)
            }
        }
    }

    class ByCompany() : Parcelable {

        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("identifier")
        @Expose
        var identifier: String? = null

        @SerializedName("registryType")
        @Expose
        var registryType: RegistryType? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        constructor(parcel: Parcel) : this() {
            id = parcel.readValue(Int::class.java.classLoader) as? Int
            identifier = parcel.readString()
            registryType = parcel.readParcelable(RegistryType::class.java.classLoader)
            name = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(id)
            parcel.writeString(identifier)
            parcel.writeParcelable(registryType, flags)
            parcel.writeString(name)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ByCompany> {
            override fun createFromParcel(parcel: Parcel): ByCompany {
                return ByCompany(parcel)
            }

            override fun newArray(size: Int): Array<ByCompany?> {
                return arrayOfNulls(size)
            }
        }

    }

    class FromCompany() : Parcelable {

        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("identifier")
        @Expose
        var identifier: String? = null

        @SerializedName("registryType")
        @Expose
        var registryType: RegistryType? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        constructor(parcel: Parcel) : this() {
            id = parcel.readValue(Int::class.java.classLoader) as? Int
            identifier = parcel.readString()
            registryType = parcel.readParcelable(RegistryType::class.java.classLoader)
            name = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(id)
            parcel.writeString(identifier)
            parcel.writeParcelable(registryType, flags)
            parcel.writeString(name)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<FromCompany> {
            override fun createFromParcel(parcel: Parcel): FromCompany {
                return FromCompany(parcel)
            }

            override fun newArray(size: Int): Array<FromCompany?> {
                return arrayOfNulls(size)
            }
        }

    }

    class RegistryType() : Parcelable {

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("id")
        @Expose
        var id: Int? = null

        constructor(parcel: Parcel) : this() {
            name = parcel.readString()
            id = parcel.readValue(Int::class.java.classLoader) as? Int
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(name)
            parcel.writeValue(id)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<RegistryType> {
            override fun createFromParcel(parcel: Parcel): RegistryType {
                return RegistryType(parcel)
            }

            override fun newArray(size: Int): Array<RegistryType?> {
                return arrayOfNulls(size)
            }
        }

    }

    class InvolvedBatches() : Parcelable {


        @SerializedName("batchObject")
        @Expose
        var batchObject: BatchObject? = null

        @SerializedName("quantity")
        @Expose
        var quantity: Int? = null

        constructor(parcel: Parcel) : this() {
            batchObject = parcel.readParcelable(BatchObject::class.java.classLoader)
            quantity = parcel.readValue(Int::class.java.classLoader) as? Int
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeParcelable(batchObject, flags)
            parcel.writeValue(quantity)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<InvolvedBatches> {
            override fun createFromParcel(parcel: Parcel): InvolvedBatches {
                return InvolvedBatches(parcel)
            }

            override fun newArray(size: Int): Array<InvolvedBatches?> {
                return arrayOfNulls(size)
            }
        }


    }

    class BatchObject() : Parcelable {

        @SerializedName("batch")
        @Expose
        var batch: String? = null

        @SerializedName("product")
        @Expose
        var product: Product? = null

        @SerializedName("presentation")
        @Expose
        var presentation: String? = null

        @SerializedName("identifier")
        @Expose
        var identifier: String? = null

        @SerializedName("presentationType")
        @Expose
        var presentationType: String? = null

        constructor(parcel: Parcel) : this() {
            batch = parcel.readString()
            product = parcel.readParcelable(Product::class.java.classLoader)
            presentation = parcel.readString()
            identifier = parcel.readString()
            presentationType = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(batch)
            parcel.writeParcelable(product, flags)
            parcel.writeString(presentation)
            parcel.writeString(identifier)
            parcel.writeString(presentationType)
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

    class Product() : Parcelable {
        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("idType")
        @Expose
        var idType: String? = null

        constructor(parcel: Parcel) : this() {
            name = parcel.readString()
            id = parcel.readString()
            idType = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(name)
            parcel.writeString(id)
            parcel.writeString(idType)
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(data, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResponseEntryDetail> {
        override fun createFromParcel(parcel: Parcel): ResponseEntryDetail {
            return ResponseEntryDetail(parcel)
        }

        override fun newArray(size: Int): Array<ResponseEntryDetail?> {
            return arrayOfNulls(size)
        }
    }

}


//{
//    "data": {
//    "event": {
//    "eventId": "61ac5010-022c-11e9-ac66-a375704f10e2",
//    "docs": [
//    {
//        "id": "000140014009",
//        "type": "NF",
//        "key": "35160625918684000136550090001400141104878121"
//    }
//    ],
//    "code": {
//    "code": "SALE",
//    "id": 4001
//},
//    "toCompany": {
//    "id": 1524,
//    "identifier": "81887838000302",
//    "registryType": {
//    "name": "CNPJ",
//    "id": 1
//},
//    "name": "PRODIET FARMACEUTICA S.A. - Profarma Specialty SP"
//},
//    "byCompany": {
//    "id": 24699,
//    "identifier": "33356706000140",
//    "registryType": {
//    "name": "CNPJ",
//    "id": 1
//},
//    "name": "TRANSPORTADORA NH"
//},
//    "fromCompany": {
//    "id": 6,
//    "identifier": "61230314000175",
//    "registryType": {
//    "name": "CNPJ",
//    "id": 1
//},
//    "name": "LIBBS FARMACÊUTICA LTDA - Barra Funda"
//},
//    "toFiscal": {
//    "id": 1524,
//    "identifier": "81887838000302",
//    "registryType": {
//    "name": "CNPJ",
//    "id": 1
//},
//    "name": "PRODIET FARMACEUTICA S.A. - Profarma Specialty SP"
//},
//    "fromFiscal": {
//    "id": 6,
//    "identifier": "61230314000175",
//    "registryType": {
//    "name": "CNPJ",
//    "id": 1
//},
//    "name": "LIBBS FARMACÊUTICA LTDA - Barra Funda"
//},
//    "involvedBatches": [
//    {
//        "batchObject": {
//        "batch": "SNCM1218",
//        "product": {
//        "name": "FAULBLASTINA 10 MG / 10 ML 5 FA OR",
//        "id": "1003301300021",
//        "idType": "ANVISA"
//    },
//        "presentation": "07896094208100",
//        "presentationType": "GTIN_QUATORZE"
//    },
//        "quantity": 2
//    }
//    ]
//}
//}
//}