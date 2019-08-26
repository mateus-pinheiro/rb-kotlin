package br.com.packapps.rbcoletorandroid.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.model.response.ResponseCompanies
import br.com.packapps.rbcoletorandroid.model.response.ResponseNfeCheckout
import br.com.packapps.rbcoletorandroid.util.Const
import android.support.v4.app.FragmentTransaction
import android.widget.Toast
import br.com.packapps.rbcoletorandroid.core.RepositoryUtils
import br.com.packapps.rbcoletorandroid.model.body.EntryFinishActionBody
import br.com.packapps.rbcoletorandroid.model.response.Error
import br.com.packapps.rbcoletorandroid.ui.adapter.CompaniesAdapter
import br.com.packapps.rbcoletorandroid.ui.fragment.FailFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.SearchCompaniesFragment

import kotlinx.android.synthetic.main.activity_destiny.*
import kotlinx.android.synthetic.main.content_destiny.*

class ExitDestinyActivity : BaseActivity(), FailFragment.OnFailFragmentListener, CompaniesAdapter.OnCompaniesListener {



    val TAG = ExitActivity::class.java.simpleName

    var responseNfeCheckout: ResponseNfeCheckout? = null
    var entryFinishActionBody: EntryFinishActionBody? = null

    lateinit var failFragment: FailFragment
    lateinit var companyDestinyWatcher: TextWatcher
    lateinit var companyTransportWatcher: TextWatcher
    lateinit var localWatcher: TextWatcher


    var transaction: FragmentTransaction? = null
    var itemSelectedCode : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destiny)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        companyDestinyWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i(TAG, "onTextChanged: ${text}")
                if (text != null) {
                    if (text.length > 5) {
                        return
                    }
                    if (text!!.length > 4) {
                        removeAllTextChangeListener()
                        openFragmentSearchView(text, etToFiscalRegistry.id)
                    }
                }
            }
        }

        companyTransportWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i(TAG, "onTextChanged: ${text}")
                if (text != null) {
                    if (text.length > 5) {
                        return
                    }
                    if (text!!.length > 4) {
                        removeAllTextChangeListener()
                        openFragmentSearchView(text, etByCompanyRegistry.id)
                    }
                }
            }
        }
        localWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i(TAG, "onTextChanged: ${text}")
                if (text != null) {
                    if (text.length > 5) {
                        return
                    }
                    if (text!!.length > 4) {
                        removeAllTextChangeListener()
                        openFragmentSearchView(text, etToCompanyRegistry.id)
                    }
                }
            }
        }



//        initSpinner()
        val bundle = intent.extras
        if (bundle != null) {
            responseNfeCheckout = bundle.getParcelable(Const.CHECKOUT_ITEM)
            entryFinishActionBody = bundle.getParcelable(Const.CHECKOUT_WITHOUT_NF)

            runOnUiThread {
                if (responseNfeCheckout != null){
                    setFields(responseNfeCheckout)
                }

                addAllTextChangeListener()
            }

        }


        fab.setOnClickListener { view ->

            if (etToFiscalRegistry.text.isNotEmpty() && etToCompanyRegistry.text.isNotEmpty() && etByCompanyRegistry.text.isNotEmpty()) {

                var bundle = Bundle()
                bundle.putParcelable(Const.CHECKOUT_ITEM, responseNfeCheckout)
                bundle.putParcelable(Const.CHECKOUT_WITHOUT_NF, entryFinishActionBody)
                var intent = Intent(this, ExitDocumentsActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            } else {
                showDialogFail("Falha", "Preencha todos os campos, por favor.")
            }

        }

        etToFiscalRegistry.setOnClickListener { view ->
            openFragmentSearchView("", etToFiscalRegistry.id)
        }
        etToCompanyRegistry.setOnClickListener { view ->
            openFragmentSearchView("", etToCompanyRegistry.id)
        }
        etByCompanyRegistry.setOnClickListener { view ->
            openFragmentSearchView("", etByCompanyRegistry.id)
        }
    }

    private fun addAllTextChangeListener() {
        etToFiscalRegistry.addTextChangedListener(companyDestinyWatcher)
        etToCompanyRegistry.addTextChangedListener(localWatcher)
        etByCompanyRegistry.addTextChangedListener(companyTransportWatcher)
    }

    private fun removeAllTextChangeListener() {
        etToFiscalRegistry.removeTextChangedListener(companyDestinyWatcher)
        etToCompanyRegistry.removeTextChangedListener(localWatcher)
        etByCompanyRegistry.removeTextChangedListener(companyTransportWatcher)
    }

    override fun onRestart() {
        addAllTextChangeListener()
        super.onRestart()
    }


    private fun setFields(rNf: ResponseNfeCheckout?) {
        if (rNf != null){
            entryFinishActionBody!!.toFiscalCpId = rNf.destinationCompany!!.cnpj
            etToFiscalRegistry.setText(rNf.destinationCompany!!.name)
            etToFiscalRegistry.isEnabled = false

            if (rNf.transportationCompany != null) {
                entryFinishActionBody!!.byUniversalCompanyId = rNf.transportationCompany!!.cnpj
                etByCompanyRegistry.setText(rNf.transportationCompany!!.name)
                etByCompanyRegistry.isEnabled = false
            }
        }

    }



    override fun onClickCompanyItemFromSearchFragment(companyName: ResponseCompanies, idWidget: Long) {
        Log.i(TAG, "click item adapter companies: ${companyName.toString()}")
        removeAllTextChangeListener()

        if (idWidget == etToFiscalRegistry.id.toLong()) {
            etToFiscalRegistry.setText(companyName.company?.name)
//            etToFiscalRegistry.isEnabled = false
            entryFinishActionBody!!.toFiscalCpId = companyName.company?.universalId
        }
        else if (idWidget == etByCompanyRegistry.id.toLong()){
            etByCompanyRegistry.setText(companyName.company?.name)
//            etByCompanyRegistry.isEnabled = false
            entryFinishActionBody!!.byUniversalCompanyId = companyName.company?.universalId
        }
        else if (idWidget == etToCompanyRegistry.id.toLong()){
            etToCompanyRegistry.setText(companyName.company?.name)
//            etToCompanyRegistry.isEnabled = false
            entryFinishActionBody!!.toUniversalCompanyId = companyName.company?.universalId
        }

//        addAllTextChangeListener()
        onBackPressed()
    }

    override fun onErrorGetListCompanies(error: Error?) {
        Toast.makeText(this, "error: " + error?.toString(), Toast.LENGTH_LONG).show()
        onBackPressed()
    }

    override fun onBackPressed() {
        if (transaction != null) {
            if (supportFragmentManager.fragments.size > 0) {
                supportFragmentManager.popBackStackImmediate()

                fab.visibility = View.VISIBLE

                addAllTextChangeListener()

                return
            }
        }

        super.onBackPressed()
    }

    override fun onActionFromFailFragment() {
        failFragment.dismiss()
    }

    private fun openFragmentSearchView(text: CharSequence, idWidget: Int) {
        RepositoryUtils.hideKeyboard(this)
        var fragmentSearchCompany = SearchCompaniesFragment.newInstance(text.toString(), idWidget.toLong())
        transaction = supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.container_search_exit, fragmentSearchCompany)
        transaction?.addToBackStack(null)
        transaction?.commit()

        fab.visibility = View.GONE
    }

    private fun showDialogFail(title: String, message: String) {
        failFragment = FailFragment.newInstance(title, message)
        failFragment.isCancelable = false
        failFragment.show(supportFragmentManager, "")
    }



//    fun initSpinner(){
//        var arrayTypeBalancesAdapter =
//            ArrayAdapter.createFromResource(this, R.array.type_balances, android.R.layout.simple_spinner_item)
//        arrayTypeBalancesAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1)
//        etDestinationCompany.adapter = arrayTypeBalancesAdapter
//
//        var arrayOriginLocalAdapter =
//            ArrayAdapter.createFromResource(this, R.array.origin_local, android.R.layout.simple_spinner_item)
//        arrayOriginLocalAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//        etLocalDestination.adapter = arrayOriginLocalAdapter
//
//
//        etCompanyTransport.adapter = arrayOriginLocalAdapter
//    }

}
