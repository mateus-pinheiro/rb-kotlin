package br.com.packapps.rbcoletorandroid.repository

import br.com.packapps.rbcoletorandroid.ui.fragment.ConfirmationFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.FailFragment
import java.lang.Exception

object DataMatrix : FailFragment.OnFailFragmentListener{

    var failFragment : FailFragment? = null

    val ANVISA_NUMBER_MAX_LENGTH = 20

    val SERIAL_NUMBER_MAX_LENGTH = 20

    val BATCH_CODE_MAX_LENGTH = 20

    val DEFAULT_COMMON_SEPARATOR = "?"  //"}[{-}]{"

    val DEFAULT_ASCII_SEPARATOR_CODE = 29


    private fun getCodeLengthByPrefix(key : String): Int? {
        val mapCodeLengthByPrefix = hashMapOf<String, Int?>()
        mapCodeLengthByPrefix.put("00", 2)
        mapCodeLengthByPrefix.put("01", 2)
        mapCodeLengthByPrefix.put("02", 2)
        mapCodeLengthByPrefix.put("10", 2)
        mapCodeLengthByPrefix.put("11", 2)
        mapCodeLengthByPrefix.put("12", 2)
        mapCodeLengthByPrefix.put("13", 2)
        mapCodeLengthByPrefix.put("15", 2)
        mapCodeLengthByPrefix.put("17", 2)
        mapCodeLengthByPrefix.put("20", 2)
        mapCodeLengthByPrefix.put("21", 2)
        mapCodeLengthByPrefix.put("30", 2)
        mapCodeLengthByPrefix.put("37", 2)
        mapCodeLengthByPrefix.put("90", 2)
        mapCodeLengthByPrefix.put("91", 2)
        mapCodeLengthByPrefix.put("92", 2)
        mapCodeLengthByPrefix.put("93", 2)
        mapCodeLengthByPrefix.put("94", 2)
        mapCodeLengthByPrefix.put("95", 2)
        mapCodeLengthByPrefix.put("96", 2)
        mapCodeLengthByPrefix.put("97", 2)
        mapCodeLengthByPrefix.put("98", 2)
        mapCodeLengthByPrefix.put("99", 2)
        mapCodeLengthByPrefix.put("70", 4)
        mapCodeLengthByPrefix.put("80", 4)
        return mapCodeLengthByPrefix.get(key)
    }

    private fun getFixedFieldLength(key : String): Int? {
        val mapFixedFieldLength = hashMapOf<String, Int?>()
        mapFixedFieldLength.put("00", 20)
        mapFixedFieldLength.put("01", 16)
        mapFixedFieldLength.put("02", 16)
        mapFixedFieldLength.put("03", 16)
        mapFixedFieldLength.put("04", 18)
        mapFixedFieldLength.put("11", 8)
        mapFixedFieldLength.put("12", 8)
        mapFixedFieldLength.put("13", 8)
        mapFixedFieldLength.put("14", 8)
        mapFixedFieldLength.put("15", 8)
        mapFixedFieldLength.put("16", 8)
        mapFixedFieldLength.put("17", 8)
        mapFixedFieldLength.put("18", 8)
        mapFixedFieldLength.put("19", 8)
        mapFixedFieldLength.put("20", 4)
        mapFixedFieldLength.put("31", 10)
        mapFixedFieldLength.put("32", 10)
        mapFixedFieldLength.put("33", 10)
        mapFixedFieldLength.put("34", 10)
        mapFixedFieldLength.put("35", 10)
        mapFixedFieldLength.put("36", 10)
        mapFixedFieldLength.put("41", 16)

        return mapFixedFieldLength.get(key)
    }


    fun splitFirstCode(dataMatrixString : String): SplitFirstCode? {
        var codePrefix = dataMatrixString.substring(0, 2)
        var codeLength = getCodeLengthByPrefix(codePrefix)?: 3

        return SplitFirstCode(dataMatrixString.substring(0, codeLength), dataMatrixString.substring(codeLength))
    }

    fun getFieldLength (code : String?): Int? {
        var result : Int? = null
        if (code == null) {
            result = -1
        } else {
            var codePrefix = code.substring(0, 2)
            val fixedFieldLength = getFixedFieldLength(codePrefix)
            if (fixedFieldLength != null)
                result = fixedFieldLength - code.length
            else
                result = 0
        }
        return result
    }



    fun normalizeDataMatrix (dataMatrixString : String, commonSeparator : String): String {
        var dataMatrixStringAux =
            if (dataMatrixString.indexOf(commonSeparator) === 0){
                dataMatrixString.substring(commonSeparator.length)
            } else {
                dataMatrixString
            }
        return dataMatrixStringAux.trim()
    }


    fun parseDataMatrix (dataMatrixString : String, commonSeparator : String? = DEFAULT_COMMON_SEPARATOR): HashMap<String, String> {
        var commonSeparatorAux = if (commonSeparator != null){ DEFAULT_COMMON_SEPARATOR} else { commonSeparator}
        var dataMatrixStringAux = normalizeDataMatrix(dataMatrixString, commonSeparatorAux!!)
        var resultObject = hashMapOf<String, String>()
        getDataMatrixFields(dataMatrixStringAux, commonSeparatorAux, resultObject)
        return resultObject
    }



    fun getDataMatrixFields (dataMatrixString : String, commonSeparator : String, resultObject : HashMap<String, String>) {
        if (dataMatrixString.isEmpty()) {
            return
        }
        var codeSplited = splitFirstCode(dataMatrixString)
        var fieldLength = getFieldLength(codeSplited?.code)
        try {
            if (fieldLength === 0) {
                var splitedDataMatrix = codeSplited?.tail?.split(commonSeparator)
                resultObject.put(codeSplited!!.code!! , splitedDataMatrix!![0])
                this.getDataMatrixFields(codeSplited.tail.substring(splitedDataMatrix[0].length + commonSeparator.length), commonSeparator, resultObject);
            } else {
                resultObject.put(codeSplited!!.code, codeSplited.tail.substring(0, fieldLength!!))
                getDataMatrixFields(codeSplited.tail.substring(fieldLength), commonSeparator, resultObject)
            }
        } catch (ex : Exception) {
//            val dialog = FailFragment.newInstance("Busca de privilégios falhou", "Por favor verifique seus dados e tente novamente.")
//            dialog.isCancelable = false
//            dialog.show(supportFragmentManager, "")
        }
    }


    fun toRBIum (parsedDataMatrix : HashMap<String, String>): String {
        return parsedDataMatrix.get("713") + parsedDataMatrix.get("21") + parsedDataMatrix.get("17")!!.substring(2, 4) + parsedDataMatrix.get("17")!!.substring(0, 2) + parsedDataMatrix.get("10")
    }

    fun toRBIumObject (parsedDataMatrix : HashMap<String, String>): RBIumObject? {
        try {
            return RBIumObject(
                parsedDataMatrix.get("713")!!,
                parsedDataMatrix.get("21")!!,
                parsedDataMatrix.get("17")!!.substring(2, 4) + parsedDataMatrix.get("17")!!.substring(0, 2),
                toRBIum(parsedDataMatrix),
                parsedDataMatrix.get("10")!!
            )
        } catch (ex : Exception) {
            return null
            ex.printStackTrace()
        }

    }

    fun toRBDataMatrixString (anvisa : String, serial : String, date : String, batch : String, commonSeparator : String?): String {
        var sep = commonSeparator?:DEFAULT_COMMON_SEPARATOR
        return "713" + anvisa + sep + "21" + serial + sep + "17" + date + "10" + batch
    }

    fun isRBIum(parsedDataMatrix : HashMap<String, String>): Boolean {
        return parsedDataMatrix.get("713") != null &&
            parsedDataMatrix.get("21") != null &&
            parsedDataMatrix.get("17") != null &&
            parsedDataMatrix.get("10") != null
    }


    fun isInteger(numString : String): Boolean {
        var regex = "/^([0-9])$/i".toRegex()
        return regex.matches(numString)
    }

    fun isAlphanumeric (numString : String): Boolean {
        // Match one character
        var regex = "/[^0-9a-z]/i".toRegex()
        return regex.matches(numString)
    }


    fun validateAnvisaNumber (anvisaString : String): Boolean {
        return anvisaString.length > 0 && anvisaString.length <= ANVISA_NUMBER_MAX_LENGTH && isAlphanumeric(anvisaString)
    }

    fun validateSerialNumber (serialString : String): Boolean {
        return serialString.length > 0 && serialString.length <= this.SERIAL_NUMBER_MAX_LENGTH
//                && this.isAlphanumeric(serialString);
    }

    fun validateDateString(dateString : String): Boolean {
        try {
            var month = dateString.substring(0, 2).toInt(10)
            var year = dateString.substring(2, 4).toInt(10)
            // var day = window.parseInt(dateString.substring(4, 6), 10);
            return (dateString.length === 4) && (year >= 0 && year < 100) && (month > 0 && month <= 12)/* && (day >= 0 && day <= 31)*/
        } catch (e : Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun validateBatchCode(batchString : String): Boolean {
        return batchString.length > 0 && batchString.length <= this.BATCH_CODE_MAX_LENGTH && this.isAlphanumeric(batchString)
    }

    fun validateRBFields (anvisaString : String, serialString : String, dateString : String, batchString : String): Any {
        var result : Any = false
        if(!this.validateAnvisaNumber(anvisaString)){
            result = "VALIDATION.INVALID_IDENTIFIER"
        }else if(!this.validateSerialNumber(serialString)){
            result = "VALIDATION.INVALID_SERIAL"
        }else if(!this.validateBatchCode(batchString)){
            result = "VALIDATION.INVALID_BATCH"
        }else if(!this.validateDateString(dateString)){
            result = "VALIDATION.INVALID_DATE_FORMAT"
        }
        return result
    }


    private fun failFragment(title: String, message: String){
        failFragment = FailFragment.newInstance(title, message)
        failFragment!!.isCancelable = false
//        failFragment!!.show(supportFragmentManager, "")
    }

    override fun onActionFromFailFragment() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class SplitFirstCode(var code : String, var tail : String)

    class RBIumObject(var anvisa: String, var serial: String, var expiration: String, var IUM : String, var batchCode: String)


}