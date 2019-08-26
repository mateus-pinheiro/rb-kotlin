package br.com.packapps.rbcoletorandroid.model.body

import br.com.packapps.rbcoletorandroid.core.SingletonApp

class EntryDetailBody (var query : String) {

    object SupplierEntryDetail {

        var queryValue = (
                EntryDetailBody(
                    "{\n event (eventId: \"${SingletonApp.getInstance().paramBody}\") " +
                            "{\n                eventId\n                " +
                            "items {\n id\n aggrId\n }\n " +
                            "docs {\n                  id\n                  type\n                  key\n                }\n                " +
                            "code {\n                  code\n                  id\n                }\n                toCompany {\n                  id\n                  identifier\n                  registryType {\n                    name\n                    id\n                  }\n                  name\n                }\n                byCompany {\n                  id\n                  identifier\n                  registryType {\n                    name\n                    id\n                  }\n                  name\n                }\n                fromCompany {\n                  id\n                  identifier\n                  registryType {\n                    name\n                    id\n                  }\n                  name\n                }\n" +
                            "involvedBatches {\n                  batchObject {\n                    batch\n                    product {\n                      name\n                      id\n                      idType\n                    }\n                    identifier\n        presentation\n                    presentationType\n                  }\n " +
                            "quantity\n                }\n              }\n            }"

                    )
                )

        val queryValueTeste = (
                EntryDetailBody(
                    "{\n              event(eventId: \"6a84f830-102e-11e9-af88-81554dacbb65\") {\n                eventId\n                eventCompany {\n                  id\n                  name\n                  identifier\n                  registryType {\n                    id\n                    name\n                  }\n                }\n                code {\n                  id\n                  code\n                  type\n                }\n                user {\n                  name\n                  login\n                }\n                location {\n                  latitude\n                  longitude\n                }\n                sourceGroupId\n                docs {\n                  id\n                  type\n                  key\n                }\n                date\n                insertionTs\n                volumeTypeCode {\n                  value\n                }\n                toCompany {\n                  id\n                  name\n                  identifier\n                  registryType {\n                    id\n                    name\n                  }\n                }\n                toFiscal {\n                  id\n                  name\n                  identifier\n                  registryType {\n                    id\n                    name\n                  }\n                }\n                fromFiscal {\n                   id\n                  name\n                  identifier\n                  registryType {\n                    id\n                    name\n                  }\n                }\n                fromCompany {\n                  id\n                  name\n                  identifier\n                  registryType {\n                    id\n                    name\n                  }\n                }\n                byCompany {\n                  id\n                  name\n                  identifier\n                  registryType {\n                    id\n                    name\n                  }\n                }\n                affectedItems\n                info\n                cancelled\n                originType\n                format\n                links {\n                  anvisa\n                  epcis\n                }\n                finInfo {\n                  cps\n                  prescriptionDate\n                  cpfBuyer\n                  cpfPatient\n                  cid\n                }\n              }\n            }")
                )
    }
}


