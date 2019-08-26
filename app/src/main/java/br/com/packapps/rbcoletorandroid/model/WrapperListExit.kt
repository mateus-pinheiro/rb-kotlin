package br.com.packapps.rbcoletorandroid.model

import br.com.packapps.rbcoletorandroid.model.response.ResponseNfeCheckout
import br.com.packapps.rbcoletorandroid.repository.DataMatrix

class WrapperListExit (
    var type : TypeItem,
    var itemFromBundle : ResponseNfeCheckout.Product?,
    var itemFromScanAggregation : String?,
    var itemFromScanIUM : DataMatrix.RBIumObject?
)

enum class TypeItem(val code : Int){
    FROM_BUNDLE(1),
    FROM_SCAN_DATAMATRIX(2),
    FROM_SCAN_AGGREGATION(3)
}
