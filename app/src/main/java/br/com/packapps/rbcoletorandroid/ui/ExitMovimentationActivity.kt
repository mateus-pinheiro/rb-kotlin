package br.com.packapps.rbcoletorandroid.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.core.RepositoryUtils
import br.com.packapps.rbcoletorandroid.model.TypeItem
import br.com.packapps.rbcoletorandroid.model.WrapperListExit
import br.com.packapps.rbcoletorandroid.model.body.EntryFinishActionBody
import br.com.packapps.rbcoletorandroid.model.body.FinalizeCheckItemBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseExitMovement
import br.com.packapps.rbcoletorandroid.model.response.ResponseNfeCheckout
import br.com.packapps.rbcoletorandroid.repository.DataMatrix
import br.com.packapps.rbcoletorandroid.ui.adapter.ExitMovimentationAdapter
import br.com.packapps.rbcoletorandroid.ui.fragment.ConfirmationFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.FailFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.SuccessFragment
import br.com.packapps.rbcoletorandroid.util.Const
import br.com.packapps.rbcoletorandroid.viewmodel.EntryFinishActionViewModel
import br.com.packapps.rbcoletorandroid.viewmodel.FinalizeCheckItemViewModel
import kotlinx.android.synthetic.main.activity_exit_movimentation.*
import kotlinx.android.synthetic.main.content_exit_movimentation2.*
import java.lang.Exception

class ExitMovimentationActivity : ScanActivity(), FailFragment.OnFailFragmentListener,
    ConfirmationFragment.OnConfirmationFragmentListener, SuccessFragment.OnSuccessFragmentListener {

    var responseNfeCheckout: ResponseNfeCheckout? = null
    var stringScanned: String = ""
    var listItemsScanned: MutableList<String> = mutableListOf()
    var listItemsToSend: MutableList<String> = mutableListOf()
    var isDataMatrix: Boolean = false
    var entryFinishActionBody: EntryFinishActionBody? = null
    lateinit var adapter: ExitMovimentationAdapter
    lateinit var finalizeCheckItemViewModel: FinalizeCheckItemViewModel
    lateinit var entryFinishActionViewModel: EntryFinishActionViewModel

    var failFragment: FailFragment? = null
    var confirmationFragment: ConfirmationFragment? = null
    var successFragment: SuccessFragment? = null

    var listItemsExit: MutableList<WrapperListExit> = mutableListOf()
    var count = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exit_movimentation)
        setSupportActionBar(toolbar)

        initRecycleView()
        mGetIntentFlow()
        observerBarcodeScan()
        observerSendCheckout()


        observerGetItem()
        observerGetNfCount()

        fab.setOnClickListener { view ->

            if (getQuantityItemsPending() > 0) {
                showDialogFail("Falha", "Você precisa validar todos os itens desta nota fiscal.")
            } else {
                showDialogConfirmation("Confirmação", "Confirma o envio?")
            }

        }
    }

    override fun observerBarcodeScan() {
        scanResponseViewModel.barcode.observe(this, Observer {
            isDataMatrix = false
            val barcodeType = RepositoryUtils.getTypeBarcode(it.toString())
            var itemScanned = it

            if (barcodeType == 1) {
                itemScanned = DataMatrix.toRBIum(DataMatrix.parseDataMatrix(it!!))
                isDataMatrix = true
            }

            //check if item was scanned?
            if (itemScanned in listItemsScanned) {
                Snackbar.make(window.decorView.rootView, "Este item já foi escaneado!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                return@Observer
            } else if (getQuantityItemsPending() == 0) {
                Snackbar.make(window.decorView.rootView, "Todos os itens já foram escaneados!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                return@Observer
            }


            //When item going form bundle
            if (responseNfeCheckout != null) {
                entryFinishActionBody!!.checkNFContent = true
                listItemsScanned.add(itemScanned!!)
                finalizeCheckItemViewModel.check(FinalizeCheckItemBody("{itemsById(id: \"$itemScanned\") {id, aggrId, serialNumber, content { quantity batchObject { batch, expirationDate, productionDate, product {name company {id name }}}}}}"))

            } else {// Just Scan items to added
                entryFinishActionBody!!.checkNFContent = false
                if (!isDataMatrix)
                    listItemsExit.add(WrapperListExit(TypeItem.FROM_SCAN_AGGREGATION, null, it!!, null))
                else {
                    var dataMatrix = DataMatrix.toRBIumObject(DataMatrix.parseDataMatrix(it!!))
                    listItemsExit.add(WrapperListExit(TypeItem.FROM_SCAN_DATAMATRIX, null, null, dataMatrix))
                }
                count++
                tvCountItems.text = count.toString()
                listItemsToSend.add(itemScanned!!)
                listItemsScanned.add(itemScanned!!)

                adapter.updateListItens(listItemsExit)
            }
        })
    }

    private fun observerGetItem() {
        finalizeCheckItemViewModel = ViewModelProviders.of(this).get(FinalizeCheckItemViewModel::class.java)
        //Success
        finalizeCheckItemViewModel.response.observe(this, Observer {
            showProgress(false)
            if (it!!.data!!.itemsById!!.count() > 0) {
                stringScanned = it.data!!.itemsById!![0].id.toString()
//                iumsFromAggragationViewModel.getAggregationList(AggregationItemsBody("{itemsByParent(parentId: \"${it.data!!.itemsById!![0].id}\"){ id aggrId company { id name }}}"))
                var body =
                    "{\n nfCount: itemBatchResume(id: \"$stringScanned\") {\n batchObject {\n identifier\n identifierType: idType\n batch\n presentation\n presentationType\n }\n amount:quantity\n }\n item(id: \"$stringScanned\") {\n id\n aggrId\n serialNumber\n owner {\n" +
                            " id\n" +
                            "  identifier\n" +
                            " }\n     product {\n" +
                            " name\n" +
                            " }\n   company {\n                  id\n                name\n }\n                currentParents: parents {\n                  id\n                }\n                lastEvent {\n                  eventId\n                  code {\n                    type\n                  }\n                }\n              }\n            }"
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
                if (it.data!!.nfCount!!.count() > 0) {
                    compareToUpdateList(it)
                } else {
                    showDialogFail(
                        "Erro",
                        "Nenhum item encontrado. Por favor volte e tente novamente."
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

    private fun observerSendCheckout() {
        entryFinishActionViewModel = ViewModelProviders.of(this).get(EntryFinishActionViewModel::class.java)
        //Success
        entryFinishActionViewModel.response.observe(this, Observer {
            showProgress(false)
            showDialogSuccess("Sucesso", "O envio foi efetuado com êxito.")

        })
        //Failure
        entryFinishActionViewModel.error.observe(this, Observer {
            showProgress(false)
            showDialogFail(
                "Erro",
                "Houve um erro ao tentar enviar a saída. Por favor, tente novamente. ${it!!.messages}"
            )

        })
    }


    private fun compareToUpdateList(item: ResponseExitMovement) = try {
        var i = 0
        var hasItem = false
        for (product in responseNfeCheckout!!.products!!) {
            for (itemNfCount in item.data!!.nfCount!!) {
                if (product.batchCode!! == itemNfCount.batchObject!!.batch &&
                    removeZerosIfRequired(product.eANcomercial!!) == itemNfCount.batchObject.presentation!!.removeRange(0, 1)
                ) {
                    hasItem = true
                    var productName = product.name
                    //validate if data matrix from this product was read
                    if (product.dataMatrixReaded) {
                        if (item.data.item!!.aggrId == null) {

                            decrementItem(item, i)
                            adapter.notifyDataSetChanged()
                            rvExitMovimentation.scrollToPosition(i)

                        } else {
                            showDialogFail(
                                "Incorreto",
                                "Você já iniciou a leitura do produto $productName por data matrix, por favor continue pelo mesmo tipo. "
                            )
                        }
                    } else {

                        decrementItem(item, i)
                        adapter.notifyDataSetChanged()
                        rvExitMovimentation.scrollToPosition(i)
                    }
                }
            }

            i++
        }
        if (hasItem) {
        } else {
            showDialogFail(
                "Falha",
                "Item não encontrado na lista abaixo."
            )
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }

    override fun onActionFromFailFragment() {
        failFragment?.dismiss()
    }

    override fun onActionConfirmFromConfirmationFragment() {
        //Send to next step
        showProgress(true)
        entryFinishActionBody!!.trackableItemIds = listItemsToSend
        entryFinishActionBody!!.timestamp = System.currentTimeMillis() / 1000
        entryFinishActionBody!!.timezoneOffset = -3
        entryFinishActionBody!!.toCpRegType = 1
        entryFinishActionBody!!.toFiscalRegType = 1
        entryFinishActionBody!!.byCpRegType = 1
        entryFinishActionBody!!.fromCpRegType = 1
        entryFinishActionBody!!.fromFiscalRegType = 1
        entryFinishActionViewModel.sendEntryFinishAction(entryFinishActionBody)

    }

    override fun onActionCancelFromConfirmationFragment() {
        confirmationFragment?.dismiss()
    }

    override fun onActionConfirmFromSuccessFragment() {
        var intent = Intent(this@ExitMovimentationActivity, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun initRecycleView() {
        adapter = ExitMovimentationAdapter(this)
        rvExitMovimentation.layoutManager = LinearLayoutManager(this)
        rvExitMovimentation.adapter = adapter
    }

    private fun mGetIntentFlow() {
        Handler().post {
            val bundle = intent.extras
            if (bundle != null) {
                responseNfeCheckout = bundle.getParcelable(Const.CHECKOUT_ITEM)
                entryFinishActionBody = bundle.getParcelable(Const.CHECKOUT_WITHOUT_NF)

            }
            try {
                runOnUiThread {
                    if (responseNfeCheckout != null) {
                        responseNfeCheckout!!.products!!.forEach {
                            listItemsExit.add(WrapperListExit(TypeItem.FROM_BUNDLE, it, "", null))
                        }

                        adapter.updateListItens(listItemsExit)
                    } else {
                        flCountItems.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()

                runOnUiThread {
                    showDialogFail("Erro", "Ouve um erro ao capturar detalhes desta NF. Volte e tente novamente!")
                }
            }

        }

    }

    private fun getQuantityItemsPending(): Int {
        var quantity = -1
        try {
            for (product in responseNfeCheckout!!.products!!) {
                if (quantity == -1)
                    quantity = 0

                //remove comma if it has
                quantity += removeComma(product.quant!!)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return quantity
    }

    private fun decrementItem(item: ResponseExitMovement, i: Int) {
        if (removeComma(responseNfeCheckout!!.products!![i].quant!!) > 0) {
            var qnt = responseNfeCheckout!!.products!![i].quant
            var subtractedValue = removeComma(qnt!!).minus(item.data!!.nfCount!![0].amount!!)
            responseNfeCheckout!!.products!![i].quant = subtractedValue.toString()
            //check if is aggregation to add the right field
            if (item.data.item!!.aggrId != null)
                listItemsToSend.add(item.data.item.aggrId.toString())
            else
                listItemsToSend.add(item.data.item.id.toString())
            responseNfeCheckout!!.products!![i].dataMatrixReaded = isDataMatrix
            Snackbar.make(
                window.decorView.rootView,
                "Item checado com sucesso!",
                Snackbar.LENGTH_SHORT
            ).setAction("Action", null).show()

        } else {
            Snackbar.make(
                window.decorView.rootView,
                "Todos os itens desse produto já foram escaneados!",
                Snackbar.LENGTH_SHORT
            ).setAction("Action", null).show()
        }
    }

    private fun removeComma(qnt: String): Int {
        var index: Int
        var productQuantity: String
        if (qnt.contains(",")) {
            index = qnt.indexOf(",")
            productQuantity = qnt.substring(0, index)
        } else {
            productQuantity = qnt
        }
        var qntFormatted = productQuantity

        return qntFormatted.toInt()
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

    private fun removeZerosIfRequired(eanCommercial : String) : String{
        var isZero = eanCommercial.first()
        if (isZero.toString() == "0"){
            return eanCommercial.drop(1)
        }
        return eanCommercial
    }

}
