package br.com.packapps.rbcoletorandroid.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.core.RepositoryUtils
import br.com.packapps.rbcoletorandroid.model.body.AggregationItemsBody
import br.com.packapps.rbcoletorandroid.model.body.EntryFinishActionBody
import br.com.packapps.rbcoletorandroid.model.body.IumItemsBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseEntryDetail
import br.com.packapps.rbcoletorandroid.model.response.ResponseIumItems
import br.com.packapps.rbcoletorandroid.repository.DataMatrix
import br.com.packapps.rbcoletorandroid.ui.adapter.DetailItemsFromNfAdapter
import br.com.packapps.rbcoletorandroid.ui.fragment.ConfirmationFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.FailFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.SuccessFragment
import br.com.packapps.rbcoletorandroid.util.Const
import br.com.packapps.rbcoletorandroid.viewmodel.AggregationListViewModel
import br.com.packapps.rbcoletorandroid.viewmodel.EntryFinishActionViewModel
import br.com.packapps.rbcoletorandroid.viewmodel.IumListViewModel

import kotlinx.android.synthetic.main.activity_entry_detail.*
import kotlinx.android.synthetic.main.content_entry_detail.*
import java.lang.Exception

class EntryDetailNFActivity : ScanActivity(), FailFragment.OnFailFragmentListener,
    ConfirmationFragment.OnConfirmationFragmentListener, SuccessFragment.OnSuccessFragmentListener {

    var responseEntryDetail: ResponseEntryDetail? = null
    var listInvolvedBatchesFromNf: MutableList<ResponseEntryDetail.InvolvedBatches>? = mutableListOf()
    var listItemsScannedMemory: MutableList<String> = mutableListOf()
    var listItemsIumMemory: MutableList<ResponseIumItems.ItemsByEventId>? = null
    var entryFinishActionBody: EntryFinishActionBody? = null
    var itemToSendList: MutableList<String>? = mutableListOf()
    var docsToSendList: MutableList<EntryFinishActionBody.Doc>? = mutableListOf()

    lateinit var iumListViewModel: IumListViewModel
    lateinit var iumsFromAggragationViewModel: AggregationListViewModel

    var failFragment: FailFragment? = null
    var confirmationFragment: ConfirmationFragment? = null
    var successFragment: SuccessFragment? = null

    lateinit var adapter: DetailItemsFromNfAdapter

    //finish action flow
    lateinit var entryActionFinishViewModel: EntryFinishActionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_detail)
        setSupportActionBar(toolbar)

        createObserverFromFinishAction()

        createObserverFromIumRequest()
        createObserverForGetIumsFromAgregationRequest()
        observerBarcodeScan()
        initRecycleView()
        mGetIntentFlow()


        fab.setOnClickListener { view ->


            //check options enables for next step send
            if (listItemsScannedMemory.size.equals(0)) {
                showDialogConfirmation(
                    "Confirme", "Você está optando em enviar essa NF sem checar os itens da mesma. " +
                            "Deseja concluir assim mesmo? Caso queira conferir os itens, clique não!"
                )

                return@setOnClickListener

            } else if (getQuantityItemsPending() > 0) {
                //check how must items pending still
                val quantity = getQuantityItemsPending()
                Snackbar.make(view, "Ainda faltam $quantity items para escanear!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

                return@setOnClickListener

            }

            showDialogConfirmation(
                "Confirme", "Será efetuada a entrada desta nota fiscal! Confirma?"
            )

        }
    }

    private fun createObserverFromFinishAction() {
        entryActionFinishViewModel = ViewModelProviders.of(this).get(EntryFinishActionViewModel::class.java)
        //Success
        entryActionFinishViewModel.response.observe(this, Observer {
            showProgress(false)
            showDialogSuccess("Sucesso","A entrada foi realizada com sucesso!")
        })
        //Failure
        entryActionFinishViewModel.error.observe(this, Observer {
            showProgress(false)
            if (it != null)
                showDialogFail("Itens não enviados", "Itens não foram enviados, por favor tente novamente." + it.messages[0])
            else
                showDialogFail("Itens não enviados", "Itens não foram enviados, por favor tente novamente.")

//            Snackbar.make(
//                window.decorView.rootView,
//                "Itens não foram enviados, por favor tente novamente." + it.messages[0],
//                Snackbar.LENGTH_LONG
//            )
//                .setAction("Action", null).show()
        })
    }

    private fun getQuantityItemsPending(): Int {
        var quantity = -1
        try {
            for (involvedBatch in listInvolvedBatchesFromNf!!) {
                if (quantity == -1)
                    quantity = 0

                quantity += involvedBatch.quantity!!
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return quantity
    }

    private fun createObserverFromIumRequest() {
        iumListViewModel = ViewModelProviders.of(this).get(IumListViewModel::class.java)
        //Success
        iumListViewModel.response.observe(this, Observer {
            showProgress(false)
            if (it != null)
                if (it.data != null)
                    if (it.data!!.itemsByEventId != null) {
                        listItemsIumMemory = it.data!!.itemsByEventId
                        return@Observer
                    }

            showDialogFail(
                "Erro IUM",
                "Houve um erro ao tentar recuperar os IUMs desta NF. Por favor volte e tente novamente."
            )

        })
        //Failure
        iumListViewModel.error.observe(this, Observer {
            showProgress(false)
            showDialogFail("Erro", it!!.messages[0])
        })
    }

    private fun createObserverForGetIumsFromAgregationRequest() {
        iumsFromAggragationViewModel = ViewModelProviders.of(this).get(AggregationListViewModel::class.java)
        //Success
        iumsFromAggragationViewModel.response.observe(this, Observer {
            showProgress(false)
            if (it != null)
                if (it.data != null)
                    if (it.data!!.itemsByParent != null) {
                        val listItemsByParent = it.data!!.itemsByParent!!
                        if (listItemsByParent.isNotEmpty()) {
                        //i was thinking to do something here, about the compare between items read and items from aggregation
                            for (item in listItemsByParent) {
                                if (item.aggrId != null) {
                                    iumsFromAggragationViewModel.getAggregationList(AggregationItemsBody("{itemsByParent(parentId: \"${item.id}\"){ id aggrId company { id name }}}"))
                                }

                                //Compare itemByParen with listOfIums
                                for (ium in listItemsIumMemory!!) {
                                    if (ium.id == item.id) {
                                        //Check if some item this Aggregation has scanned
                                        if (listItemsScannedMemory.contains(item.id)){
                                            showDialogFail("Você já escaneou um item IUM desta Agregação. ", "Continue escaneado os próximos.")
//                                            Toast.makeText(this, "Você já escaneou um item IUM desta Agregação. " +
//                                                    "Continue escaneado os próximos.", Toast.LENGTH_LONG).show()
                                            return@Observer
                                        }

                                        listItemsScannedMemory.add(item.id!!) //added item on list for check after
                                        managerDecrementIUMQuantity(ium, item.id)
                                    }
                                }
                            }
                        }



                        return@Observer
                    }

            showDialogFail(
                "Erro IUM",
                "Houve um erro ao tentar recuperar os IUMs desta NF. Por favor volte e tente novamente."
            )

        })
        //Failure
        iumsFromAggragationViewModel.error.observe(this, Observer {
            showProgress(false)
            showDialogFail("Erro", it!!.messages[0])
        })
    }

    private fun initRecycleView() {
        adapter = DetailItemsFromNfAdapter(this)
        rvDetailItemFromNF.layoutManager = LinearLayoutManager(this)
        rvDetailItemFromNF.adapter = adapter
    }

    private fun mGetIntentFlow() {
        showProgress(true)
        Handler().post {

            val bundle = intent.extras
            if (bundle != null) {
                responseEntryDetail = (bundle.getParcelable(Const.RESPONSE_ENTRY_DETAIL))

                tvBarcodeNf.text = responseEntryDetail!!.data!!.event!!.docs!![0].id
//                nfe_key_tv.text = responseEntryDetail!!.data!!.event!!.docs!![0].key

                by_name_tv.text = responseEntryDetail!!.data!!.event!!.byCompany!!.name
                to_name_tv.text = responseEntryDetail!!.data!!.event!!.toCompany!!.name
                from_name_tv.text = responseEntryDetail!!.data!!.event!!.fromCompany!!.name

                by_cnpj_tv.text = responseEntryDetail!!.data!!.event!!.byCompany!!.identifier
                to_cnpj_tv.text = responseEntryDetail!!.data!!.event!!.toCompany!!.identifier
                from_cnpj_tv.text = responseEntryDetail!!.data!!.event!!.fromCompany!!.identifier

                //More
                responseEntryDetail?.data?.event?.items!!.forEach {
                    if (it.aggrId == null) {
                        itemToSendList!!.add(it!!.id!!)
                    } else {
                        itemToSendList!!.add(it!!.aggrId!!)
                    }

                }

                responseEntryDetail?.data?.event?.docs!!.forEach {
                    var doc = EntryFinishActionBody.Doc()
                    doc.id = it.id
                    doc.key = it.key
                    doc.type = it.type
                    docsToSendList!!.add(doc)
                }

                entryFinishActionBody = EntryFinishActionBody()
                entryFinishActionBody!!.code = responseEntryDetail!!.data!!.event!!.code!!.id!!.minus(1000).toInt()
                entryFinishActionBody!!.checkoutEventId = responseEntryDetail!!.data!!.event!!.eventId
                entryFinishActionBody!!.trackableItemIds = itemToSendList!!.toList()
                entryFinishActionBody!!.timestamp = System.currentTimeMillis() / 1000
                entryFinishActionBody!!.timezoneOffset = -3
                entryFinishActionBody!!.docs = docsToSendList
                entryFinishActionBody!!.toUniversalCompanyId = responseEntryDetail!!.data!!.event!!.toCompany!!.identifier
                entryFinishActionBody!!.toCpRegType = responseEntryDetail!!.data!!.event!!.toCompany!!.registryType!!.id
                entryFinishActionBody!!.fromUniversalCompanyId = responseEntryDetail!!.data!!.event!!.fromCompany!!.identifier
                entryFinishActionBody!!.fromCpRegType = responseEntryDetail!!.data!!.event!!.fromCompany!!.registryType!!.id
                entryFinishActionBody!!.byUniversalCompanyId = responseEntryDetail!!.data!!.event!!.byCompany!!.identifier
                entryFinishActionBody!!.byCpRegType = responseEntryDetail!!.data!!.event!!.byCompany!!.registryType!!.id


            }


            try {
                listInvolvedBatchesFromNf = responseEntryDetail!!.data!!.event!!.involvedBatches
                runOnUiThread {
                    showProgress(false)
                    adapter.updateListItens(listInvolvedBatchesFromNf!!)
//                    SingletonApp.getInstance().paramBody = responseEntryDetail!!.data!!.event!!.eventId
                    iumListViewModel.getEntryDetailIum(
                        IumItemsBody(
                            "{\n" +
                                    "  itemsByEventId(eventId: \"${responseEntryDetail!!.data!!.event!!.eventId}\") {\n" +
                                    "    id\n" +
                                    "    aggrId\n" +
                                    "    presentation\n" +
                                    "    identifier\n" +
                                    "    batchObject {\n" +
                                    "       batch\n" +
                                    "    }\n" +
                                    "  }\n" +
                                    "}"
                        )
                    )
                }


            } catch (e: Exception) {
                e.printStackTrace()

                runOnUiThread {
                    showDialogFail("Erro", "Ouve um erro ao capturar detalhes desta NF. Volte e tente novamente!")
                }

            }

        }
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


    override fun observerBarcodeScan() {
        scanResponseViewModel.barcode.observe(this, Observer {

            val barcodeType = RepositoryUtils.getTypeBarcode(it.toString())
            var itemScanned = it

            if (barcodeType == 1) {
                itemScanned = DataMatrix.toRBIum(DataMatrix.parseDataMatrix(it!!))
            }


            //check if item was scanned?
            if (itemScanned in listItemsScannedMemory) {
                Snackbar.make(window.decorView.rootView, "Este item já foi escaneado!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                return@Observer
            }

            //check if item has into list objects from NF
            for (ium in listItemsIumMemory!!) {

                if (ium!!.aggrId != null) {
                    //AGGREGATION
                    if (ium.aggrId.equals(itemScanned, true)) {

                        //                        SingletonApp.getInstance().paramBody = ium.id
                        iumsFromAggragationViewModel.getAggregationList(AggregationItemsBody("{itemsByParent(parentId: \"${ium.id}\"){ id aggrId company { id name }}}"))

                        return@Observer


                    }

                } else {

                    //IUM
                    if (ium.id.equals(itemScanned, true)) {
                        managerDecrementIUMQuantity(ium, itemScanned)

                        return@Observer
                    }
                }
            }


            Snackbar.make(window.decorView.rootView, "Este item não foi encontrado na lista!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()


            //gone hints
//            tvTitle.visibility = View.GONE
//            lfAnim.visibility = View.GONE
        })
    }

    private fun managerDecrementIUMQuantity(
        ium: ResponseIumItems.ItemsByEventId,
        it: String?
    ) {
        val hasIUM = comparePresentationForUpdateList(ium)
        if (hasIUM) {
            listItemsScannedMemory.add(it!!)//added item on list for check after
            Snackbar.make(
                window.decorView.rootView,
                "Item checado com sucesso!",
                Snackbar.LENGTH_SHORT
            ).setAction("Action", null).show()

            //Scroll to position item

        } else {
            Snackbar.make(window.decorView.rootView, "Item não encontrado!", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()
        }
    }


    private fun comparePresentationForUpdateList(ium: ResponseIumItems.ItemsByEventId): Boolean {
        try {
            var i = 0
            for (involvedBatch in listInvolvedBatchesFromNf!!) {

                var batchObject = involvedBatch.batchObject

                if (!listItemsScannedMemory.contains(ium.id) || !listItemsScannedMemory.contains(ium.aggrId)) {
                    if (batchObject!!.presentation.equals(ium.presentation, true) && batchObject.identifier.equals(
                            ium.identifier,
                            true
                        ) && batchObject.batch!!.equals(ium.batchObject!!.batch, true)
                    ) {
                        var qnt = listInvolvedBatchesFromNf!!.get(i).quantity!!
                        var subtractedValue = qnt.minus(1) //decrement quantity for list of the adapter
                        listInvolvedBatchesFromNf!!.get(i).quantity = subtractedValue

                        adapter.notifyDataSetChanged()
                        rvDetailItemFromNF.scrollToPosition(i)

                        return true
                    }
                }
                i++
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return false
    }


    override fun onActionFromFailFragment() {
        failFragment?.dismiss()
    }

    override fun onActionConfirmFromConfirmationFragment() {
        //Send to next step
        showProgress(true, "Carregando")
        entryActionFinishViewModel.sendEntryFinishAction(entryFinishActionBody)

    }

    override fun onActionCancelFromConfirmationFragment() {
        confirmationFragment?.dismiss()
    }

    override fun onActionConfirmFromSuccessFragment() {
        var intent = Intent(this@EntryDetailNFActivity, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}
