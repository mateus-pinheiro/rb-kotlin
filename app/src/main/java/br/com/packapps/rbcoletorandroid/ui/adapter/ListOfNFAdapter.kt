package br.com.packapps.rbcoletorandroid.ui.adapter

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.model.response.ResponseEntryListNFs
import kotlinx.android.synthetic.main.item_nfe.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatterBuilder
import java.util.Locale
import java.util.*

class ListOfNFAdapter(val activity: Activity, private var listItem: List<ResponseEntryListNFs.Checkout>) :
    RecyclerView.Adapter<ListOfNFAdapter.MyViewHolder2>(), View.OnClickListener {

    lateinit var listener: OnNfItemAdapterListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder2 {
        //Init listener
        onAtachAdapter()

        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_nfe, parent, false)

        return MyViewHolder2(view)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }


    override fun onBindViewHolder(holder: MyViewHolder2, position: Int) {
        var checkout = listItem.get(position)
        holder.itemView.setOnClickListener {
            listener.onfItemAdapterClick(checkout)
            Log.i("TAG", "click item NF")
        }

        var startDate = Date(checkout.date!!)
        var endDate = Date(checkout.insertionTs!!)
        var format = SimpleDateFormat("dd/MM/yyyy")

        holder.tvBarcodeNf.text = checkout.docs!![0].key
        holder.tvStartDateNf.text = format.format(startDate).toString() //checkout.date!!.substring(0, 10)
        holder.tvEndDateNf.text = format.format(endDate).toString() //checkout.insertionTs!!.substring(0, 10)
        holder.tvQuantityItem.text = checkout.affectedItems.toString() + " itens"
    }

    fun updateListItens(list: MutableList<ResponseEntryListNFs.Checkout>) {
        this.listItem = list

        notifyDataSetChanged()
    }

    override fun onClick(view: View?) {
        listener.onfItemAdapterClick(listItem.get(view!!.clNfe.tag.toString().toInt()))
    }

    class MyViewHolder2(view: View) : RecyclerView.ViewHolder(view) {
        var tvBarcodeNf = view.findViewById<TextView>(R.id.tvBarcodeNf)
        var tvStartDateNf = view.findViewById<TextView>(R.id.tvStartDateNf)
        var tvEndDateNf = view.findViewById<TextView>(R.id.tvEndDateNf)
        var tvQuantityItem = view.findViewById<TextView>(R.id.tvQuantityItemsNf)

    }

    fun onAtachAdapter() {
        if (activity is OnNfItemAdapterListener) {
            listener = activity
        } else {
            throw RuntimeException(activity.toString() + " must implement OnNfItemAdapterListener")
        }
    }

    interface OnNfItemAdapterListener {
        fun onfItemAdapterClick(position: ResponseEntryListNFs.Checkout)
    }

}
