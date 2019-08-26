package br.com.packapps.rbcoletorandroid.model.body

class EntryListOfNFsBody(var query: String)

object SupplierEntry {

    val queryValue = (
            EntryListOfNFsBody(
                "{\n" +
                        "              checkouts(, minDate: ${getDaysTimesMillis(20) / 1000}, \n" +
                        "                        maxDate: ${System.currentTimeMillis().plus(1000*60*60*24) / 1000}, limit: 10000, offset: 0) {\n" +
                        "                eventId\n" +
                        "                code {\n" +
                        "                  id\n" +
                        "                  code\n" +
                        "                  type\n" +
                        "                }\n" +
                        "                docs {\n" +
                        "                  id\n" +
                        "                  type\n" +
                        "                  key\n" +
                        "                }\n" +
                        "                items {\n" +
                        "                  id\n" +
                        "                  aggrId\n" +
                        "                }\n" +
                        "                date\n" +
                        "                insertionTs\n" +
                        "                affectedItems\n" +
                        "              }\n" +
                        "            }"
            )
            )

    val queryValueTeste = (
            EntryListOfNFsBody(
                "{\n" +
                        "              checkouts(, minDate: 1546221600, \n" +
                        "                        maxDate: 1547604000, limit: 25, offset: 0) {\n" +
                        "                eventId\n" +
                        "                code {\n" +
                        "                  id\n" +
                        "                  code\n" +
                        "                  type\n" +
                        "                }\n" +
                        "                docs {\n" +
                        "                  id\n" +
                        "                  type\n" +
                        "                  key\n" +
                        "                }\n" +
                        "                items {\n" +
                        "                  id\n" +
                        "                  aggrId\n" +
                        "                }\n" +
                        "                date\n" +
                        "                insertionTs\n" +
                        "                affectedItems\n" +
                        "              }\n" +
                        "            }"
            )
            )


    fun getDaysTimesMillis(days : Int) : Long {
        return System.currentTimeMillis() - ( (1000*60*60*24) * days)
    }

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