package br.com.packapps.rbcoletorandroid.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentTransaction
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.core.SingletonApp
import br.com.packapps.rbcoletorandroid.model.CheckoutTypeItem
import br.com.packapps.rbcoletorandroid.model.body.EntryFinishActionBody
import br.com.packapps.rbcoletorandroid.model.body.SupplierCompanyType
import br.com.packapps.rbcoletorandroid.model.response.*
import br.com.packapps.rbcoletorandroid.ui.adapter.CompaniesAdapter
import br.com.packapps.rbcoletorandroid.ui.fragment.ConfirmationFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.FailFragment
import br.com.packapps.rbcoletorandroid.ui.fragment.SearchCompaniesFragment
import br.com.packapps.rbcoletorandroid.util.Const
import br.com.packapps.rbcoletorandroid.viewmodel.CompanyTypeViewModel
import br.com.packapps.rbcoletorandroid.viewmodel.NfeViewModel

import kotlinx.android.synthetic.main.activity_exit.*
import kotlinx.android.synthetic.main.content_exit.*
import br.com.packapps.rbcoletorandroid.core.RepositoryUtils.hideKeyboard


class ExitActivity : ScanActivity(), CompaniesAdapter.OnCompaniesListener, FailFragment.OnFailFragmentListener,
    ConfirmationFragment.OnConfirmationFragmentListener, AdapterView.OnItemSelectedListener {

    val TAG = ExitActivity::class.java.simpleName
    var transaction: FragmentTransaction? = null


    private lateinit var nfeViewModel: NfeViewModel
    lateinit var companyTypeViewModel: CompanyTypeViewModel

    var responseNfeCheckout: ResponseNfeCheckout? = null
    var entryFinishActionBody: EntryFinishActionBody? = null

    lateinit var failFragment: FailFragment
    lateinit var confirmationFragment: ConfirmationFragment

    lateinit var companyNameWatcher: TextWatcher
    lateinit var companyTransportWatcher: TextWatcher
    lateinit var nfSearchWatcher: TextWatcher

    var itemSpinnerSelected: String? = null
    var codeItemSelected: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exit)
        setSupportActionBar(toolbar)

        observerNfeCheck()
        observerBarcodeScan()
        observerCompanyType()



        showProgress(true)
        companyTypeViewModel.getCompanyTypeFrom((SupplierCompanyType.queryValue))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        companyNameWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i(TAG, "onTextChanged: ${text}")
                if (text != null) {
                    if (text.length > 5) {
                        return
                    }
                    if (text.length > 4) {
                        removeAllChangeListener()
                        openFragmentSearchView(text, etFromFiscalRegistry.id)
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
                    if (text.length > 4) {
                        removeAllChangeListener()
                        openFragmentSearchView(text, etFromCompanyRegistry.id)
                    }
                }
            }
        }

        nfSearchWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            var stopWatcher = false

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i(TAG, "onTextChanged: ${text}")
                if (text != null) {
                    if (text.length > 43) {
                        if (!stopWatcher) {
                            stopWatcher = true
                            showProgress(true)
                            removeAllChangeListener()
                            nfeViewModel.getNfe(
                                text.toString(),
                                (System.currentTimeMillis().plus(1000 * 60 * 60 * 3) / 1000).toString()
                            )
                        }
                    } else {
                        stopWatcher = false
                    }
                }
            }
        }


        fab.setOnClickListener { view ->

            if (etFromFiscalRegistry.text.isNotEmpty() && etFromCompanyRegistry.text.isNotEmpty()) {
                var bundle = Bundle()
                bundle.putParcelable(Const.CHECKOUT_ITEM, responseNfeCheckout)
                bundle.putParcelable(Const.CHECKOUT_WITHOUT_NF, entryFinishActionBody)
                var intent = Intent(this, ExitDestinyActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            } else {
                showDialogFail("Falha", "Preencha os campos ou escaneie uma nota fiscal disponível.")
            }


        }

        etFromFiscalRegistry.setOnClickListener { view ->
            openFragmentSearchView("", etFromFiscalRegistry.id)
        }
        etFromCompanyRegistry.setOnClickListener { view ->
            openFragmentSearchView("", etFromCompanyRegistry.id)
        }

    }

    override fun onClickCompanyItemFromSearchFragment(companyName: ResponseCompanies, idWidget: Long) {
        Log.i(TAG, "click item adapter companies: ${companyName.toString()}")

        if (idWidget == etFromFiscalRegistry.id.toLong()) {
            etFromFiscalRegistry.setText(companyName.company?.name)
            entryFinishActionBody!!.fromFiscalCpId = companyName.company?.universalId
        } else if (idWidget == etFromCompanyRegistry.id.toLong()) {
            etFromCompanyRegistry.setText(companyName.company!!.name)
            entryFinishActionBody!!.fromUniversalCompanyId = companyName.company!!.universalId
        }

//        addAllTextChangeListener()

        fab.visibility = View.VISIBLE

        onBackPressed()

    }

    private fun removeAllChangeListener() {
        etFromFiscalRegistry.removeTextChangedListener(companyNameWatcher)
        etFromCompanyRegistry.removeTextChangedListener(companyTransportWatcher)
        etNfCheckout.removeTextChangedListener(nfSearchWatcher)
    }

    private fun addAllTextChangeListener() {
        etFromFiscalRegistry.addTextChangedListener(companyNameWatcher)
        etFromCompanyRegistry.addTextChangedListener(companyTransportWatcher)
        etNfCheckout.addTextChangedListener(nfSearchWatcher)
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

    override fun observerBarcodeScan() {
        scanResponseViewModel.barcode.observe(this, Observer {
            showProgress(true)
            if (it != null) {

                if (it.length != 44) {
                    showProgress(false)
                    Snackbar.make(window.decorView.rootView, "Este item não é uma NF!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    return@Observer
                }

                nfeViewModel.getNfe(
                    it,
                    (System.currentTimeMillis().plus(1000 * 60 * 60 * 3) / 1000).toString()
                )
            }

        })
    }

    private fun observerNfeCheck() {
        nfeViewModel =
            ViewModelProviders.of(this@ExitActivity).get(NfeViewModel::class.java)
        //Success
        nfeViewModel.response.observe(this, Observer {
            removeAllChangeListener()
            responseNfeCheckout = it!!
            showProgress(false)
            Log.i(TAG, "response: ${it.toString()}")
            if (it != null) {
                setViewFields(it)
            }

        })
        //Error
        nfeViewModel.error.observe(this, Observer {
            Log.e(TAG, it.toString())
            showProgress(false)
            showDialogConfirmation(
                "Confirmação",
                "Nenhum nota fiscal encontrada, deseja prosseguir sem nota?".plus("\n ${it!!.messages[0]}")
            )
            if (it.code == 401)
                showDialogFail("Falha no Login", "Faça o login novamente para acessar qualquer funcionalidade")

        })
    }

    private fun observerCompanyType() {
        companyTypeViewModel = ViewModelProviders.of(this).get(CompanyTypeViewModel::class.java)
        //Success
        companyTypeViewModel.response.observe(this, Observer<ResponseCompanyType> {
            Log.i(TAG, it.toString())
            showProgress(false)
            var typePrefixCode: String = ""
            if (it != null) {
                typePrefixCode = it.data?.company?.typePrefixCode.toString()
//                if(entryFinishActionBody == null)
//                    entryFinishActionBody = EntryFinishActionBody()
//                entryFinishActionBody!!.fromUniversalCompanyId = it.data?.company?.identifier
//                etFromCompanyRegistry.setText(it.data?.company?.name)
//                etFromCompanyRegistry.isEnabled = false
            }
            if (typePrefixCode.isNotEmpty()) {
                SingletonApp.getInstance().companyType = typePrefixCode
            }

            initSpinners()

            //active listener
            addAllTextChangeListener()

        })
        //Failure
        companyTypeViewModel.error.observe(this, Observer<Error> {
            Log.e(TAG, it.toString())
            showProgress(false)

            if (it!!.code == 401) {
                showDialogFail("Falha no login", "Por favor, acesso expirado, logue novamente. \n" + it.messages[0])
            } else {
                showDialogFail("Falha", "A busca da empresa não foi efetuada com sucesso.\n" + it.messages[0])
            }


        })


    }


    private fun setViewFields(response: ResponseNfeCheckout) {
        removeAllChangeListener()

        etNfCheckout.setText(response.id)

        if (entryFinishActionBody == null)
            entryFinishActionBody = EntryFinishActionBody()
        entryFinishActionBody!!.fromFiscalCpId = formatCnpj(response.sourceCompany?.cnpj)

//        var cnpjSourceCompany = response.sourceCompany?.cnpj
//        val re = Regex("[^A-Za-z0-9 ]")
//        cnpjSourceCompany = re.replace(cnpjSourceCompany!!, "")

        etFromFiscalRegistry.setText(response.sourceCompany?.name)
        etFromFiscalRegistry.isEnabled = false


        entryFinishActionBody!!.fromUniversalCompanyId = formatCnpj(response.sourceCompany?.cnpj)
        etFromCompanyRegistry.setText(response.sourceCompany?.name)
        etFromCompanyRegistry.isEnabled = false

    }

    private fun showDialogFail(title: String, message: String) {
        failFragment = FailFragment.newInstance(title, message)
        failFragment.isCancelable = false
        failFragment.show(supportFragmentManager, "")
    }

    private fun showDialogConfirmation(title: String, message: String) {
        confirmationFragment = ConfirmationFragment.newInstance(title, message)
        confirmationFragment.isCancelable = false
        confirmationFragment.show(supportFragmentManager, "")
    }


    private fun initSpinners() {

        var arrayTypeBalancesAdapter =
            ArrayAdapter.createFromResource(
                this,
                R.array.checkout_type_items_1,
                android.R.layout.simple_spinner_item
            )

        arrayTypeBalancesAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1)
        spCheckoutType.adapter = arrayTypeBalancesAdapter

        spCheckoutType.setOnItemSelectedListener(this)
        if (SingletonApp.getInstance().companyType.equals("IND")) {
            var arrayTypeBalancesAdapter =
                ArrayAdapter.createFromResource(
                    this,
                    R.array.checkout_type_items_1,
                    android.R.layout.simple_spinner_item
                )

            arrayTypeBalancesAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1)
            spCheckoutType.adapter = arrayTypeBalancesAdapter
        } else {

            var arrayTypeBalancesAdapter =
                ArrayAdapter.createFromResource(this, R.array.checkout_type_items, android.R.layout.simple_spinner_item)
            arrayTypeBalancesAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1)
            spCheckoutType.adapter = arrayTypeBalancesAdapter
        }

    }


    private fun openFragmentSearchView(text: CharSequence, idWidget: Int) {
        hideKeyboard(this)
        var fragmentSearchCompany = SearchCompaniesFragment.newInstance(text.toString(), idWidget.toLong())
        transaction = supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.container_search_exit, fragmentSearchCompany)
        transaction?.addToBackStack(null)
        transaction?.commit()

        fab.visibility = View.GONE
    }

//    override fun onRestart() {
//        addAllTextChangeListener()
//        super.onRestart()
//    }


    override fun onActionFromFailFragment() {
        var intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActionConfirmFromConfirmationFragment() {
        confirmationFragment.dismiss()
    }

    override fun onActionCancelFromConfirmationFragment() {
        confirmationFragment.dismiss()
        finish()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        try {
            var items: Array<CheckoutTypeItem.Items>
            var itemsIndustry: Array<CheckoutTypeItem.ItemsIndustry>

            itemSpinnerSelected = spCheckoutType.getItemAtPosition(position).toString()
            if (SingletonApp.getInstance().companyType.equals("IND")) {
                itemsIndustry = CheckoutTypeItem.ItemsIndustry.values()
                codeItemSelected = itemsIndustry[position].code
            } else {
                items = CheckoutTypeItem.Items.values()
                codeItemSelected = items[position].code
            }
            if (entryFinishActionBody != null)
                entryFinishActionBody!!.code = codeItemSelected
            else {
                entryFinishActionBody = EntryFinishActionBody()
                entryFinishActionBody!!.code = codeItemSelected
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Houve um erro", Toast.LENGTH_LONG).show()
        }
    }


    override fun onErrorGetListCompanies(error: Error?) {
        Toast.makeText(this, "error: " + error?.toString(), Toast.LENGTH_LONG).show()
        onBackPressed()
    }

    private fun formatCnpj(it: String?): String? {
        return it?.trim('.', '/')
    }


}
