package br.com.packapps.rbcoletorandroid.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.View
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.core.RepositoryUtils
import br.com.packapps.rbcoletorandroid.model.body.FinalizeCheckItemBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseExitMovement
import br.com.packapps.rbcoletorandroid.repository.DataMatrix
import br.com.packapps.rbcoletorandroid.ui.fragment.FailFragment
import br.com.packapps.rbcoletorandroid.viewmodel.FinalizeCheckItemViewModel

import kotlinx.android.synthetic.main.activity_search_barcode_traceable.*
import kotlinx.android.synthetic.main.content_search_barcode_traceable.*
import java.text.SimpleDateFormat
import java.util.*

class SearchBarcodeTraceableActivity : ScanActivity(), FailFragment.OnFailFragmentListener {

    lateinit var finalizeCheckItemViewModel: FinalizeCheckItemViewModel
    var failFragment: FailFragment? = null
    var stringScanned: String = ""

    val typeIum = "IUM"
    val typeAgregation = "Agregação"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_barcode_traceable)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        observerBarcodeScan()
        observerGetItem()
        observerGetNfCount()


    }


    override fun observerBarcodeScan() {
        scanResponseViewModel.barcode.observe(this, Observer {
            if (it != null) {
                var itemScannedType = RepositoryUtils.getTypeBarcode(it)
                var itemScanned: String
                if (itemScannedType == 1) {
                    itemScanned = DataMatrix.toRBIum(DataMatrix.parseDataMatrix(it))
                    tvItemType.text = typeIum
                } else {
                    itemScanned = it
                    tvItemType.text = typeAgregation
                }



                tvBarcode.text = itemScanned
                showProgress(true)
                finalizeCheckItemViewModel.check(FinalizeCheckItemBody("{itemsById(id: \"$itemScanned\") {id, aggrId, serialNumber, content { quantity batchObject { batch, expirationDate, productionDate, product {name company {id name }}}}}}"))
            }

        })
    }

    override fun onActionFromFailFragment() {
        finish()
    }

    private fun observerGetItem() {
        finalizeCheckItemViewModel = ViewModelProviders.of(this).get(FinalizeCheckItemViewModel::class.java)
        //Success
        finalizeCheckItemViewModel.response.observe(this, Observer {
            showProgress(false)
            if (it!!.data!!.itemsById!!.count() > 0) {
                stringScanned = it.data!!.itemsById!![0].id.toString()
                var body =
                    "{\n nfCount: itemBatchResume(id: \"$stringScanned\") {\n batchObject {\n identifier\n identifierType: idType\n batch\n presentation\n presentationType\n }\n amount:quantity\n }\n item(id: \"$stringScanned\") {\n id\n aggrId\n serialNumber\n owner { name identifier } " +
                            "batchObject {\n" +
                            " expirationDate\n productionDate\n" +
                            " }\n product {\n name\n }\n   company {\n                  id\n                name\n }\n                currentParents: parents {\n                  id\n                }\n                lastEvent {\n toCompany { name } " +
                            "eventId\n                  code {\n                    type\n                  }\n                }\n              }\n            }"
                finalizeCheckItemViewModel.checkNfCount(FinalizeCheckItemBody(body))
            } else {
                showDialogFail(
                    "Erro",
                    "Houve um erro ao tentar recuperar o item escaneado. Por favor volte e tente novamente."
                )
            }

        })
        //Failure
        finalizeCheckItemViewModel.error.observe(this, Observer {
            showProgress(false)
            showDialogFail("Erro", it!!.messages[0])

        })
    }

    private fun observerGetNfCount() {
        finalizeCheckItemViewModel = ViewModelProviders.of(this).get(FinalizeCheckItemViewModel::class.java)
        //Success
        finalizeCheckItemViewModel.responseNfCount.observe(this, Observer {
            showProgress(false)
            if (it != null) {
                if (it.data!!.nfCount!!.count() > 0 && it.data.item != null) {
                    setFields(it)
                } else {
                    showDialogFail(
                        "Erro",
                        "Nenhum resultado encontrado."
                    )
                }
            }

        })
        //Failure
        finalizeCheckItemViewModel.error.observe(this, Observer {
            showProgress(false)
            showDialogFail("Erro", it!!.messages[0])

        })
    }

    private fun showDialogFail(title: String, message: String) {
        failFragment = FailFragment.newInstance(title, message)
        failFragment!!.isCancelable = false
        failFragment!!.show(supportFragmentManager, "")
    }

    private fun setFields(it: ResponseExitMovement) {
        showFields(it.data!!.item!!.aggrId)

        tvEntityResponsible.text = it.data.item!!.company?.name
        tvEntityOwner.text = it.data.item.lastEvent?.toCompany?.name
        if (it.data.item.owner != null)
            tvEntityOwner.text = it.data.item.owner.name
        tvEventId.text = it.data.item.lastEvent?.eventId

        if (it.data.item.aggrId == null) {

            var startDate = Date(it.data.item.batchObjectItem?.productionDate!!)
            var format = SimpleDateFormat("dd/MM/yyyy")

            tvCreationDate.text = format.format(startDate).toString()
            tvProduct.text = it.data.item.product?.name
            tvBatchNumber.text = it.data.nfCount!![0].batchObject?.batch
            tvSerialNumber.text = it.data.item.serialNumber
            tvAnvisaNumber.text =
                it.data.nfCount[0].batchObject?.identifierType + " - " + it.data.nfCount[0].batchObject?.identifier
            tvGtinNumber.text =
                it.data.nfCount[0].batchObject?.presentationType + "\n" + it.data.nfCount[0].batchObject?.presentation
        }


    }

    private fun showFields(aggrId: String?) {

        if (aggrId.equals(null)) {
            textView30.visibility = View.VISIBLE
            textView29.visibility = View.VISIBLE
            textView28.visibility = View.VISIBLE
            textView27.visibility = View.VISIBLE
            textView26.visibility = View.VISIBLE
            textView.visibility = View.VISIBLE
            largeLabel2.visibility = View.VISIBLE
        }

        tvItemTypeLabel.visibility = View.VISIBLE
        textView25.visibility = View.VISIBLE
        textView23.visibility = View.VISIBLE


    }


}
