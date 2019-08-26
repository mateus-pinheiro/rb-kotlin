package br.com.packapps.rbcoletorandroid.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.model.FinalizeReasonItem
import br.com.packapps.rbcoletorandroid.model.SupplierFinalizeReason
import br.com.packapps.rbcoletorandroid.model.body.FinalizeBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseFinalizeCheck
import br.com.packapps.rbcoletorandroid.ui.adapter.FinalizeReasonItemsAdapter
import br.com.packapps.rbcoletorandroid.ui.fragment.ConfirmationFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.FailFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.SuccessFragment
import br.com.packapps.rbcoletorandroid.util.Const
import br.com.packapps.rbcoletorandroid.viewmodel.FinalizeItemViewModel
import kotlinx.android.synthetic.main.activity_finalize_dispensation.*
import kotlinx.android.synthetic.main.activity_finalize_scan_items.*
import kotlinx.android.synthetic.main.content_finalize_reason.*
import java.sql.Timestamp

class FinalizeReasonActivity : BaseActivity(), ConfirmationFragment.OnConfirmationFragmentListener,
    SuccessFragment.OnSuccessFragmentListener,
    FinalizeReasonItemsAdapter.OnReasonAdapterListener,
    FailFragment.OnFailFragmentListener {

    val TAG = FinalizeReasonActivity::class.java.simpleName

    var typePrefixCode: String = ""
    var listItemsChecked: MutableList<ResponseFinalizeCheck>? = null
    var listTrackableIds: MutableList<String>? = mutableListOf()
    lateinit var adapter: FinalizeReasonItemsAdapter
    lateinit var finalizeItemViewModel: FinalizeItemViewModel
    lateinit var finalizeItemBody: FinalizeBody

    var failFragment: FailFragment? = null
    var confirmationFragment: ConfirmationFragment? = null
    var successFragment: SuccessFragment? = null

    var reasonClickedFromList: FinalizeReasonItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalize_reason)
//        setSupportActionBar(toolbar)

        val bundle = intent.extras
        if (bundle != null) {
            typePrefixCode = (bundle.getString(Const.TYPE_COMPANY).toString())
            listItemsChecked = ArrayList<ResponseFinalizeCheck>(bundle.getParcelableArrayList(Const.LIST_ITEM))
        }

        observerFinalizeItem()

        rvFinalizeReasonItem.layoutManager = LinearLayoutManager(this)
        if (!typePrefixCode.isEmpty()) {
            adapter = if (typePrefixCode == "IND" || typePrefixCode == "DIST") {
                FinalizeReasonItemsAdapter(this, SupplierFinalizeReason.items)
            } else {
                FinalizeReasonItemsAdapter(this, SupplierFinalizeReason.items1)
            }
        }
        rvFinalizeReasonItem.adapter = adapter

        listItemsChecked?.forEach {
            if (it!!.data!!.itemsById!![0].aggrId == null)
                listTrackableIds?.add(it!!.data!!.itemsById!![0].id!!)
            else
                listTrackableIds?.add(it!!.data!!.itemsById!![0].aggrId!!)
        }

//        to finish the finalize
        finalizeItemBody = FinalizeBody()
        finalizeItemBody.latitude = null
        finalizeItemBody.longitude = null
        finalizeItemBody.timestamp = System.currentTimeMillis() / 1000
        finalizeItemBody.timezoneOffset = -3
        finalizeItemBody.trackableItemIds = listTrackableIds
        finalizeItemBody.finInfo = null
        finalizeItemBody.info = "Finalizado pelo coletor"

        bundle.putParcelable(Const.FINALIZE_BODY, finalizeItemBody)

//                FinalizeBody(1017, listTrackableIds, Timestamp(System.currentTimeMillis()), -3, null, null, null)
//
//        finalizeItemViewModel.finalize(finalizeItemBody)

    }

    private fun observerFinalizeItem() {
        finalizeItemViewModel =
                ViewModelProviders.of(this@FinalizeReasonActivity).get(FinalizeItemViewModel::class.java)
        //Success
        finalizeItemViewModel.response.observe(this, Observer {
            Log.i(TAG, "response: ${it.toString()}")
            showProgress(false)
            showDialogSuccess("Sucesso", "A finalização foi realizada com sucesso!")

        })
        //Error
        finalizeItemViewModel.error.observe(this, Observer {
            Log.e(TAG, it.toString())
            showProgress(false)
            showDialogFail("Falha", "Itens não enviados, tente novamente em alguns instantes! \n" + it!!.messages[0])
//            Snackbar.make(window.decorView.rootView, "Itens não enviados!", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//            showDialogFail(
//                "Item não encontrado", "Erro ao buscar item escaneado. Por favor, verifique sua " +
//                        "conexão e tente novamente."
//            )

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

    override fun reasonAdapterClick(itemFromList: FinalizeReasonItem) {
        Log.i(TAG, "adapter click item position: ${itemFromList.toString()}")
        reasonClickedFromList = itemFromList
        finalizeItemBody.code = reasonClickedFromList?.code

        if (itemFromList.title.equals("Dispensação", true)) {

            var intent = Intent(this, FinalizeDispensationActivity::class.java)
//            intent.putExtras()
            val bundle = Bundle()
            bundle.putParcelableArrayList(
                Const.LIST_ITEM,
                ArrayList<ResponseFinalizeCheck>(listItemsChecked)
            )
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
        } else {
            //Open dialog confirmation
            ConfirmationFragment.newInstance(
                "Finalizar",
                "Daqui em diante o objeto será finalizado. Deseja realmente finalizar o envio?"
            )
                .show(supportFragmentManager, "CONFIRMATION")
        }
    }

    override fun onActionConfirmFromConfirmationFragment() {
        showProgress(true)
        finalizeItemViewModel.finalize(finalizeItemBody)
    }

    override fun onActionCancelFromConfirmationFragment() {
        confirmationFragment?.dismiss()
    }

    override fun onActionConfirmFromSuccessFragment() {
        var intent = Intent(this@FinalizeReasonActivity, MenuActivity::class.java)
        this.startActivity(intent)
        finish()
    }

    override fun onActionFromFailFragment() {
        failFragment?.dismiss()
    }


}
