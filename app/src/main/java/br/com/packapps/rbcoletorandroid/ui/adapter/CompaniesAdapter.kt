package br.com.packapps.rbcoletorandroid.ui.adapter

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.model.response.Error
import br.com.packapps.rbcoletorandroid.model.response.ResponseCompanies


import kotlinx.android.synthetic.main.item_company.view.*


class CompaniesAdapter(
    val context: FragmentActivity?) : RecyclerView.Adapter<CompaniesAdapter.ViewHolder>() {

    private var idWidget: Long = 0

    private var listCompanies : MutableList<ResponseCompanies> = mutableListOf()
    private var listener: OnCompaniesListener? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        onAtachAdapter()
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_company, parent, false)
        view.setOnClickListener {  }


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var company = listCompanies.get(position)
        holder.tvNameCompany.text = company.company!!.name
        holder.tvCNPJ.text = company.company!!.universalId

        holder.mView.setOnClickListener {
            listener?.onClickCompanyItemFromSearchFragment(company, idWidget)
        }

    }

    override fun getItemCount(): Int = listCompanies.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var tvNameCompany: TextView = mView.tvNameCompany
        var tvCNPJ: TextView = mView.tvCNPJ

    }


    fun onAtachAdapter() {
        if (context is OnCompaniesListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnReasonAdapterListener")
        }
    }


    fun updateListCompanyAdapter(list : MutableList<ResponseCompanies>){
        listCompanies = list

        notifyDataSetChanged()
    }

    fun setWidgetId(idWidget: Long) {
        this.idWidget = idWidget
    }

    fun onErrorGetList(error : Error?) {
        listener?.onErrorGetListCompanies(error)
    }


    interface OnCompaniesListener{
        fun onClickCompanyItemFromSearchFragment(companyName: ResponseCompanies, idWidget: Long)
        fun onErrorGetListCompanies(error : Error?)
    }
}
