package br.com.packapps.rbcoletorandroid.model.body

class FinalizeCheckItemBody(var query : String){

    //json a ser enviado para consulta:
    // {"query":"{itemsById(id: \"317135286163660\") {id, aggrId, content { quantity batchObject {product {name company {id name }}}}}}" }

//    fun query() : String {
//        return "query:\n" +
//                "{\n" +
//                "itemsById(id: \"${barCode}\") {\n" +
//                "    id\n" +
//                "    aggrId\n" +
//                "    serialNumber\n" +
//                "    content {\n" +
//                "      batchObject {\n" +
//                "        batch\n" +
//                "        expirationDate\n" +
//                "        productionDate\n" +
//                "        product {\n" +
//                "          name\n" +
//                "          company {\n" +
//                "            id\n" +
//                "            name\n" +
//                "          }\n" +
//                "        }\n" +
//                "      }\n" +
//                "      quantity\n" +
//                "    }\n" +
//                "  }\n" +
//                "}\n"
//    }

}