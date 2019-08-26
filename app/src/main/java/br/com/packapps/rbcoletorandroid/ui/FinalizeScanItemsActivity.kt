package br.com.packapps.rbcoletorandroid.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.core.RepositoryUtils
import br.com.packapps.rbcoletorandroid.core.SingletonApp
import br.com.packapps.rbcoletorandroid.model.body.CompanyTypeBody
import br.com.packapps.rbcoletorandroid.model.body.FinalizeCheckItemBody
import br.com.packapps.rbcoletorandroid.model.response.Error
import br.com.packapps.rbcoletorandroid.model.response.ResponseCompanyType
import br.com.packapps.rbcoletorandroid.model.response.ResponseFinalizeCheck
import br.com.packapps.rbcoletorandroid.repository.DataMatrix
import br.com.packapps.rbcoletorandroid.ui.adapter.ScanItemsAdapter
import br.com.packapps.rbcoletorandroid.ui.fragment.FailFragment
import br.com.packapps.rbcoletorandroid.util.Const
import br.com.packapps.rbcoletorandroid.viewmodel.CompanyTypeViewModel
import br.com.packapps.rbcoletorandroid.viewmodel.FinalizeCheckItemViewModel

import kotlinx.android.synthetic.main.activity_finalize_scan_items.*
import kotlinx.android.synthetic.main.content_finalize_scan_items.*
import java.lang.Exception
import java.util.*

class FinalizeScanItemsActivity : ScanActivity(), FailFragment.OnFailFragmentListener {


    val TAG = FinalizeScanItemsActivity::class.java.simpleName

    var typePrefixCode: String = ""
    var listItemsWrapperFinalizeItem: MutableList<WrapperFinalizeItem> = mutableListOf<WrapperFinalizeItem>()
    var listItemOnlyScanned: MutableList<String> = mutableListOf()
    var listItemsCheckedWithItems = mutableListOf<ResponseFinalizeCheck>()
    var finalizeCheckViewModel: FinalizeCheckItemViewModel? = null
    lateinit var failFragment: FailFragment

    var iumObject: DataMatrix.RBIumObject? = null
    lateinit var adapter: ScanItemsAdapter
    lateinit var companyTypeViewModel: CompanyTypeViewModel
    lateinit var wrapperFinalizeItem: WrapperFinalizeItem

    var countItemCheckedSuccess = 0
    var countItemScannned = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalize_scan_items)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rvItemsScan.layoutManager = LinearLayoutManager(this)
        adapter = ScanItemsAdapter(this)
        rvItemsScan.adapter = adapter

        //init viewModel FinalizeItem
        finalizeCheckViewModel = ViewModelProviders.of(this).get(FinalizeCheckItemViewModel::class.java)


        //ViewModel observer
        observerBarcodeScan()


        //Implement observer finalize item check
//        showProgress(true)
        observerFinalizeCheckItem()

        observerCompanyType()

        fab.setOnClickListener {

            if (countItemScannned > countItemCheckedSuccess) {
                Toast.makeText(
                    this@FinalizeScanItemsActivity,
                    "Por favor aguarde, ainda existem ${countItemScannned - countItemCheckedSuccess} em processamento.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }


            listItemsWrapperFinalizeItem.forEach { itItem ->
                if (itItem.responseItemCheck!!.data?.itemsById != null) {
                    if (itItem.responseItemCheck!!.data!!.itemsById!!.size > 0) {
                        listItemsCheckedWithItems.add(itItem.responseItemCheck!!)
                    }
                }
            }

            if (listItemsWrapperFinalizeItem.size == 0 || listItemsCheckedWithItems.size == 0) {
                Snackbar.make(it, "Escaneie ao menos um item de sua posse.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show()

                return@setOnClickListener
            }


            showProgress(true)

            //call
            companyTypeViewModel.getCompanyTypeFrom((CompanyTypeBody("{company(companyId: ${SingletonApp.getInstance().companyId}) {id identifier name typePrefixCode }}")))


        }


    }

    private var barcodeReadLast: String = ""

    var proccess = 1 //loading = 0 | synchronized = 1
    override fun observerBarcodeScan() {
        if (proccess == 0)
            proccessWithLoading()
        else if (proccess == 1)
            proccessWithSynchronized()
    }

    private fun proccessWithSynchronized() {
        scanResponseViewModel.barcode.observe(this, Observer {

            Handler().post {
                countItemScannned++


                val barcodeType = RepositoryUtils.getTypeBarcode(it.toString())

                wrapperFinalizeItem = WrapperFinalizeItem()
                wrapperFinalizeItem.type = barcodeType
                wrapperFinalizeItem.barcode = it
                wrapperFinalizeItem.responseItemCheck = ResponseFinalizeCheck()


                if (barcodeType == 1) {

                    iumObject = DataMatrix.toRBIumObject(DataMatrix.parseDataMatrix(it!!))
                    wrapperFinalizeItem.dataMatrixObject = iumObject

                } else if (barcodeType == 0) {

                    wrapperFinalizeItem.responseItemCheck!!.barCode = it
                    //                adapter.updateListItemsChecked(listItemsChecked)
                } else if (barcodeType == 2) {

//                    runOnUiThread {
                    Snackbar.make(
                        window.decorView.rootView,
                        "Não pode ser consultado pela nota fiscal! Tente pelo Data Matrix.",
                        Snackbar.LENGTH_LONG
                    ).setAction("Action", null).show()
                    showProgress(false)
//                    }

                    return@post
                }

                listItemOnlyScanned.forEach {
                    if (it == wrapperFinalizeItem.barcode) {

                        //Access UI thread
//                        runOnUiThread {
                        Snackbar.make(
                            window.decorView.rootView,
                            "Este item já foi escaneado!",
                            Snackbar.LENGTH_LONG
                        ).setAction("Action", null).show()
                        showProgress(false)
//                        }
                        countItemScannned--

                        return@post
                    }

                }
                listItemOnlyScanned.add(it!!)

//                //Add item with just barcode
//                wrapperFinalizeItem.responseItemCheck!!.justBarcode = true
//                listItemsWrapperFinalizeItem.add(wrapperFinalizeItem)
//                runOnUiThread {
//                    adapter.updateListItemsChecked(listItemsWrapperFinalizeItem)
//                }

                var finalizeCheckItemString = ""

                finalizeCheckItemString = if (wrapperFinalizeItem.dataMatrixObject != null) {
                    "{itemsById(id: \"${wrapperFinalizeItem.dataMatrixObject!!.IUM}\") {id, aggrId, serialNumber, content { quantity batchObject { batch, expirationDate, productionDate, product {name company {id name }}}}}}"
                } else {
                    "{itemsById(id: \"${wrapperFinalizeItem.responseItemCheck!!.barCode}\") {id, aggrId, serialNumber, content { quantity batchObject { batch, expirationDate, productionDate, product {name company {id name }}}}}}"
                }
                finalizeCheckViewModel!!.check(FinalizeCheckItemBody(finalizeCheckItemString))

            }


            //gone hints
            tvTitle.visibility = View.GONE
            lfAnim.visibility = View.GONE
        })
    }

    private fun proccessWithLoading() {
        scanResponseViewModel.barcode.observe(this, Observer {

            Handler().post {

                if (progress?.isVisible == true) {
                    runOnUiThread {
                        Toast.makeText(this, "Aguarde. Executando última leitura.", Toast.LENGTH_SHORT).show()
                    }
                    return@post
                }

                runOnUiThread {
                    showProgress(true)
                }


                barcodeReadLast = it!!

                val barcodeType = RepositoryUtils.getTypeBarcode(it.toString())

                wrapperFinalizeItem = WrapperFinalizeItem()
                wrapperFinalizeItem.type = barcodeType
                wrapperFinalizeItem.barcode = it
                wrapperFinalizeItem.responseItemCheck = ResponseFinalizeCheck()


                listItemsWrapperFinalizeItem.forEach {
                    if (it.barcode == wrapperFinalizeItem.barcode) {

                        //Access UI thread
                        runOnUiThread {
                            Snackbar.make(
                                window.decorView.rootView,
                                "Este item já foi escaneado!",
                                Snackbar.LENGTH_LONG
                            )
                                .setAction("Action", null).show()
                            showProgress(false)
                        }

                        return@post
                    }
                }


                if (barcodeType == 1) {

                    iumObject = DataMatrix.toRBIumObject(DataMatrix.parseDataMatrix(it!!))
                    wrapperFinalizeItem.dataMatrixObject = iumObject

                } else if (barcodeType == 0) {

                    wrapperFinalizeItem.responseItemCheck!!.barCode = it
                    //                adapter.updateListItemsChecked(listItemsChecked)
                } else if (barcodeType == 2) {

                    runOnUiThread {
                        Snackbar.make(
                            window.decorView.rootView,
                            "Não pode ser consultado pela nota fiscal! Tente pelo Data Matrix.",
                            Snackbar.LENGTH_LONG
                        ).setAction("Action", null).show()
                        showProgress(false)
                    }

                    return@post
                }

                //Add item with just barcode
//                wrapperFinalizeItem.responseItemCheck!!.justBarcode = true
//                listItemsWrapperFinalizeItem.add(wrapperFinalizeItem)

                //                adapter.updateListItemsChecked(listItemsWrapperFinalizeItem)


                var finalizeCheckItemString = ""

                finalizeCheckItemString = if (wrapperFinalizeItem.dataMatrixObject != null) {
                    "{itemsById(id: \"${wrapperFinalizeItem.dataMatrixObject!!.IUM}\") {id, aggrId, serialNumber, content { quantity batchObject { batch, expirationDate, productionDate, product {name company {id name }}}}}}"
                } else {
                    "{itemsById(id: \"${wrapperFinalizeItem.responseItemCheck!!.barCode}\") {id, aggrId, serialNumber, content { quantity batchObject { batch, expirationDate, productionDate, product {name company {id name }}}}}}"
                }
                finalizeCheckViewModel!!.check(FinalizeCheckItemBody(finalizeCheckItemString))

            }


            //gone hints
            tvTitle.visibility = View.GONE
            lfAnim.visibility = View.GONE
        })
    }


    private fun observerFinalizeCheckItem() {
        //Success
        finalizeCheckViewModel!!.response.observe(this, Observer {
            Log.i(TAG, "response: ${it.toString()}")

            Handler().post {

                try {
//                    var wrapperFinalizeItem: WrapperFinalizeItem
//                    wrapperFinalizeItem = WrapperFinalizeItem()

                    //remove last item added with flag justBarcode = true
//                    if (listItemsWrapperFinalizeItem.size > 0) {
//                        var i = 0
//                        var positionTodelete : Int? = null
//                        for (item in listItemsWrapperFinalizeItem){
//                            if (item.barcode.equals(it?.barCode)){
//                                positionTodelete = i
//                            }
//                            i++
//                        }
//                        if (positionTodelete != null){
//                            listItemsWrapperFinalizeItem.removeAt(positionTodelete)
//                            runOnUiThread {
//                                adapter.updateListItemsChecked(listItemsWrapperFinalizeItem)
//                            }
//                        }
//                    }

                    //Continue building item
                    wrapperFinalizeItem.responseItemCheck!!.data = it?.data
                    wrapperFinalizeItem.responseItemCheck!!.justBarcode = false
                    if (it?.data?.itemsById?.size == 0)
                        wrapperFinalizeItem.itemNotFound = true
                    listItemsWrapperFinalizeItem.add(wrapperFinalizeItem)


                    runOnUiThread {
                        runOnUiThread {
                            adapter.updateListItemsChecked(listItemsWrapperFinalizeItem)
                        }
                        rvItemsScan.scrollToPosition(listItemsWrapperFinalizeItem.size - 1)
                        countItemCheckedSuccess++
                        tvCountItems.text = countItemCheckedSuccess.toString()
                    }

                } catch (e: Exception) {
                    countItemScannned--
                    managerErrorItemNotFound()
                }

                runOnUiThread {
                    rvItemsScan.scrollToPosition(listItemsWrapperFinalizeItem.size - 1)
                    showProgress(false)
                }

            }


        })
        //Error
        finalizeCheckViewModel!!.error.observe(this, Observer {
            Log.e(TAG, it.toString())
            showProgress(false)
            if (it!!.code == 401) {
                showDialogFail("Falha no login", "Seu login não é mais válido, logue-se novamente.")
                return@Observer
            }
//            managerErrorItemNotFound()
            countItemScannned--

            showDialogFail(
                "Item não encontrado", "Erro ao buscar item escaneado. \n" + it.messages[0]
            )

        })
    }

    private fun managerErrorItemNotFound() {
        //Error
//        listItemsWrapperFinalizeItem.forEach {
//            if (it.barcode.equals(barcodeReadLast)) {
//                var itAux = it
//                itAux.itemNotFound = true
//                //remove item from list
//                listItemsWrapperFinalizeItem.removeAt(listItemsWrapperFinalizeItem.size)
//                listItemsWrapperFinalizeItem.add(itAux)
//
//                adapter.updateListItemsChecked(listItemsWrapperFinalizeItem)
//
//            }
//        }
//        rvItemsScan.scrollToPosition(listItemsWrapperFinalizeItem.size - 1)

        Toast.makeText(this, "Item " + wrapperFinalizeItem.barcode + " não está em sua posse ", Toast.LENGTH_SHORT)
            .show()
    }


//    @Synchronized
//    private fun addItemOnListSynchronized(wrapperFinalizeItem: WrapperFinalizeItem){
//        listItemsWrapperFinalizeItem.add(wrapperFinalizeItem)
//    }


    private fun observerCompanyType() {
        companyTypeViewModel = ViewModelProviders.of(this).get(CompanyTypeViewModel::class.java)
        //Success
        companyTypeViewModel.response.observe(this, Observer<ResponseCompanyType> {
            Log.i(TAG, it.toString())
            showProgress(false)
            if (it != null) {
                typePrefixCode = it!!.data?.company?.typePrefixCode.toString()
            }
            if (typePrefixCode.isNotEmpty()) {

                val activityOptions =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this,
                        fab,
                        "arrow_image"
                    )
                val intent = Intent(this, FinalizeReasonActivity::class.java)


                //parcelable list
                var bundle = Bundle()
                bundle.putString(Const.TYPE_COMPANY, typePrefixCode.toString())
                bundle.putParcelableArrayList(
                    Const.LIST_ITEM,
                    ArrayList<ResponseFinalizeCheck>(listItemsCheckedWithItems)
                )
                intent.putExtras(bundle)

                startActivity(intent, activityOptions.toBundle())
            }

        })
        //Failure
        companyTypeViewModel.error.observe(this, Observer<Error> {
            Log.e(TAG, it.toString())
            showProgress(false)
            val dialog = FailFragment()
            dialog.isCancelable = false
            dialog.show(supportFragmentManager, "")

        })

    }

    private fun showDialogFail(title: String, message: String) {
        failFragment = FailFragment.newInstance(title, message)
        failFragment!!.isCancelable = false
        failFragment!!.show(supportFragmentManager, "")
    }

    override fun onActionFromFailFragment() {
        var intent = Intent(this, LoginActivity::class.java)
        this.startActivity(intent)
        finish()
    }

    class WrapperFinalizeItem {

        var dataMatrixObject: DataMatrix.RBIumObject? = null
        var responseItemCheck: ResponseFinalizeCheck? = null
        var type: Int? = null
        var barcode: String? = null
        var itemNotFound: Boolean = false

    }


}
