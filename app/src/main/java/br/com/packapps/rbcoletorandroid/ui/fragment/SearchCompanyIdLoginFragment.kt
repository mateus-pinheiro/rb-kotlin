package br.com.packapps.rbcoletorandroid.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.model.body.CompaniesUserBody
import br.com.packapps.rbcoletorandroid.ui.LoginActivity
import br.com.packapps.rbcoletorandroid.ui.adapter.CompaniesIdLoginAdapter
import br.com.packapps.rbcoletorandroid.util.Const
import br.com.packapps.rbcoletorandroid.viewmodel.CompanyListViewModel
import kotlinx.android.synthetic.main.fragment_company_list.*

class SearchCompanyIdLoginFragment : Fragment() {
    val TAG = SearchCompanyIdLoginFragment::class.java.simpleName


    //    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var rvCompanies: RecyclerView
    private lateinit var companyListViewModel: CompanyListViewModel

    lateinit var adapter: CompaniesIdLoginAdapter
//    private var queryToSearch: String? = null
    private lateinit var flProgressSearchCompany: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_company_list, container, false)

        rvCompanies = mView.findViewById<RecyclerView>(R.id.rvCompanies)
        flProgressSearchCompany = mView.findViewById<FrameLayout>(R.id.flProgressSearchCompany)

        observerCompaniesCheck()

        // Set the adapter
        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = CompaniesIdLoginAdapter(activity)

        rvCompanies.layoutManager = layoutManager
        rvCompanies.adapter = adapter
        adapter.onAtachAdapter()


        flProgressSearchCompany.visibility = View.VISIBLE
//        if (queryToSearch == null) {
//            Handler().postDelayed({
//                callSearchQueryOnApi(queryToSearch)
//            }, 1000)
//        } else {
//            callSearchQueryOnApi(queryToSearch)
//        }
        callSearchQueryOnApi()


        return mView
    }


    private fun observerCompaniesCheck() {
        companyListViewModel =
            ViewModelProviders.of(this@SearchCompanyIdLoginFragment).get(CompanyListViewModel::class.java)
        //Success
        companyListViewModel.responseCompaniesByUser.observe(this, Observer {
            Log.i(TAG, "response: ${it.toString()}")
            if (it!!.data!!.companies != null) {
                var companyList = it!!.data!!.companies

                adapter.updateListCompanyAdapter(companyList!!)
                flProgressSearchCompany.visibility = View.GONE
                etSearchCompany.visibility = View.GONE
            }

        })
        //Error
        companyListViewModel.error.observe(this, Observer {
            Log.e(TAG, it.toString())
            adapter.onErrorGetList(it)
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
        fun newInstance(query: String) =
            SearchCompanyIdLoginFragment().apply {
                arguments = Bundle().apply {
                    putString(Const.QUERY_COMPANY, query)
                }
            }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var id = item?.itemId
        if (id == android.R.id.home) {
            return true
        }


        return false
    }


    private fun callSearchQueryOnApi() {
        companyListViewModel.checkCompaniesByUser(
            CompaniesUserBody(
                "{\n" +
                        "\n" +
                        "  companies {\n" +
                        "\n" +
                        "    id\n" +
                        "\n" +
                        "    identifier\n" +
                        "\n" +
                        "    name\n" +
                        "\n" +
                        "  }\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "}"
            )
        )
    }
}