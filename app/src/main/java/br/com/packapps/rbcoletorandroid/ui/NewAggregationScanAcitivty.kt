package br.com.packapps.rbcoletorandroid.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.core.RepositoryUtils
import br.com.packapps.rbcoletorandroid.ui.fragment.ConfirmationFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.FailFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.SuccessFragment
import br.com.packapps.rbcoletorandroid.util.Const
import br.com.packapps.rbcoletorandroid.viewmodel.AggregationListViewModel

import kotlinx.android.synthetic.main.activity_new_aggregation_scan_acitivty.*
import kotlinx.android.synthetic.main.content_new_aggregation_scan_acitivty.*

class NewAggregationScanAcitivty : ScanActivity(), ConfirmationFragment.OnConfirmationFragmentListener,
    FailFragment.OnFailFragmentListener, SuccessFragment.OnSuccessFragmentListener {

    var itemScannedCheckedOnApi = ""
    lateinit var iumsFromAggragationViewModel: AggregationListViewModel

    var failFragment: FailFragment? = null
    var confirmationFragment: ConfirmationFragment? = null
    var successFragment: SuccessFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_aggregation_scan_acitivty)
        setSupportActionBar(toolbar)

        val typeAggregationClicked = intent.getIntExtra(Const.TYPE_AGRREGATION, 100)
        observerBarcodeScan()



        fab.setOnClickListener { view ->
            if (itemScannedCheckedOnApi.isEmpty()) {

                Snackbar.make(view, "Primeiro escaneie uma Agregação", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

                return@setOnClickListener
            }

            var intent = Intent(this@NewAggregationScanAcitivty, NewAggregationAddedOrRemoveItemActivity::class.java)
            var bundle = Bundle()
            bundle.putInt(Const.TYPE_AGRREGATION, typeAggregationClicked)
            bundle.putString(
                Const.FROM_ACTIVITY,
                NewAggregationAddedOrRemoveItemActivity.FromActivity.NEW_AGGREGATION.label
            )
            bundle.putString(Const.NEW_AGGREGATION_SCANNED, itemScannedCheckedOnApi)

            intent.putExtras(bundle)
            startActivity(intent)


        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun observerBarcodeScan() {
        scanResponseViewModel.barcode.observe(this, Observer {
            itemScannedCheckedOnApi = it!!
            if (RepositoryUtils.getTypeBarcode(itemScannedCheckedOnApi) == 0) {
                tvBarcode.text = itemScannedCheckedOnApi
            } else
                showDialogFail("Incorreto", "O item lido deve ser uma agregação.")

        })
    }


    private fun showDialogFail(title: String, message: String) {
        failFragment = FailFragment.newInstance(title, message)
        failFragment!!.isCancelable = false
        failFragment!!.show(supportFragmentManager, "")
    }

    private fun showDialogConfirmation(title: String, message: String) {
        confirmationFragment = ConfirmationFragment.newInstance(title, message)
        confirmationFragment!!.isCancelable = false
        confirmationFragment!!.show(supportFragmentManager, "")
    }

    private fun showDialogSuccess(title: String, message: String) {
        successFragment = SuccessFragment.newInstance(title, message)
        successFragment!!.isCancelable = false
        successFragment!!.show(supportFragmentManager, "")
    }

    override fun onActionConfirmFromConfirmationFragment() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActionCancelFromConfirmationFragment() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActionFromFailFragment() {
        failFragment!!.dismiss()
    }

    override fun onActionConfirmFromSuccessFragment() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
