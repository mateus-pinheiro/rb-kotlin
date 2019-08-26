package br.com.packapps.rbcoletorandroid.core

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import br.com.packapps.rbcoletorandroid.model.ItemScanned

object RepositoryUtils {

    fun getTypeBarcode(barcode : String) : Int{

//        if (barcode.length <= 23)
//            return ItemScanned.BarcodeType.BARCODE_NORMAL.code
//        else if (barcode.length > 23 && barcode.length < 44)
//            return ItemScanned.BarcodeType.BARCODE_DATAMATRIX.code
//        else if (barcode.length >= 44)
//            return ItemScanned.BarcodeType.BARCODE_NF.code
//        else
//            return 0

        if (barcode.length <= 23)
            return ItemScanned.BarcodeType.BARCODE_NORMAL.code
        else if (barcode.contains("?"))
            return ItemScanned.BarcodeType.BARCODE_DATAMATRIX.code
        else if (barcode.length >= 44)
            return ItemScanned.BarcodeType.BARCODE_NF.code
        else
            return 0

    }

    fun hideKeyboard(activity: Activity) {
        val inputManager = activity
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // check if no view has focus:
        val currentFocusedView = activity.currentFocus
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}
