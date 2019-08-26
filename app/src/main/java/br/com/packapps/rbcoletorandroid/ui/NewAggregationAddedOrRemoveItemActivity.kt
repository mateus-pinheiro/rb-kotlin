package br.com.packapps.rbcoletorandroid.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.core.RepositoryUtils
import br.com.packapps.rbcoletorandroid.model.body.AggregationMessageBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseAggregationItems
import br.com.packapps.rbcoletorandroid.repository.DataMatrix
import br.com.packapps.rbcoletorandroid.ui.fragment.ConfirmationFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.FailFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.SuccessFragment
import br.com.packapps.rbcoletorandroid.util.Const
import br.com.packapps.rbcoletorandroid.viewmodel.AggregationMessageViewModel

import kotlinx.android.synthetic.main.activity_new_aggregation_added_or_remove_item.*
import kotlinx.android.synthetic.main.content_new_aggregation_added_or_remove_item.*

class NewAggregationAddedOrRemoveItemActivity : ScanActivity(), ConfirmationFragment.OnConfirmationFragmentListener,
    SuccessFragment.OnSuccessFragmentListener, FailFragment.OnFailFragmentListener {


    var typeAggregationClicked: Int? = null
    var newAggregationScanned: String? = null
    var editAggregationScanned: ResponseAggregationItems? = null
    var from: String? = null
    var editAggregationBarcodeScanned = ""
    var count = 0
    var editAggregationOption: String? = null
    var listOfItemsScanned: MutableList<String>? = mutableListOf()
    var listOfItemsAggregation: MutableList<String>? = mutableListOf()

    lateinit var aggregationMessageBody: AggregationMessageBody
    lateinit var aggregationMessageViewModel: AggregationMessageViewModel

    var failFragment: FailFragment? = null
    var confirmationFragment: ConfirmationFragment? = null
    var successFragment: SuccessFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_aggregation_added_or_remove_item)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        observerBarcodeScan()
        observerAggregationMessage()

        val bundle = intent.extras
        if (bundle != null) {
            from = bundle.getString(Const.FROM_ACTIVITY)
            editAggregationOption = bundle.getString(Const.EDIT_AGGREGATION_OPTION)
        }

        if (from!!.equals(FromActivity.NEW_AGGREGATION.label)) {
            typeAggregationClicked = bundle.getInt(Const.TYPE_AGRREGATION)
            newAggregationScanned = bundle.getString(Const.NEW_AGGREGATION_SCANNED)
            count = 0
            btButtonQuantityItems.text = count.toString()
        } else {
            editAggregationBarcodeScanned = bundle.getString(Const.EDIT_AGGREGATION_BARCODE_SCANNED)
            editAggregationScanned = bundle.getParcelable(Const.EDIT_AGGREGATION_SCANNED)
            count = editAggregationScanned!!.data!!.itemsByParent!!.count()
            editAggregationScanned!!.data!!.itemsByParent!!.forEach {
                listOfItemsAggregation!!.add(it.id!!)
            }
            btButtonQuantityItems.text = count.toString()
        }



        btButtonQuantityItems.setOnClickListener {
            Snackbar.make(it, "Escaneie os items desta Agregação", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        fab.setOnClickListener { view ->

            if (from == null) {
                Snackbar.make(view, "Desculpe ocorreu um erro.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                Handler().postDelayed({
                    finish()
                }, 1500)

                return@setOnClickListener
            }

            var message = ""
            if (from!!.equals(FromActivity.NEW_AGGREGATION.label)) {

                message = "Você escaneou todos os itens para esta nova agregação?"
                aggregationMessageBody = AggregationMessageBody(
                    2000,
                    newAggregationScanned,
                    listOfItemsAggregation,
                    typeAggregationClicked!!,
                    "",
                    true,
                    false,
                    System.currentTimeMillis() / 1000,
                    -3
                )

            } else if (from!!.equals(FromActivity.EDIT_AGGREGATION.label)) {
                message = "Você escaneou todos os itens que deseja adicionar ou remover desta agregação? Foram ${listOfItemsScanned?.size} item/itens."
                aggregationMessageBody = AggregationMessageBody(
                    2000,
                    editAggregationBarcodeScanned,
                    listOfItemsAggregation,
//                    typeAggregationClicked!!.toInt(),
                    100,
                    "",
                    false,
                    false,
                    System.currentTimeMillis() / 1000,
                    -3
                )

            }


            showDialogConfirmation(
                "CONFIRME",
                message
//                "Foi escaneado " + listOfItemsScanned!!.count() + " itens, confirma a alteração?"
            )


        }

    }

    override fun onActionConfirmFromConfirmationFragment() {
        showProgress(true)
        aggregationMessageViewModel.sendAggregation(aggregationMessageBody)
    }

    override fun onActionCancelFromConfirmationFragment() {
        //Do nothing
    }

    override fun onActionConfirmFromSuccessFragment() {
        var intent = Intent(this@NewAggregationAddedOrRemoveItemActivity, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActionFromFailFragment() {
        failFragment!!.dismiss()
    }

    override fun observerBarcodeScan() {
        scanResponseViewModel.barcode.observe(this, Observer {
            var itemScanned = it

            if (RepositoryUtils.getTypeBarcode(it!!) == 1) {
                itemScanned = DataMatrix.toRBIum(DataMatrix.parseDataMatrix(it))
            }




            listOfItemsScanned?.forEach { itemScannedFromList ->
                if (itemScannedFromList == itemScanned) {
                    Snackbar.make(window.decorView.rootView, "Este item já foi escaneado!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()

                    return@Observer
                }

            }

            if (editAggregationOption != null) {
                if (editAggregationOption == "ADD") {
                    listOfItemsAggregation?.forEach { itemAggregation ->
                        if (itemAggregation == itemScanned) {
                            Snackbar.make(
                                window.decorView.rootView,
                                "Este item já está na lista!",
                                Snackbar.LENGTH_LONG
                            )
                                .setAction("Action", null).show()

                            return@Observer
                        }

                    }
                    addItem(itemScanned!!)
                } else {
                    listOfItemsAggregation?.forEach { itemAggregation ->
                        if (itemAggregation == itemScanned) {
                            removeItem(itemScanned)

                            return@Observer
                        }

                    }

                    Snackbar.make(window.decorView.rootView, "Item não encontrado na lista!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                }
            } else {
                addItem(itemScanned!!)
            }

        })

    }

    private fun observerAggregationMessage() {
        aggregationMessageViewModel = ViewModelProviders.of(this).get(AggregationMessageViewModel::class.java)
        //Success
        aggregationMessageViewModel.response.observe(this, Observer {
            showProgress(false)
            showDialogSuccess("Sucesso", "A agregação foi realizada com sucesso!")
        })
        //Failure
        aggregationMessageViewModel.error.observe(this, Observer {
            showProgress(false)
            if (it != null)
                showDialogFail("Falhou", "Itens não foram enviados, por favor tente novamente. \n" + it!!.messages[0])
            else
                showDialogFail("Falhou", "Itens não foram enviados, por favor tente novamente.")
        })
    }

    fun addItem(it: String) {
        listOfItemsAggregation!!.add(it)
        listOfItemsScanned!!.add(it)
        count += 1
        btButtonQuantityItems.text = count.toString()

    }

    fun removeItem(it: String) {
        listOfItemsAggregation!!.remove(it)
        listOfItemsScanned!!.add(it)
        count -= 1
        btButtonQuantityItems.text = count.toString()
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

    private fun showDialogFail(title: String, message: String) {
        failFragment = FailFragment.newInstance(title, message)
        failFragment!!.isCancelable = false
        failFragment!!.show(supportFragmentManager, "")
    }


    enum class FromActivity(val label: String) {
        NEW_AGGREGATION("new_aggregation"),
        EDIT_AGGREGATION("edit_aggregation")
    }


}
