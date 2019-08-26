package br.com.packapps.rbcoletorandroid.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.core.SingletonApp
import br.com.packapps.rbcoletorandroid.model.body.EntryDetailBody
import br.com.packapps.rbcoletorandroid.model.body.SupplierEntry
import br.com.packapps.rbcoletorandroid.model.response.ResponseEntryListNFs
import br.com.packapps.rbcoletorandroid.ui.adapter.ListOfNFAdapter
import br.com.packapps.rbcoletorandroid.ui.fragment.FailFragment
import br.com.packapps.rbcoletorandroid.util.Const
import br.com.packapps.rbcoletorandroid.viewmodel.EntryDetailFromNFViewModel
import br.com.packapps.rbcoletorandroid.viewmodel.EntryListOfNFsViewModel

import kotlinx.android.synthetic.main.activity_entry_list_options.*
import kotlinx.android.synthetic.main.content_entry_list_options.*

class EntryListINFsFromUserActivity : ScanActivity(), FailFragment.OnFailFragmentListener, ListOfNFAdapter.OnNfItemAdapterListener {

    val TAG = EntryListINFsFromUserActivity::class.java.simpleName
//    lateinit var adapter : ItemsFromNFAdapter

    lateinit var entryListOfNFsViewModel: EntryListOfNFsViewModel
    var listNFs: MutableList<ResponseEntryListNFs.Checkout> = mutableListOf()
    var failFragment: FailFragment? = null
    lateinit var adapter: ListOfNFAdapter

    //detail NF
    lateinit var entryDetailNFViewModel: EntryDetailFromNFViewModel

    //listBarcode scanned just for memory
    var listBarcodeScanned: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_list_options)
        setSupportActionBar(toolbar)

        //init rv
        rvListItemsFromNf.layoutManager = LinearLayoutManager(this)
        adapter = ListOfNFAdapter(this, mutableListOf())
        rvListItemsFromNf.adapter = adapter

        observerBarcodeScan()
        createObserverFromEntryListNFs()
        createObserverFromDetailNF()

        showProgress(true)
        entryListOfNFsViewModel.getEntryListOfNFs(SupplierEntry.queryValue)

    }

    override fun onResume() {
        super.onResume()
        SingletonApp.getInstance().paramBody = null
    }

    private fun createObserverFromEntryListNFs() {
        entryListOfNFsViewModel = ViewModelProviders.of(this).get(EntryListOfNFsViewModel::class.java)
        //Success
        entryListOfNFsViewModel.response.observe(this, Observer {
            Log.i(TAG, "response: " + it.toString())
            showProgress(false)
            if (it != null) {
                listNFs = it.data!!.checkouts!!
                adapter.updateListItens(listNFs)

                if (it.data!!.checkouts!!.size == 0) {
                    showDialogFail(
                        "Lista de Notas Fiscais", "Nenhuma nota fiscal dísponivel."
                    )
                }
            }

        })
        //Failure
        entryListOfNFsViewModel.error.observe(this, Observer {
            Log.e(TAG, "error: " + it.toString())
            showProgress(false)
            showDialogFail(
                "Lista de Notas Fiscais", "Erro ao buscar lista de notas fiscais. Por favor, verifique sua " +
                        "conexão e Tente novamente. ${it!!.messages}"
            )

        })
    }


    private fun createObserverFromDetailNF() {
        entryDetailNFViewModel = ViewModelProviders.of(this).get(EntryDetailFromNFViewModel::class.java)
        //Success
        entryDetailNFViewModel.response.observe(this, Observer {
            showProgress(false)
            if (it != null) {
                //Call next View
                var bundle = Bundle()
                bundle.putParcelable(Const.RESPONSE_ENTRY_DETAIL, it)

                var intent = Intent(this@EntryListINFsFromUserActivity, EntryDetailNFActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }

        })
        //Failure
        entryDetailNFViewModel.error.observe(this, Observer {
            showProgress(false)
            showDialogFail("Nota Fiscal", "Detalhes de nota fiscal escaneada, não encontrados.")
        })

    }

    private fun showDialogFail(title: String, message: String) {
        failFragment = FailFragment.newInstance(title, message)
        failFragment!!.isCancelable = false
        failFragment!!.show(supportFragmentManager, "")
    }


    override fun observerBarcodeScan() {
        scanResponseViewModel.barcode.observe(this, Observer {
            if (it != null) {
                if (it.length != 44) {
                    showProgress(false)
                    Snackbar.make(window.decorView.rootView, "Este item não é uma NF!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    return@Observer
                }
                showProgress(true)

//                if (it in listBarcodeScanned) {
//                    Snackbar.make(window.decorView.rootView, "Esta NF já foi escaneada!", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show()
//                    return@Observer
//                }

                val eventID = hasItemScannedFromListNF(it!!)
                if (eventID != null) {
                    listBarcodeScanned.add(it)

//                    SingletonApp.getInstance().paramBody = eventID
//                    Thread.sleep(1000)
                    entryDetailNFViewModel.getEntryDetailFromNF(EntryDetailBody(
                        "{\n event (eventId: \"${eventID}\") " +
                                "{\n                eventId\n                " +
                                "items {\n id\n aggrId\n }\n " +
                                "docs {\n                  id\n                  type\n                  key\n                }\n                " +
                                "code {\n                  code\n                  id\n                }\n                toCompany {\n                  id\n                  identifier\n                  registryType {\n                    name\n                    id\n                  }\n                  name\n                }\n                byCompany {\n                  id\n                  identifier\n                  registryType {\n                    name\n                    id\n                  }\n                  name\n                }\n                fromCompany {\n                  id\n                  identifier\n                  registryType {\n                    name\n                    id\n                  }\n                  name\n                }\n" +
                                "involvedBatches {\n                  batchObject {\n                    batch\n                    product {\n                      name\n                      id\n                      idType\n                    }\n                    identifier\n        presentation\n                    presentationType\n                  }\n " +
                                "quantity\n                }\n              }\n            }"

                    ))

                } else {
                    showProgress(false)
                    Snackbar.make(
                        window.decorView.rootView,
                        "Este item não pertence a esta lista de NF!",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("Action", null).show()
                }
            }

            //gone hints
//            tvTitle.visibility = View.GONE
//            lfAnim.visibility = View.GONE
        })
    }

    override fun onfItemAdapterClick(checkout: ResponseEntryListNFs.Checkout) {
        showProgress(true)
        if (checkout != null) {
            entryDetailNFViewModel.getEntryDetailFromNF(EntryDetailBody(
                "{\n event (eventId: \"${checkout.eventId}\") " +
                        "{\n                eventId\n                " +
                        "items {\n id\n aggrId\n }\n " +
                        "docs {\n                  id\n                  type\n                  key\n                }\n                " +
                        "code {\n                  code\n                  id\n                }\n                toCompany {\n                  id\n                  identifier\n                  registryType {\n                    name\n                    id\n                  }\n                  name\n                }\n                byCompany {\n                  id\n                  identifier\n                  registryType {\n                    name\n                    id\n                  }\n                  name\n                }\n                fromCompany {\n                  id\n                  identifier\n                  registryType {\n                    name\n                    id\n                  }\n                  name\n                }\n" +
                        "involvedBatches {\n                  batchObject {\n                    batch\n                    product {\n                      name\n                      id\n                      idType\n                    }\n                    identifier\n        presentation\n                    presentationType\n                  }\n " +
                        "quantity\n                }\n              }\n            }"

            ))
        }
    }

    private fun hasItemScannedFromListNF(barcode: String): String? {
        for (item: ResponseEntryListNFs.Checkout in listNFs) {
            if (item.docs != null) {
                for (doc in item.docs!!) {
                    if (doc != null) {
                        val nfeNumberDigits: String = extract9DigitsFromNF(barcode) //9
                        val serialDigits: String = extractSerialDigitsFromNF(barcode) //3

                        val nfId = nfeNumberDigits + serialDigits
                        if (doc.id.equals(nfId, true))
                            return item.eventId
                    }
                }
            }
        }

        return null
    }

    private fun extractSerialDigitsFromNF(barcode: String): String {
        return barcode.substring(22, 25)
    }

    private fun extract9DigitsFromNF(barcode: String): String {
        return barcode.substring(25, 34)
    }


    override fun onActionFromFailFragment() {
        finish()
    }

}
