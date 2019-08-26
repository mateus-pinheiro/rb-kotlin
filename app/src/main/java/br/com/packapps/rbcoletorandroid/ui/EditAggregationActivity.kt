package br.com.packapps.rbcoletorandroid.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.View
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.core.RepositoryUtils
import br.com.packapps.rbcoletorandroid.model.body.AggregationItemsBody
import br.com.packapps.rbcoletorandroid.model.body.FinalizeCheckItemBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseAggregationItems
import br.com.packapps.rbcoletorandroid.model.response.ResponseFinalizeCheck
import br.com.packapps.rbcoletorandroid.ui.fragment.ConfirmationFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.FailFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.SuccessFragment
import br.com.packapps.rbcoletorandroid.util.Const
import br.com.packapps.rbcoletorandroid.viewmodel.AggregationListViewModel
import br.com.packapps.rbcoletorandroid.viewmodel.FinalizeCheckItemViewModel

import kotlinx.android.synthetic.main.activity_edit_aggregation.*
import kotlinx.android.synthetic.main.content_edit_aggregation.*

class EditAggregationActivity : ScanActivity(), ConfirmationFragment.OnConfirmationFragmentListener,
    SuccessFragment.OnSuccessFragmentListener, FailFragment.OnFailFragmentListener {

    var aggregationScannedChecked = false
    var responseAggregationItems: ResponseAggregationItems? = null
    var stringScanned: String = ""
    lateinit var iumsFromAggragationViewModel: AggregationListViewModel
    lateinit var finalizeCheckItemViewModel: FinalizeCheckItemViewModel

    var failFragment: FailFragment? = null
    var confirmationFragment: ConfirmationFragment? = null
    var successFragment: SuccessFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_aggregation)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        createObserverForGetIumsFromAgregationRequest()
        observerGetItem()
        observerBarcodeScan()

        containerProgress.visibility = View.GONE
        tvMessageHelp.visibility = View.GONE

        fabAdded.setOnClickListener { view ->
            flowAfterClickOnFabs(view, "ADD")
        }
        fabRemove.setOnClickListener { view ->
            flowAfterClickOnFabs(view, "REMOVE")
        }
    }

    private fun flowAfterClickOnFabs(view: View, option: String) {
        var message = ""
        if (view.id == R.id.fabAdded) {
            message = "Para adicionar um item, escaneie primeiro uma Agregação"
        } else if (view.id == R.id.fabRemove) {
            message = "Para remover um item, escaneie primeiro uma Agregação"
        }

        if (!aggregationScannedChecked) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            return
        }

        var intent = Intent(this@EditAggregationActivity, NewAggregationAddedOrRemoveItemActivity::class.java)
        var bundle = Bundle()
        //            bundle.putString(Const.TYPE_AGRREGATION, typeAggregationClicked)
        bundle.putString(
            Const.FROM_ACTIVITY,
            NewAggregationAddedOrRemoveItemActivity.FromActivity.EDIT_AGGREGATION.label
        )
        bundle.putString(Const.EDIT_AGGREGATION_OPTION, option)
        bundle.putString(Const.EDIT_AGGREGATION_BARCODE_SCANNED, stringScanned)
        bundle.putParcelable(Const.EDIT_AGGREGATION_SCANNED, responseAggregationItems)

        intent.putExtras(bundle)
        startActivity(intent)
    }


    override fun observerBarcodeScan() {
        scanResponseViewModel.barcode.observe(this, Observer {
            if (it != null) {
                if (RepositoryUtils.getTypeBarcode(it!!) == 0) {
                    containerProgress.visibility = View.VISIBLE
                    finalizeCheckItemViewModel.check(FinalizeCheckItemBody("{itemsById(id: \"${it}\") {id, aggrId }}"))
                } else
                    showDialogFail("Incorreto", "O item lido deve ser uma agregação.")
            }

        })

    }

    private fun observerGetItem() {
        finalizeCheckItemViewModel = ViewModelProviders.of(this).get(FinalizeCheckItemViewModel::class.java)
        //Success
        finalizeCheckItemViewModel.response.observe(this, Observer {
            showProgress(false)
            if (it != null && it.data!!.itemsById!!.size > 0) {
                stringScanned = it.data!!.itemsById!![0].aggrId.toString()
                iumsFromAggragationViewModel.getAggregationList(AggregationItemsBody("{itemsByParent(parentId: \"${it.data!!.itemsById!![0].id}\"){ id aggrId parent { lastEvent { volumeTypeCode { code } }} company { id name }}}"))
            } else {
                showDialogFail(
                    "Erro",
                    "Houve um erro ao tentar recuperar os itens desta agregação. Por favor volte e tente novamente."
                )
                containerProgress.visibility = View.GONE
            }

        })
        //Failure
        finalizeCheckItemViewModel.error.observe(this, Observer {
            showProgress(false)
            showDialogFail("Erro", it!!.messages[0])
            containerProgress.visibility = View.GONE

        })
    }

    private fun createObserverForGetIumsFromAgregationRequest() {
        iumsFromAggragationViewModel = ViewModelProviders.of(this).get(AggregationListViewModel::class.java)
        //Success
        iumsFromAggragationViewModel.response.observe(this, Observer {
            showProgress(false)
            if (it != null) {
                responseAggregationItems = it
                tvBarcode.text = stringScanned
                aggregationScannedChecked = true

                containerProgress.visibility = View.GONE
                tvMessageHelp.visibility = View.VISIBLE
            } else {
                showDialogFail(
                    "Erro",
                    "Houve um erro ao tentar recuperar os itens desta agregação. Por favor volte e tente novamente."
                )
                containerProgress.visibility = View.GONE
            }


        })
        //Failure
        iumsFromAggragationViewModel.error.observe(this, Observer {
            showProgress(false)
            showDialogFail("Erro", it!!.messages[0])
            containerProgress.visibility = View.GONE
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

    override fun onActionConfirmFromSuccessFragment() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActionFromFailFragment() {
        failFragment!!.dismiss()
    }

}
