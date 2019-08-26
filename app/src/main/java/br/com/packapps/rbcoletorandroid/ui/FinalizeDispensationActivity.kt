package br.com.packapps.rbcoletorandroid.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Log
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.model.body.FinalizeBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseFinalizeCheck
import br.com.packapps.rbcoletorandroid.ui.fragment.ConfirmationFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.FailFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.SuccessFragment
import br.com.packapps.rbcoletorandroid.util.Const
import br.com.packapps.rbcoletorandroid.viewmodel.FinalizeItemViewModel
import kotlinx.android.synthetic.main.activity_finalize_dispensation.*

class FinalizeDispensationActivity : BaseActivity(), ConfirmationFragment.OnConfirmationFragmentListener,
    SuccessFragment.OnSuccessFragmentListener, FailFragment.OnFailFragmentListener {

    val TAG = FinalizeDispensationActivity::class.java.simpleName

    lateinit var finalizeItemViewModel: FinalizeItemViewModel
    lateinit var finalizeItemBody: FinalizeBody
    var listTrackableIds: MutableList<String>? = mutableListOf()
    var failFragment: FailFragment? = null
    var confirmationFragment: ConfirmationFragment? = null
    var successFragment: SuccessFragment? = null

    var typePrefixCode: String = ""
    var listItemsChecked: MutableList<ResponseFinalizeCheck>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalize_dispensation)

        observerFinalizeItem()
        finalizeItemBody = FinalizeBody()
        val bundle = intent.extras
        if (bundle != null) {
            val bundle = intent.extras
            if (bundle != null) {
                listItemsChecked = ArrayList<ResponseFinalizeCheck>(bundle.getParcelableArrayList(Const.LIST_ITEM))

                listItemsChecked?.forEach {
                    try {
                        listTrackableIds!!.add(it.data!!.itemsById!![0].id!!)

                    } catch (ex: Exception) {
                        print(ex)
                    }
                }
            }
        }

        finalizeItemBody.code = 5001
        finalizeItemBody.latitude = null
        finalizeItemBody.longitude = null
        finalizeItemBody.timestamp = System.currentTimeMillis() / 1000
        finalizeItemBody.timezoneOffset = -3
        finalizeItemBody.trackableItemIds = listTrackableIds
        finalizeItemBody.finInfo = null
        finalizeItemBody.info = "Finalizado pelo coletor"

        finalize_dispensation_btn.setOnClickListener { view ->

            if (cid_et.text.isEmpty() || cps_et.text.isEmpty() || cpf_buyer_et.text.isEmpty() || cpf_patient_et.text.isEmpty()) {
                showDialogFail("Falha", "Por favor preencha todos os campos.")

            } else {
                showDialogConfirmation("Confirme", "Confirma o envio da finalização?")
                finalizeItemBody.finInfo = FinalizeBody.FinInfo()
                finalizeItemBody.finInfo?.cid = cid_et.text.toString()
                finalizeItemBody.finInfo?.cps = cps_et.text.toString()
                finalizeItemBody.finInfo?.cpfBuyer = cpf_buyer_et.text.toString()
                finalizeItemBody.finInfo?.cpfPatient = cpf_patient_et.text.toString()
                finalizeItemBody.finInfo?.prescriptionDate = System.currentTimeMillis() / 1000
            }
        }

    }

    private fun observerFinalizeItem() {
        finalizeItemViewModel =
            ViewModelProviders.of(this@FinalizeDispensationActivity).get(FinalizeItemViewModel::class.java)
        //Success
        finalizeItemViewModel.response.observe(this, Observer {
            Log.i(TAG, "response: ${it.toString()}")
            showProgress(false)

            showDialogSuccess("Sucesso", "A finalização foi realizada com sucesso!")
//            Toast.makeText(this, "Processo finalizado com sucesso!", Toast.LENGTH_SHORT).show()
//            Thread.sleep(1000)
//            var intent = Intent(this@FinalizeDispensationActivity, MenuActivity::class.java)
//            this.startActivity(intent)

        })
        //Error
        finalizeItemViewModel.error.observe(this, Observer {
            Log.e(TAG, it.toString())
            showProgress(false)
            showDialogFail("Falha", "Itens não enviados, tente novamente em alguns instantes!")
//            Snackbar.make(window.decorView.rootView, "Itens não enviados!", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()

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
        showProgress(true)
        finalizeItemViewModel.finalize(finalizeItemBody)
    }

    override fun onActionCancelFromConfirmationFragment() {
        confirmationFragment?.dismiss()
    }

    override fun onActionConfirmFromSuccessFragment() {
        var intent = Intent(this@FinalizeDispensationActivity, MenuActivity::class.java)
        this.startActivity(intent)
        finish()
    }

    override fun onActionFromFailFragment() {
        failFragment?.dismiss()
    }

}