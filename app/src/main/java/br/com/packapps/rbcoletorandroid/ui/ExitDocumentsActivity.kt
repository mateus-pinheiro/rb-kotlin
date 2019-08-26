package br.com.packapps.rbcoletorandroid.ui

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.core.RepositoryUtils.hideKeyboard
import br.com.packapps.rbcoletorandroid.model.DocumentTypeItem
import br.com.packapps.rbcoletorandroid.model.body.EntryFinishActionBody
import br.com.packapps.rbcoletorandroid.model.response.ResponseNfeCheckout
import br.com.packapps.rbcoletorandroid.ui.adapter.ExitDocumentsAdapter
import br.com.packapps.rbcoletorandroid.ui.fragment.FailFragment
import br.com.packapps.rbcoletorandroid.util.Const

import kotlinx.android.synthetic.main.activity_exit_documents.*
import kotlinx.android.synthetic.main.content_exit_documents.*
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context
import android.view.inputmethod.InputMethodManager


class ExitDocumentsActivity : ScanActivity(), ExitDocumentsAdapter.OnDocumentAdpterListener,
    AdapterView.OnItemSelectedListener, FailFragment.OnFailFragmentListener {

    var itemSpinnerSelected: String? = null
    var prefixItemSelected: String? = null


    val TAG = ExitDocumentsActivity::class.java.simpleName
    lateinit var adapter: ExitDocumentsAdapter
    var listDocuements = mutableListOf<EntryFinishActionBody.Doc>()

    lateinit var failFragment: FailFragment
    var responseNfeCheckout: ResponseNfeCheckout? = null
    var entryFinishActionBody: EntryFinishActionBody? = null
    var itemSelectedCode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exit_documents)
        setSupportActionBar(toolbar)

        initSpinner()
        observerBarcodeScan()


        rvDocuments.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ExitDocumentsAdapter()
        adapter.onAttach(this)
        rvDocuments.adapter = adapter

        Handler().post {
            val bundle = intent.extras
            if (bundle != null) {
                responseNfeCheckout = bundle.getParcelable(Const.CHECKOUT_ITEM)
                entryFinishActionBody = bundle.getParcelable(Const.CHECKOUT_WITHOUT_NF)
            }

            runOnUiThread {
                if (responseNfeCheckout != null) {
                    onAdd(responseNfeCheckout?.id, "NF")
//                    etNFCode.clearFocus()
//                    hideKeyboard(this)
                }
            }
        }


        fab.setOnClickListener { view ->

            if (entryFinishActionBody!!.docs != null && entryFinishActionBody!!.docs!!.size > 0){
                var bundle = Bundle()
                bundle.putParcelable(Const.CHECKOUT_ITEM, responseNfeCheckout)
                bundle.putParcelable(Const.CHECKOUT_WITHOUT_NF, entryFinishActionBody)
                bundle.putInt(Const.ITEM_SELECTED, itemSelectedCode)
                var intent = Intent(this, ExitMovimentationActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            } else {
                showDialogFail("Falha","Adicione pelo menos um documento.")
            }


        }

        btnAddDocument.setOnClickListener { view ->
            onAdd(null, null)
        }
    }


    override fun observerBarcodeScan() {
        scanResponseViewModel.barcode.observe(this, Observer {
            if (it != null) {
                etNFCode.setText(it)
            }

        })

    }


    fun initSpinner() {
        spDocumentType.setOnItemSelectedListener(this)
        spDocumentType.onItemSelectedListener = this
        var arrayTypeBalancesAdapter =
            ArrayAdapter.createFromResource(this, R.array.document_type_items, android.R.layout.simple_spinner_item)
        arrayTypeBalancesAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1)
        spDocumentType.adapter = arrayTypeBalancesAdapter


    }

    fun onAdd(idByParameter : String?, typeByParameter : String?) {
        var doc = EntryFinishActionBody.Doc()
        if (idByParameter != null && typeByParameter != null) {
            if (typeByParameter == "NF")
                doc.key = idByParameter
            else
                doc.id = idByParameter
            doc.type = typeByParameter
            if (entryFinishActionBody!!.docs == null)
                entryFinishActionBody!!.docs = mutableListOf()
        } else {
            if (prefixItemSelected == "NF")
                doc.key = etNFCode.text.toString()
            else
                doc.id = etNFCode.text.toString()
            doc.type = prefixItemSelected
            if (entryFinishActionBody!!.docs == null)
                entryFinishActionBody!!.docs = mutableListOf()
        }
        entryFinishActionBody!!.docs!!.add(doc)
        listDocuements.add(doc)
        adapter.updateListDocuements(listDocuements)

        etNFCode.setText("")
    }


    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
        itemSpinnerSelected = spDocumentType.getItemAtPosition(position).toString()
        var arrayEnum = DocumentTypeItem.Items.values()
        prefixItemSelected = arrayEnum.get(position).prefix

    }

    override fun onAdapterDocumentClickIcDeleteItem(position: Int) {
        entryFinishActionBody!!.docs!!.removeAt(position)
        listDocuements.removeAt(position)
        adapter.updateListDocuements(listDocuements)
    }

    override fun onActionFromFailFragment() {
        failFragment.dismiss()
    }

    private fun showDialogFail(title: String, message: String) {
        failFragment = FailFragment.newInstance(title, message)
        failFragment.isCancelable = false
        failFragment.show(supportFragmentManager, "")
    }
}
