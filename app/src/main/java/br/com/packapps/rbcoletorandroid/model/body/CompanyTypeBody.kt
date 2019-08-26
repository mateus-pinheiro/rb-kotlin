package br.com.packapps.rbcoletorandroid.model.body

import br.com.packapps.rbcoletorandroid.core.SingletonApp

class CompanyTypeBody(var query : String)

object SupplierCompanyType{
    var queryValue = (
        CompanyTypeBody("{company(companyId: ${SingletonApp.getInstance().companyId}) {id identifier name typePrefixCode }}")
    )

}
//{
//
//    fun query() : String {
//        return "{\"query\":\"{company(companyId: ${companyId}) {typePrefixCode}}\"}"
//
////        "query\n" +
////                "{\n" +
////                "company(companyId: \"${companyId}\") {typePrefixCode}}"
//    }
//
//}