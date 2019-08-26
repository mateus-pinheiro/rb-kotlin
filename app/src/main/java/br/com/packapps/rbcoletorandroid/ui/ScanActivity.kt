package br.com.packapps.rbcoletorandroid.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanDevice
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.repository.DataMatrix
import br.com.packapps.rbcoletorandroid.viewmodel.ScanResponseViewModel
import kotlinx.android.synthetic.main.activity_test_scan.*
import java.lang.Exception

abstract class ScanActivity : BaseActivity() {

    internal var sm: ScanDevice? = null
    private var barcodeStr: String? = null
    private var barcodeString : String? = ""
    lateinit var scanResponseViewModel : ScanResponseViewModel


    private val mScanReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            val barocode = intent.getByteArrayExtra("barocode")
            val barocodelen = intent.getIntExtra("length", 0)
            val temp = intent.getByteExtra("barcodeType", 0.toByte())
            barcodeStr = String(barocode, 0, barocodelen)

            barcodeString = ""
            for (t in barocode) {
                if (t == 29.toByte())
                    barcodeString += "?"
                else
                    barcodeString += t.toChar()
            }

//            if (barcodeString!!.contains("?")) {
//                scanResponseViewModel.barcode.value =  DataMatrix.toRBIum(DataMatrix.parseDataMatrix(barcodeString!!))
//            } else {
//                scanResponseViewModel.barcode.value = barcodeString
//            }
            scanResponseViewModel.barcode.value = barcodeString

//            var teste = DataMatrix.toRBIum(DataMatrix.parseDataMatrix(barcodeString!!))

//            scanResponseViewModel.barcode.value = barcodeStr


            try { //To not crash on phone device
                sm!!.stopScan()
            }catch (e : Exception){
                e.printStackTrace()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_scan)

        scanResponseViewModel = ViewModelProviders.of(this).get(ScanResponseViewModel::class.java)

        //TODO commented just test
//        try { //To not crash on phone device
//            sm = ScanDevice()
//            sm!!.outScanMode = 0
//        }catch (e : Exception){
//            e.printStackTrace()
//        }


    }

    abstract fun observerBarcodeScan()



    override fun onPause() {
        super.onPause()
        if (sm != null) {
            sm!!.stopScan()
        }
        unregisterReceiver(mScanReceiver)
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction(SCAN_ACTION)
        registerReceiver(mScanReceiver, filter)
    }

    companion object {
        private val SCAN_ACTION = "scan.rcv.message"
    }
}
