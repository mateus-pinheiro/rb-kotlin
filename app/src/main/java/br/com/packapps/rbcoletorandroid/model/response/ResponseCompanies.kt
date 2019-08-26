package br.com.packapps.rbcoletorandroid.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ResponseCompanies(

    @SerializedName("address")
    @Expose
    var address: Address? = null,
    @SerializedName("company")
    @Expose
    var company: Company? = null

)

class Address(

    @SerializedName("address_id")
    @Expose
    var addressId: Int? = null,
    @SerializedName("address")
    @Expose
    var address: String? = null,
    @SerializedName("county")
    @Expose
    var county: String? = null,
    @SerializedName("state")
    @Expose
    var state: String? = null,
    @SerializedName("country")
    @Expose
    var country: String? = null,
    @SerializedName("zipCode")
    @Expose
    var zipCode: String? = null

)


class Company(

    @SerializedName("deleted")
    @Expose
    var deleted: Int? = null,
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("info")
    @Expose
    var info: String? = null,
    @SerializedName("logoUrl")
    @Expose
    var logoUrl: String? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("prefixGs1")
    @Expose
    var prefixGs1: String? = null,
    @SerializedName("typeCode")
    @Expose
    var typeCode: Int? = null,
    @SerializedName("typePrefixCode")
    @Expose
    var typePrefixCode: String? = null,
    @SerializedName("typeCompany")
    @Expose
    var typeCompany: Int? = null,
    @SerializedName("universalId")
    @Expose
    var universalId: String? = null,
    @SerializedName("timezone")
    @Expose
    var timezone: String? = null,
    @SerializedName("isRBClient")
    @Expose
    var isRBClient: Boolean? = null,
    @SerializedName("address_id")
    @Expose
    var addressId: Int? = null,
    @SerializedName("address")
    @Expose
    var address: Address? = null,
    @SerializedName("economicGroup")
    @Expose
    var economicGroup: List<EconomicGroup>? = null,
    @SerializedName("contact")
    @Expose
    var contact: List<Any>? = null,
    @SerializedName("registryType")
    @Expose
    var registryType: RegistryType? = null,
    @SerializedName("parameters")
    @Expose
    var parameters: List<Any>? = null

)

class CompanyHasEconomicGroup(

    @SerializedName("company_id")
    @Expose
    var companyId: Int? = null,
    @SerializedName("economic_group_id")
    @Expose
    var economicGroupId: Int? = null

)

class EconomicGroup(

    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("default")
    @Expose
    var default: Int? = null,
    @SerializedName("active")
    @Expose
    var active: Int? = null,
    @SerializedName("company_has_economic_group")
    @Expose
    var companyHasEconomicGroup: CompanyHasEconomicGroup? = null

)

class RegistryType(

    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null

)



