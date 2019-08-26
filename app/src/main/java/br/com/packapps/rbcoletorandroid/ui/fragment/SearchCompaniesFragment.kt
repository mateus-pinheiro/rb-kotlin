package br.com.packapps.rbcoletorandroid.ui.fragment

import android.os.Build
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.FrameLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.core.RepositoryUtils
import br.com.packapps.rbcoletorandroid.ui.adapter.CompaniesAdapter
import br.com.packapps.rbcoletorandroid.util.Const

import br.com.packapps.rbcoletorandroid.viewmodel.CompanyListViewModel
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.EditText
import kotlinx.android.synthetic.main.content_exit.*
import kotlinx.android.synthetic.main.fragment_company_list.*




class SearchCompaniesFragment : Fragment(), SearchView.OnQueryTextListener {


    val TAG = SearchCompaniesFragment::class.java.simpleName


    //    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var rvCompanies : RecyclerView
    private lateinit var companyListViewModel : CompanyListViewModel
    lateinit var nfSearchWatcher: TextWatcher

    lateinit var adapter: CompaniesAdapter
    private var queryToSearch : String? = null
    private lateinit var flProgressSearchCompany : FrameLayout
    private lateinit var etSearchCompanies: EditText
    var idWidget : Long = 0
    var mView1 : View? = null


    private var idWidgetAux: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nfSearchWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i(TAG, "onTextChanged: ${text}")
                if (text != null) {
                    if (text.length > 6) {
                        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                        callSearchQueryOnApi(text.toString())
                    }
                }
            }
        }

        Handler().post {
            arguments?.let {
                queryToSearch = it.getString(Const.QUERY_COMPANY)
                idWidgetAux = it.getLong(Const.ID_WIDGET_FROM)

            }

            activity?.runOnUiThread {
                callSearchQueryOnApi(queryToSearch)
                idWidget = idWidgetAux
                adapter.setWidgetId(idWidget)
            }
        }



    }

    private fun callSearchQueryOnApi(queryToSearch: String?) {
        if (queryToSearch!!.isNotEmpty()){
            companyListViewModel.checkCompanies(queryToSearch!! , (System.currentTimeMillis().plus(1000*60*60*3) / 1000).toString(), 100)
            flProgressSearchCompany.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_company_list, container, false)
        mView1 = inflater.inflate(R.layout.fragment_company_list, container, false)
        rvCompanies = mView.findViewById<RecyclerView>(R.id.rvCompanies)
        flProgressSearchCompany = mView.findViewById<FrameLayout>(R.id.flProgressSearchCompany)
        etSearchCompanies = mView.findViewById<EditText>(R.id.etSearchCompany)

        observerCompaniesCheck()

        // Set the adapter
        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = CompaniesAdapter(activity)
        adapter.setWidgetId(idWidget)

        rvCompanies.layoutManager = layoutManager
        rvCompanies.adapter = adapter

        etSearchCompanies.addTextChangedListener(nfSearchWatcher)

        setHasOptionsMenu(false)

        return mView
    }


    override fun onResume() {
        etSearchCompanies.requestFocus()
        etSearchCompanies.showKeyboard()
        adapter.setWidgetId(idWidget)
        super.onResume()
    }

    fun EditText.showKeyboard() {
        if (requestFocus()) {
            (activity!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(this, SHOW_IMPLICIT)
            setSelection(text.length)
        }
    }


    private fun observerCompaniesCheck() {
        companyListViewModel =
                ViewModelProviders.of(this@SearchCompaniesFragment).get(CompanyListViewModel::class.java)
        //Success
        companyListViewModel.response.observe(this, Observer {
            Log.i(TAG, "response: ${it.toString()}")
            var companyList = it
            etSearchCompanies.setText("")
            adapter.updateListCompanyAdapter(companyList!!)
            flProgressSearchCompany.visibility = View.GONE

        })
        //Error
        companyListViewModel.error.observe(this, Observer {
            Log.e(TAG, it.toString())
            flProgressSearchCompany.visibility = View.GONE
        })
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnListFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
//        }
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }
//
//
//    interface OnListFragmentInteractionListener {
//        fun onListFragmentInteraction(item: DummyItem?)
//    }

    companion object {
        @JvmStatic
        fun newInstance(query: String, idWidget: Long) =
            SearchCompaniesFragment().apply {
                arguments = Bundle().apply {
                    putLong(Const.ID_WIDGET_FROM, idWidget)
                    putString(Const.QUERY_COMPANY, query)
                }
            }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search_menu, menu)

        //SearchView
        var searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.queryHint = "Empresa"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            searchView.isFocusedByDefault = true
            searchView.focusable = android.view.View.FOCUSABLE
        }
        searchView.setOnQueryTextListener(this@SearchCompaniesFragment)
    }





    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var id = item?.itemId
        if (id == android.R.id.home){
            return true
        }

        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.i(TAG, "onQueryTextSubmit: " + query)
        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        Log.i(TAG, "onQueryTextChange: " + query)
        if (query!!.length >= 5){
            callSearchQueryOnApi(query)
            // hide keyboard
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.windowToken, 0)
        }

        return false
    }
}
